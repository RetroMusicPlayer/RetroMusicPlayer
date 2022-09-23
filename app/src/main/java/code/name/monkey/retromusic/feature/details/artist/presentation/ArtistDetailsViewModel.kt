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
package code.name.monkey.retromusic.feature.details.artist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetails
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsAlbum
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsRepository
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsSong
import code.name.monkey.retromusic.feature.details.artist.presentation.entity.ArtistDetailsAlbumUi
import code.name.monkey.retromusic.feature.details.artist.presentation.entity.ArtistDetailsSongUi
import code.name.monkey.retromusic.feature.details.artist.presentation.entity.ArtistDetailsUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.stersh.retrosonic.core.extensions.mapItems

class ArtistDetailsViewModel(
    private val artistId: String,
    private val artistDetailsRepository: ArtistDetailsRepository
) : ViewModel() {
    private val _artistDetails = MutableStateFlow<ArtistDetailsUi?>(null)
    val artistDetails: Flow<ArtistDetailsUi>
        get() = _artistDetails.filterNotNull()

    private val _albums = MutableStateFlow<List<ArtistDetailsAlbumUi>>(emptyList())
    val albums: Flow<List<ArtistDetailsAlbumUi>>
        get() = _albums

    private val _songs = MutableStateFlow<List<ArtistDetailsSongUi>>(emptyList())
    val songs: Flow<List<ArtistDetailsSongUi>>
        get() = _songs

    init {
        viewModelScope.launch {
            artistDetailsRepository
                .getArtistDetails(artistId)
                .map { it.toPresentation() }
                .collect { _artistDetails.value = it }
        }
        viewModelScope.launch {
            artistDetailsRepository
                .getArtistAlbums(artistId)
                .mapItems { it.toPresentation() }
                .collect { _albums.value = it }
        }
        viewModelScope.launch {
            artistDetailsRepository
                .getArtistSongs(artistId)
                .mapItems { it.toPresentation() }
                .collect { _songs.value = it }
        }
    }

    private fun ArtistDetails.toPresentation(): ArtistDetailsUi {
        return ArtistDetailsUi(title, biography, coverArtUrl, albumCount, songCount, totalDuration)
    }

    private fun ArtistDetailsAlbum.toPresentation(): ArtistDetailsAlbumUi {
        return ArtistDetailsAlbumUi(id, title, artist, coverArtUrl, year)
    }

    private fun ArtistDetailsSong.toPresentation(): ArtistDetailsSongUi {
        return ArtistDetailsSongUi(id, title, artist, album, albumId, track, duration, year, coverArtUrl)
    }
}
