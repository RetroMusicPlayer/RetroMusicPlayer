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

import code.name.monkey.retromusic.Result
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.rest.model.LastFmArtist
import io.reactivex.Observable

/**
 * Created by hemanths on 11/08/17.
 */

interface Repository {

    suspend fun allAlbums(): Result<ArrayList<Album>>

    suspend fun allSongs(): Result<ArrayList<Song>>

    suspend fun allArtists(): Result<ArrayList<Artist>>

    suspend fun allPlaylists(): Result<ArrayList<Playlist>>

    suspend fun allGenres(): Result<ArrayList<Genre>>

    suspend fun search(query: String?): Result<MutableList<Any>>

    suspend fun getPlaylistSongs(playlist: Playlist): Result<ArrayList<Song>>

    suspend fun getGenre(genreId: Int): Result<ArrayList<Song>>

    suspend fun recentArtists(): Result<Home>

    suspend fun topArtists(): Result<Home>

    suspend fun topAlbums(): Result<Home>

    suspend fun recentAlbums(): Result<Home>

    suspend fun favoritePlaylist(): Result<Home>

    suspend fun artistInfo(name: String, lang: String?, cache: String?): Result<LastFmArtist>

    suspend fun artistById(artistId: Int): Result<Artist>

    val allSongsFlowable: Observable<ArrayList<Song>>

    val suggestionSongsFlowable: Observable<ArrayList<Song>>

    val allAlbumsFlowable: Observable<ArrayList<Album>>

    val recentAlbumsFlowable: Observable<ArrayList<Album>>

    val topAlbumsFlowable: Observable<ArrayList<Album>>

    val allArtistsFlowable: Observable<ArrayList<Artist>>

    val recentArtistsFlowable: Observable<ArrayList<Artist>>

    val topArtistsFlowable: Observable<ArrayList<Artist>>

    val allPlaylistsFlowable: Observable<ArrayList<Playlist>>

    val allGenresFlowable: Observable<ArrayList<Genre>>

    fun getSongFlowable(id: Int): Observable<Song>

    fun getSong(id: Int): Song

    fun getAlbumFlowable(albumId: Int): Observable<Album>

    fun getAlbum(albumId: Int): Album

    fun getArtistByIdFlowable(artistId: Int): Observable<Artist>

    fun getArtistById(artistId: Long): Artist

    fun getPlaylistSongsFlowable(playlist: Playlist): Observable<ArrayList<Song>>

    fun getGenreFlowable(genreId: Int): Observable<ArrayList<Song>>


    val favoritePlaylistFlowable: Observable<ArrayList<Playlist>>


}