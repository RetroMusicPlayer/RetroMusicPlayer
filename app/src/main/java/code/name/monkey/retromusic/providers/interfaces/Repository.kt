/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.providers.interfaces

import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.rest.model.LastFmAlbum
import code.name.monkey.retromusic.rest.model.LastFmArtist

/**
 * Created by hemanths on 11/08/17.
 */

interface Repository {

    suspend fun allAlbums(): List<Album>

    suspend fun albumById(albumId: Int): Album

    suspend fun allSongs(): List<Song>

    suspend fun allArtists(): List<Artist>

    suspend fun allPlaylists(): List<Playlist>

    suspend fun allGenres(): List<Genre>

    suspend fun search(query: String?): MutableList<Any>

    suspend fun getPlaylistSongs(playlist: Playlist): ArrayList<Song>

    suspend fun getGenre(genreId: Int): ArrayList<Song>

    suspend fun recentArtists(): Home?

    suspend fun topArtists(): Home?

    suspend fun topAlbums(): Home?

    suspend fun recentAlbums(): Home?

    suspend fun favoritePlaylist(): Home?

    suspend fun artistInfo(name: String, lang: String?, cache: String?): LastFmArtist

    suspend fun albumInfo(artist: String, album: String): LastFmAlbum

    suspend fun artistById(artistId: Int): Artist
}