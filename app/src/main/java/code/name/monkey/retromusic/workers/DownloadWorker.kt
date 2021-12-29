package code.name.monkey.retromusic.workers

import android.content.Context
import android.os.Environment
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import java.io.File
import java.lang.Exception

class DownloadWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters)
{
    override fun doWork(): Result {

        /*val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "youtube_dl")
        val request = YoutubeDLRequest(link)
        request.addOption("-o", dir.absolutePath + "/%(title)s.%(ext)s")
        request.addOption("-f", "bestaudio")
        request.addOption("--extract-audio")
        request.addOption("--audio-format", "mp3")

        try {
            YoutubeDL.getInstance().execute(request)
        } catch (e: Exception) {
            return Result.failure()
        }*/

        return Result.success()
    }
}