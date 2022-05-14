package io.github.muntashirakon.music.extensions

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat.QueueItem
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.util.MusicUtil

val Song.uri get() = MusicUtil.getSongFileUri(songId = id)


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
