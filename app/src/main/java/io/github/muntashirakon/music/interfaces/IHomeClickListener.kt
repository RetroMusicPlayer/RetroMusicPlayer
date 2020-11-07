package io.github.muntashirakon.music.interfaces

import io.github.muntashirakon.music.model.Album
import io.github.muntashirakon.music.model.Artist
import io.github.muntashirakon.music.model.Genre

interface IHomeClickListener {
    fun onAlbumClick(album: Album)

    fun onArtistClick(artist: Artist)

    fun onGenreClick(genre: Genre)
}