package code.name.monkey.retromusic.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import code.name.monkey.retromusic.R // Assuming you have a R.drawable.ic_notification or similar
import com.google.gson.JsonParser
import okhttp3.*
import java.io.File
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import code.name.monkey.retromusic.repository.RealSongRepository
import code.name.monkey.retromusic.model.SongTMPContainer
import okhttp3.OkHttpClient
import androidx.documentfile.provider.DocumentFile
import java.io.FileNotFoundException
import androidx.core.net.toUri
import code.name.monkey.retromusic.network.InternetConnection

object LyricsGetter {

    private const val LYRICS_CHANNEL_ID = "lyrics_channel"
    private const val LYRICS_NOTIFICATION_ID = 2 // Different from metadata helper

    private fun createLyricsNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lyrics Downloader"
            val descriptionText = "Notifications for lyrics download status"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(LYRICS_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun writeLyricsToFile(
        file: File?,
        lrcContent: String,
        context: Context,
        song: SongTMPContainer,
        sdCardPath: String?
    ) {
        try {
            file?.writeText(lrcContent)
        } catch (e: FileNotFoundException) {
            handleFileNotFoundException(context, song, file, lrcContent, sdCardPath)
        }
    }

    fun handleFileNotFoundException(
        context: Context,
        song: SongTMPContainer,
        file: File?,
        lrc: String,
        sdCardPath: String?
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && !song.data.contains("/storage/emulated/0") && sdCardPath != null) {
            val sd = context.externalCacheDirs[1].absolutePath.substring(
                0,
                context.externalCacheDirs[1].absolutePath.indexOf("/Android/data")
            )
            val path = file?.absolutePath?.substringAfter(sd)?.split("/")?.dropLast(1)
            var sdCardFiles = DocumentFile.fromTreeUri(context, sdCardPath.toUri())
            for (element in path!!) {
                for (sdCardFile in sdCardFiles!!.listFiles()) {
                    if (sdCardFile.name == element) {
                        sdCardFiles = sdCardFile
                    }
                }
            }
            sdCardFiles?.listFiles()?.forEach {
                if (it.name == file.name) {
                    it.delete()
                    return@forEach
                }
            }
            sdCardFiles?.createFile("text/lrc", file.name)?.let {
                val outputStream = context.contentResolver.openOutputStream(it.uri)
                outputStream?.write(lrc.toByteArray())
                outputStream?.close()
            }
        } else {
            println("Unable to handle FileNotFoundException for: ${file?.absolutePath}")
        }
    }

    fun String.toLrcFile(): File? {
        return if (this.isNotEmpty()) {
            File(this.substringBeforeLast('.') + ".lrc")
        } else {
            null
        }
    }

