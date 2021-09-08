package code.name.monkey.retromusic.cast

import androidx.core.net.toUri
import code.name.monkey.retromusic.cast.RetroWebServer.Companion.MIME_TYPE_AUDIO
import code.name.monkey.retromusic.cast.RetroWebServer.Companion.PART_COVER_ART
import code.name.monkey.retromusic.cast.RetroWebServer.Companion.PART_SONG
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.gms.cast.*
import com.google.android.gms.cast.MediaInfo.STREAM_TYPE_BUFFERED
import com.google.android.gms.cast.MediaMetadata.*
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.common.images.WebImage
import java.net.MalformedURLException
import java.net.URL

object CastHelper {

    private const val CAST_MUSIC_METADATA_ID = "metadata_id"
    private const val CAST_MUSIC_METADATA_ALBUM_ID = "metadata_album_id"
    private const val CAST_URL_PROTOCOL = "http"

    fun castSong(castSession: CastSession, song: Song) {
        try {
            val remoteMediaClient = castSession.remoteMediaClient
            val mediaLoadOptions = MediaLoadOptions.Builder().apply {
                setPlayPosition(0)
                setAutoplay(true)
            }.build()
            remoteMediaClient?.load(song.toMediaInfo()!!, mediaLoadOptions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun castQueue(castSession: CastSession, songs: List<Song>, position: Int, progress: Long) {
        try {
            val remoteMediaClient = castSession.remoteMediaClient
            remoteMediaClient?.queueLoad(
                songs.toMediaInfoList(),
                position,
                MediaStatus.REPEAT_MODE_REPEAT_OFF,
                progress,
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun List<Song>.toMediaInfoList(): Array<MediaQueueItem> {
        return map { MediaQueueItem.Builder(it.toMediaInfo()!!).build() }.toTypedArray()
    }

    private fun Song.toMediaInfo(): MediaInfo? {
        val song = this
        val baseUrl: URL
        try {
            baseUrl = URL(CAST_URL_PROTOCOL, RetroUtil.getIpAddress(true), SERVER_PORT, "")
        } catch (e: MalformedURLException) {
            return null
        }

        val songUrl = "$baseUrl/$PART_SONG?id=${song.id}"
        val albumArtUrl = "$baseUrl/$PART_COVER_ART?id=${song.albumId}"
        val musicMetadata = MediaMetadata(MEDIA_TYPE_MUSIC_TRACK).apply {
            putInt(CAST_MUSIC_METADATA_ID, song.id.toInt())
            putInt(CAST_MUSIC_METADATA_ALBUM_ID, song.albumId.toInt())
            putString(KEY_TITLE, song.title)
            putString(KEY_ARTIST, song.artistName)
            putString(KEY_ALBUM_TITLE, song.albumName)
            putInt(KEY_TRACK_NUMBER, song.trackNumber)
            addImage(WebImage(albumArtUrl.toUri()))
        }
        return MediaInfo.Builder(songUrl).apply {
            setStreamType(STREAM_TYPE_BUFFERED)
            setContentType(MIME_TYPE_AUDIO)
            setMetadata(musicMetadata)
            setStreamDuration(song.duration)
        }.build()

    }
}