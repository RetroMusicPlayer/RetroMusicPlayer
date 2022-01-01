package code.name.monkey.retromusic.fragments.downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.tageditor.AbsTagEditorActivity
import code.name.monkey.retromusic.activities.tageditor.SongTagEditorActivity
import code.name.monkey.retromusic.adapter.YTSearchResult
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.PreferenceUtil.safSdCardUri
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.YoutubeDLResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DownloaderViewModel : ViewModel() {

    data class SongInfo(val title: String, val author: String, val path: String)

    var results: Array<YTSearchResult>? = null

    val songInfo: MutableLiveData<SongInfo> by lazy {
        MutableLiveData<SongInfo>()
    }


    fun downloadVideo(link: String, builder: NotificationCompat.Builder, context: Context, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Downloader", " Uri: ${PreferenceUtil.safSdCardUri}")
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "music")
            val request = YoutubeDLRequest(link)
            request.addOption("-o", "${dir.absolutePath}/%(title)s.%(ext)s")
            request.addOption("-f", "bestaudio")
            request.addOption("--extract-audio")
            request.addOption("--audio-format", "mp3")
            request.addOption("--audio-quality", 0)

            var notificationManager: NotificationManager? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Downloading"
                val descriptionText = "Downloading a music track"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    DownloaderFragment.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as
                            NotificationManager?
                notificationManager?.createNotificationChannel(channel)
            }
            YoutubeDL.getInstance().execute(request) { progress: Float, _ ->
                showProgress(id, "Downloading", progress.toInt(), notificationManager, context, builder)
            }
            val info = YoutubeDL.getInstance().getInfo(request)
            songInfo.postValue(
                SongInfo(
                    title = info.title,
                    author = info.uploader,
                    path = "${dir.absolutePath}/${info.title}.mp3"
                )
            )
        }
    }

    private fun showProgress(
        id: Int,
        name: String,
        progress: Int,
        notificationManager: NotificationManager?,
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        val notification = NotificationCompat.Builder(
            context,
            DownloaderFragment.NOTIFICATION_CHANNEL_ID
        )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_download_music)
            .setContentTitle(name)
            .setProgress(100, progress, false)
            .build()
        notificationManager?.notify(id, notification)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }

    }

    private fun createNotificationChannel(): NotificationChannel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Downloading"
            val descriptionText = "Downloading a music track"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            return NotificationChannel(
                DownloaderFragment.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
        }
        return null
    }

    fun searchVideos(search: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val ytRequest = YoutubeDLRequest("ytsearch:$search")
            val ytdl = YoutubeDL.getInstance()
            val info = ytdl.getInfo(ytRequest)
            print("finished")
            print(info.title)

        }
    }
}