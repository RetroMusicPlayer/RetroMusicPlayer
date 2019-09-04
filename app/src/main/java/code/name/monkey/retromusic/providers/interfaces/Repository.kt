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
import code.name.monkey.retromusic.rest.model.LastFmArtist
import io.reactivex.Observable

/**
 * Created by hemanths on 11/08/17.
 */

interface Repository {

    val allSongsFlowable: Observable<ArrayList<Song>>

    fun allSongs(): ArrayList<Song>

    val suggestionSongsFlowable: Observable<ArrayList<Song>>

    val allAlbumsFlowable: Observable<ArrayList<Album>>

    fun allAlbums(): ArrayList<Album>

    val recentAlbumsFlowable: Observable<ArrayList<Album>>

    fun recentAlbums(): ArrayList<Album>

    val topAlbumsFlowable: Observable<ArrayList<Album>>

    fun topAlbums(): ArrayList<Album>

    val allArtistsFlowable: Observable<ArrayList<Artist>>

    fun allArtists(): ArrayList<Artist>

    val recentArtistsFlowable: Observable<ArrayList<Artist>>

    fun recentArtists(): ArrayList<Artist>

    val topArtistsFlowable: Observable<ArrayList<Artist>>

    fun topArtists(): ArrayList<Artist>

    val allPlaylistsFlowable: Observable<ArrayList<Playlist>>

    fun allPlaylists(): ArrayList<Playlist>

    val allGenresFlowable: Observable<ArrayList<Genre>>

    fun allGenres(): ArrayList<Genre>

    fun getSongFlowable(id: Int): Observable<Song>

    fun getSong(id: Int): Song

    fun getAlbumFlowable(albumId: Int): Observable<Album>

    fun getAlbum(albumId: Int): Album

    fun getArtistByIdFlowable(artistId: Int): Observable<Artist>

    fun getArtistById(artistId: Long): Artist

    fun search(query: String?): MutableList<Any>

    fun getPlaylistSongsFlowable(playlist: Playlist): Observable<ArrayList<Song>>

    fun getPlaylistSongs(playlist: Playlist): ArrayList<Song>

    fun getGenreFlowable(genreId: Int): Observable<ArrayList<Song>>

    fun getGenre(genreId: Int): ArrayList<Song>

    val favoritePlaylistFlowable: Observable<ArrayList<Playlist>>

    val favoritePlaylist: ArrayList<Playlist>

    fun artistInfoFloable(name: String,
                          lang: String?,
                          cache: String?): Observable<LastFmArtist>
}