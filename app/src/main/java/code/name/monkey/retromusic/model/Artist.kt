package code.name.monkey.retromusic.model

import code.name.monkey.retromusic.util.MusicUtil
import java.util.*


class Artist {
    val albums: ArrayList<Album>?

    val id: Int
        get() = safeGetFirstAlbum().artistId

    val name: String
        get() {
            val name = safeGetFirstAlbum().artistName
            return if (MusicUtil.isArtistNameUnknown(name)) {
                UNKNOWN_ARTIST_DISPLAY_NAME
            } else name!!
        }

    val songCount: Int
        get() {
            var songCount = 0
            for (album in albums!!) {
                songCount += album.songCount
            }
            return songCount
        }

    val albumCount: Int
        get() = albums!!.size

    val songs: ArrayList<Song>
        get() {
            val songs = ArrayList<Song>()
            for (album in albums!!) {
                songs.addAll(album.songs!!)
            }
            return songs
        }

    constructor(albums: ArrayList<Album>) {
        this.albums = albums
    }

    constructor() {
        this.albums = ArrayList()
    }

    fun safeGetFirstAlbum(): Album {
        return if (albums!!.isEmpty()) Album() else albums[0]
    }

    companion object {
        const val UNKNOWN_ARTIST_DISPLAY_NAME = "Unknown Artist"
    }
}
