/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.feature.details.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.feature.details.album.domain.AlbumDetails
import code.name.monkey.retromusic.feature.details.album.domain.AlbumDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AlbumDetailsViewModel(
    private val albumDetailsRepository: AlbumDetailsRepository,
    private val albumId: String
) : ViewModel() {
    private val _albumDetails = MutableStateFlow<AlbumDetailsUi?>(null)
    val albumDetails: Flow<AlbumDetailsUi>
        get() = _albumDetails.filterNotNull()

    init {
        viewModelScope.launch {
            albumDetailsRepository
                .getAlbumDetails(albumId)
                .map { it.toPresentation() }
                .collect {
                    _albumDetails.value = it
                }
        }
    }

    private fun AlbumDetails.toPresentation(): AlbumDetailsUi {
        return AlbumDetailsUi(
            id = id,
            title = title,
            artist = artist.toPresentation(),
            year = year,
            coverArtUrl = coverArtUrl,
            songs = songs.map { it.toPresentation() }
        )
    }

    private fun AlbumDetails.Artist.toPresentation(): AlbumDetailsUi.ArtistUi {
        return AlbumDetailsUi.ArtistUi(id, title, coverArtUrl)
    }

    private fun AlbumDetails.Song.toPresentation(): AlbumDetailsUi.SongUi {
        return AlbumDetailsUi.SongUi(id, albumId, coverArtUrl, title, artist, album, year)
    }

//    fun getArtist(artistId: Long): LiveData<Artist> = liveData(IO) {
//        val artist = repository.artistById(artistId)
//        emit(artist)
//    }
//
//    fun getAlbumArtist(artistName: String): LiveData<Artist> = liveData(IO) {
//        val artist = repository.albumArtistByName(artistName)
//        emit(artist)
//    }

//    fun getAlbumInfo(album: Album): LiveData<Result<LastFmAlbum>> = liveData(IO) {
//        emit(Result.Loading)
//        emit(repository.albumInfo(album.artistName, album.title))
//    }

//    fun getMoreAlbums(artist: Artist): LiveData<List<Album>> = liveData(IO) {
//        artist.albums.filter { item -> item.id != albumId }.let { albums ->
//            if (albums.isNotEmpty()) emit(albums)
//        }
//    }
}
