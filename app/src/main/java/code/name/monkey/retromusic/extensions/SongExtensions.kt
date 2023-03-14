package code.name.monkey.retromusic.extensions

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat.QueueItem
import code.name.monkey.retromusic.model.Song

val Song.uri get() = code.name.monkey.retromusic.util.MusicUtil.getSongFileUri(songId = id)


fun ArrayList<Song>.toMediaSessionQueue(): List<QueueItem> {
    return map {
        val mediaDescription = MediaDescriptionCompat.Builder()
            .setMediaId(it.id.toString())
            .setTitle(it.title)
            .setSubtitle(it.artistName)
            .build()
        QueueItem(mediaDescription, it.hashCode().toLong())
    }
}
