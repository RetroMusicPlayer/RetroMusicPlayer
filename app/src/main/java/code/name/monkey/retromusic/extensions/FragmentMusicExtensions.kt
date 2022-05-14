package code.name.monkey.retromusic.extensions

import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.RetroUtil
import org.jaudiotagger.audio.AudioFileIO
import java.io.File
import java.net.URLEncoder

fun getSongInfo(song: Song): String {
    val file = File(song.data)
    if (file.exists()) {
        return try {
            val audioHeader = AudioFileIO.read(File(song.data)).audioHeader
            val string: StringBuilder = StringBuilder()
            val uriFile = file.toUri()
            string.append(getMimeType(uriFile.toString())).append(" • ")
            string.append(audioHeader.bitRate).append(" kb/s").append(" • ")
            string.append(RetroUtil.frequencyCount(audioHeader.sampleRate.toInt()))
                .append(" kHz")
            string.toString()
        } catch (er: Exception) {
            " - "
        }
    }
    return "-"
}

private fun getMimeType(url: String): String {
    var type: String? = MimeTypeMap.getFileExtensionFromUrl(
        URLEncoder.encode(url, "utf-8")
    ).uppercase()
    if (type == null) {
        type = url.substring(url.lastIndexOf(".") + 1)
    }
    return type
}