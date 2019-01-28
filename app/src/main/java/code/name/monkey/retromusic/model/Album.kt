package code.name.monkey.models

import java.util.*


class Album {
    val songs: ArrayList<code.name.monkey.models.Song>?

    val id: Int
        get() = safeGetFirstSong().albumId

    val title: String?
        get() = safeGetFirstSong().albumName

    val artistId: Int
        get() = safeGetFirstSong().artistId

    val artistName: String?
        get() = safeGetFirstSong().artistName

    val year: Int
        get() = safeGetFirstSong().year

    val dateModified: Long
        get() = safeGetFirstSong().dateModified

    val songCount: Int
        get() = songs!!.size

    constructor(songs: ArrayList<code.name.monkey.models.Song>) {
        this.songs = songs
    }

    constructor() {
        this.songs = ArrayList()
    }

    fun safeGetFirstSong(): code.name.monkey.models.Song {
        return if (songs!!.isEmpty()) code.name.monkey.models.Song.EMPTY_SONG else songs[0]
    }
}
