package code.name.monkey.retromusic.extensions

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat.QueueItem
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil

val Song.uri get() = MusicUtil.getSongFileUri(songId = id)

val Song.albumArtUri get() = MusicUtil.getMediaStoreAlbumCoverUri(albumId)

fun ArrayList<Song>.toMediaSessionQueue(): List<QueueItem> {
    return map { song ->
        val mediaDescription = MediaDescriptionCompat.Builder()
            .setMediaId(song.id.toString())
            .setTitle(song.title)
            .setSubtitle(song.artistName)
            .setIconUri(song.albumArtUri)
            .build()
        QueueItem(mediaDescription, song.hashCode().toLong())
    }
}