    fun fetchLyricsForSong(song: SongTMPContainer): String? {
        val client = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        var lyricsContentToWrite = ""
        val artist = song.artistName?.joinToString(" ") ?: ""
        val title = song.title
        val request = Request.Builder().url(buildLyricsApiUrl(artist, title)).get().build()
        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body.string()
                    if (body.isNullOrBlank()) return ""
                    val json = JsonParser().parse(body).asJsonObject
                    val syncedLyrics = if (json.has("syncedLyrics") && !json.get("syncedLyrics").isJsonNull) json.get("syncedLyrics").asString else null
                    val plainLyrics = if (json.has("plainLyrics") && !json.get("plainLyrics").isJsonNull) json.get("plainLyrics").asString else null
                    val rawApiLyrics = syncedLyrics ?: plainLyrics ?: ""
                    lyricsContentToWrite = removeHtmlTags(rawApiLyrics)
                    if (lyricsContentToWrite.isBlank() && (!syncedLyrics.isNullOrBlank() || !plainLyrics.isNullOrBlank())) {
                        return ""
                    }
                } else if (response.code == 404) {
                    return "No Lyrics Found"
                } else {
                    return ""
                }
            }
        } catch (e: Exception) {
            println("Error fetching lyrics for ${song.title}: ${e.message}")
            return ""
        }
        return lyricsContentToWrite
    }

    fun removeHtmlTags(input: String): String {
        return input.replace(Regex("<.*?>"), "")
    }

    fun buildLyricsApiUrl(artist: String, title: String): String {
        val baseUrl = "https://lrclib.net/api/get"
        val artistParam = URLEncoder.encode(artist, "UTF-8")
        val titleParam = URLEncoder.encode(title, "UTF-8")
        return "$baseUrl?artist_name=$artistParam&track_name=$titleParam"
    }

    fun downloadLyrics(context: Context) {
        createLyricsNotificationChannel(context)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, LYRICS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Lyrics Download")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

        notificationBuilder.setContentText("Preparing to download lyrics...")
            .setProgress(0, 0, true)
        notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())

        var totalWaitTimeMillis = 0L
        val initialSleepTimeMillis = 2 * 60 * 1000L // 2 minutes
        val thirtyMinThresholdSleepTimeMillis = 11000L * 60L * 15L // 165 minutes
        val oneHourThresholdSleepTimeMillis = 20 * 60 * 1000L     // 20 minutes
        val thirtyMinutesMillis = 30 * 60 * 1000L
        val oneHourMillis = 60 * 60 * 1000L
        var sleepDurationForThisIterationMillis: Long

        while (!InternetConnection.hasInternetConnection(context)) {
            if (totalWaitTimeMillis >= oneHourMillis) {
                sleepDurationForThisIterationMillis = oneHourThresholdSleepTimeMillis
            } else if (totalWaitTimeMillis >= thirtyMinutesMillis) {
                sleepDurationForThisIterationMillis = thirtyMinThresholdSleepTimeMillis
            } else {
                sleepDurationForThisIterationMillis = initialSleepTimeMillis
            }

            val nextCheckInMinutes = sleepDurationForThisIterationMillis / (60 * 1000)
            val totalWaitTimeSoFarMinutes = totalWaitTimeMillis / (60 * 1000)
            val waitMsg = if (totalWaitTimeMillis == 0L) {
                "Waiting for internet. Retrying in $nextCheckInMinutes min."
            } else {
                "Still no internet. Retrying in $nextCheckInMinutes min. Total wait: $totalWaitTimeSoFarMinutes min."
            }
            notificationBuilder
                .setContentText(waitMsg)
                .setProgress(0, 0, true)
            notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())

            try {
                Thread.sleep(sleepDurationForThisIterationMillis)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                notificationBuilder
                    .setContentText("Lyrics download interrupted while waiting for internet.")
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())
                return
            }
            totalWaitTimeMillis += sleepDurationForThisIterationMillis
        }

        notificationBuilder.setContentText("Starting lyrics download...")
            .setProgress(0,0,true) // Reset to indeterminate before song processing
        notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())

        val songRepository = RealSongRepository(context)
        val deviceSongs = songRepository.songs().map {
            SongTMPContainer(
                title = it.title,
                artistName = it.artistName
                    .split(',', '/')
                    .map { name -> name.trim() }
                    .filter { name -> name.isNotEmpty() },
                data = it.data,
                year = it.year,
                liked = false,
                favorite = false,
                rating = 0
            )
        }

        if (deviceSongs.isEmpty()) {
            notificationBuilder
                .setContentText("No songs found on device to download lyrics for.")
                .setProgress(0, 0, false)
                .setOngoing(false)
            notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())
            return
        }

        var songsProcessedCount = 0
        val totalSongsToProcess = deviceSongs.size

        notificationBuilder.setProgress(totalSongsToProcess, songsProcessedCount, false)

        for (song in deviceSongs) {
            songsProcessedCount++
            notificationBuilder
                .setContentText("Processing ${song.title} ($songsProcessedCount/$totalSongsToProcess)")
                .setProgress(totalSongsToProcess, songsProcessedCount, false)
            notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())
            if(songsProcessedCount == totalSongsToProcess){
                notificationBuilder
                    .setContentText("Lyrics download complete. Processed $songsProcessedCount songs.")
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())
            }
            val file = song.data.toLrcFile()
            if (doesFileExist(file)) {
                continue
            }
            val lyrics = fetchLyricsForSong(song)
            if (lyrics != null && lyrics != "No Lyrics Found" && lyrics.isNotBlank()) {
                 writeLyricsToFile(file, lyrics, context, song, null)
            } else if (lyrics == "No Lyrics Found") {
                continue
            }
            Thread.sleep(500)
        }

        notificationBuilder
            .setContentText("Lyrics download complete. Processed $songsProcessedCount songs.")
            .setProgress(0, 0, false)
            .setOngoing(false)
        notificationManager.notify(LYRICS_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun doesFileExist(file: File?): Boolean {
        return file?.exists() == true && file.isFile
    }
}
