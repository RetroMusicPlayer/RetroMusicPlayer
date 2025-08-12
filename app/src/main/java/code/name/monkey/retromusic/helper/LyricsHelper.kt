package code.name.monkey.retromusic.helper

import android.content.Context
import android.os.Build
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
            println("Unable to handle FileNotFoundException")
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
            .readTimeout(30, TimeUnit.SECONDS) // Also good to set read timeout
            .build()
        var lyricsContentToWrite = "" // Default to null (meaning no content or error)
        val artist = song.artistName?.joinToString(" ") ?: ""
        val title = song.title
        val request = Request.Builder().url(buildLyricsApiUrl(artist, title)).get().build()
        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body.string()
                    val json = JsonParser().parse(body).asJsonObject // Use try-catch for parsing
                    val syncedLyrics = if (json.has("syncedLyrics") && !json.get("syncedLyrics").isJsonNull) json.get("syncedLyrics").asString else null
                    val plainLyrics = if (json.has("plainLyrics") && !json.get("plainLyrics").isJsonNull) json.get("plainLyrics").asString else null
                    val rawApiLyrics = syncedLyrics ?: plainLyrics ?: ""
                    lyricsContentToWrite = removeHtmlTags(rawApiLyrics)
                    if (lyricsContentToWrite.isBlank() && (syncedLyrics != null || plainLyrics != null)) {
                        return "" // Or specific placeholder
                    }
                } else if (response.code == 404) {
                    return "No Lyrics Found" // Or LYRICS_NOT_FOUND_PLACEHOLDER if you want to distinguish
                }
            }
        } catch (e: Exception) {
            return "" // Write empty on exception
        }
        return lyricsContentToWrite
    }

    fun removeHtmlTags(input: String): String {
        return input.replace(Regex("<.*?>"), "")
    }

    fun buildLyricsApiUrl(artist: String, title: String): String {
        val baseUrl = "https://lrclib.net/api/get" // Corrected base URL if this was a typo
        val artistParam = URLEncoder.encode(artist, "UTF-8")
        val titleParam = URLEncoder.encode(title, "UTF-8")
        // Add other params like album and duration if your API supports them
        return "$baseUrl?artist_name=$artistParam&track_name=$titleParam"
    }

    fun downloadLyrics(context: Context) {
        if(InternetConnection.hasInternetConnection(context)) {
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
            for (song in deviceSongs){
                val file = song.data.toLrcFile()
                if (doesFileExist(file)){
                    continue
                }
                val lyrics = fetchLyricsForSong(song) ?: ""
                writeLyricsToFile(file, lyrics, context, song, null)
            }
        }
    }
    fun doesFileExist(file: File?): Boolean {
        return file?.exists() == true && file.isFile == true
    }
}
