package code.name.monkey.retromusic.fragments.downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.networkModule
import code.name.monkey.retromusic.repository.RealRepository
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.lang.Exception

class DownloaderViewModel : ViewModel() {

    data class SongInfo(val title: String, val author: String, val path: String)
    data class DownloadResult(val successful: Boolean, val songInfo: SongInfo)
    data class DownloaderSearchResult(val successful: Boolean, val results: List<SearchResult>?)

    var downloading: Boolean = false

    val results: MutableLiveData<DownloaderSearchResult> by lazy {
        MutableLiveData<DownloaderSearchResult>(DownloaderSearchResult(false, listOf()))
    }

    val downloadResult: MutableLiveData<DownloadResult> by lazy {
        MutableLiveData<DownloadResult>()
    }

    val progress: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private var service: YouTube? = null

    companion object {
        const val DEBUG_TAG = "Downloader ViewModel"
        private const val DESTINATION_TERM = "Destination:"
    }


    fun downloadVideo(link: String, builder: NotificationCompat.Builder, context: Context, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            downloading = true
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "music")
            val request = YoutubeDLRequest(link)
            request.addOption("-o", "${dir.absolutePath}/%(title)s.%(ext)s")
            request.addOption("-f", "bestaudio")
            request.addOption("--extract-audio")
            request.addOption("--audio-format", "mp3")
            request.addOption("--audio-quality", 0)

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Downloading"
                val descriptionText = "Downloading a music track"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    DownloaderFragment.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            }
            val notification = NotificationCompat.Builder(
                context,
                DownloaderFragment.NOTIFICATION_CHANNEL_ID
            )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_download_music)
                .setContentTitle(context.getString(R.string.notification_downloading))
                .setOnlyAlertOnce(true)
                .setProgress(100, 0, false)
            notificationManager.notify(id, notification.build())
            try {
                val response = YoutubeDL.getInstance().execute(request) { dProgress: Float, _ ->
                    //showProgress(id, "Downloading", progress.toInt(), 100, notificationManager, context, builder)
                    progress.postValue(dProgress.toInt())
                    notification.setProgress(100, dProgress.toInt(), false)
                    notificationManager.notify(id, notification.build())
                }
                progress.postValue(100)
                notificationManager.cancel(id)
                notification
                    .setProgress(0, 0, false)
                    .setContentTitle(context.getString(R.string.notification_finished))
                showProgress(id, "Finished", 0, 0, notificationManager, context, builder)
                val info = YoutubeDL.getInstance().getInfo(request)
                val index = response.out.lastIndexOf(DESTINATION_TERM) + DESTINATION_TERM.length + 1
                val path = response.out.substring(startIndex = index).takeWhile { it != '\n' }
                Log.d(DEBUG_TAG, path)
                downloadResult.postValue(
                    DownloadResult(
                        songInfo = SongInfo(
                            title = info.title,
                            author = info.uploader,
                            path = path
                        ),
                        successful = true
                    )
                )
            } catch (e: YoutubeDLException) {
                Log.e(DEBUG_TAG, e.stackTrace.toString())
                downloadResult.postValue(
                    DownloadResult(successful = false, songInfo = SongInfo("", "", ""))
                )
            } finally {
                downloading = false
            }
        }
    }
    // TODO: remove this method
    private fun showProgress(
        id: Int,
        name: String,
        progress: Int,
        progressMax: Int,
        notificationManager: NotificationManager?,
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        val notification = NotificationCompat.Builder(
            context,
            DownloaderFragment.NOTIFICATION_CHANNEL_ID
        )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_download_music)
            .setContentTitle(name)
            .setProgress(progressMax, progress, false)
            .build()
        notificationManager?.notify(id, notification)
        //notificationManager.notify(id, builder.build())

    }

    fun searchVideos(search: String): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            if (service == null) {
                val transport = AndroidHttp.newCompatibleTransport()
                val jsonFactory = JacksonFactory.getDefaultInstance()
                service = YouTube.Builder(
                    transport, jsonFactory, null
                )   .setApplicationName("Retro-Music-DSearch")
                    .build()
            }
            val maxResults: Long = 20
            val request = service!!.search().list("snippet")
                .set("q", search)
                .set("maxResults", maxResults)
                .set("key", "")

            try {
                val result = request.execute()
                val videos = result.items
                if (videos != null) {
                    results.postValue(DownloaderSearchResult(true, videos))
                }
            } catch (e: Exception) {
                Log.e(DEBUG_TAG, e.toString())
                results.postValue(DownloaderSearchResult(false, null))
            }
        }
    }
}