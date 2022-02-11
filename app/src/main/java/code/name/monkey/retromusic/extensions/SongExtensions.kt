package code.name.monkey.retromusic.extensions

import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil

val Song.uri get() = MusicUtil.getSongFileUri(songId = id)