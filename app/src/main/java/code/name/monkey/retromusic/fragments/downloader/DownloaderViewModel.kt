package code.name.monkey.retromusic.fragments.downloader

import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DownloaderViewModel : ViewModel() {
    fun downloadVideo(link: String, builder: NotificationCompat.Builder) {
        viewModelScope.launch(Dispatchers.IO) {
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "youtube_dl")
            val request = YoutubeDLRequest(link)
            request.addOption("-o", dir.absolutePath + "/%(title)s.%(ext)s")
            request.addOption("-f", "bestaudio")
            request.addOption("--extract-audio")
            request.addOption("--audio-format", "mp3")

            YoutubeDL.getInstance().execute(request) { progress, _ ->
                builder.setProgress(
                    100,
                    progress.toInt(),
                    false
                )
            }
        }
    }
}