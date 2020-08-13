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

package code.name.monkey.retromusic.providers

import android.content.Context
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.loaders.*
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.model.smartplaylist.NotRecentlyPlayedPlaylist
import code.name.monkey.retromusic.network.LastFMService
import code.name.monkey.retromusic.network.model.LastFmAlbum
import code.name.monkey.retromusic.network.model.LastFmArtist
import code.name.monkey.retromusic.providers.interfaces.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val context: Context,
    private val lastFMService: LastFMService
) : Repository {

    override suspend fun allAlbums(): List<Album> = AlbumLoader.getAllAlbums(context)

    override suspend fun albumById(albumId: Int): Album = AlbumLoader.getAlbum(context, albumId)

    override suspend fun allArtists(): List<Artist> = ArtistLoader.getAllArtists(context)

    override suspend fun artistById(artistId: Int): Artist =
        ArtistLoader.getArtist(context, artistId)

    override suspend fun recentArtists(): List<Artist> =
        LastAddedSongsLoader.getLastAddedArtists(context)

    override suspend fun topArtists(): List<Artist> =
        TopAndRecentlyPlayedTracksLoader.getTopArtists(context)

    override suspend fun topAlbums(): List<Album> =
        TopAndRecentlyPlayedTracksLoader.getTopAlbums(context)

    override suspend fun recentAlbums(): List<Album> =
        LastAddedSongsLoader.getLastAddedAlbums(context)

    override suspend fun allPlaylists(): List<Playlist> = PlaylistLoader.getAllPlaylists(context)

    override suspend fun allGenres(): List<Genre> = GenreLoader.getAllGenres(context)

    override suspend fun allSongs(): List<Song> = SongLoader.getAllSongs(context)


    override suspend fun search(query: String?): MutableList<Any> =
        SearchLoader.searchAll(context, query)

    override suspend fun getPlaylistSongs(playlist: Playlist): ArrayList<Song> {
        return if (playlist is AbsCustomPlaylist) {
            playlist.getSongs(context)
        } else {
            PlaylistSongsLoader.getPlaylistSongList(context, playlist.id)
        }
    }

    override suspend fun getGenre(genreId: Int): ArrayList<Song> =
        GenreLoader.getSongs(context, genreId)


    override suspend fun artistInfo(
        name: String,
        lang: String?,
        cache: String?
    ): LastFmArtist = lastFMService.artistInfo(name, lang, cache)


    override suspend fun albumInfo(
        artist: String,
        album: String
    ): LastFmAlbum = lastFMService.albumInfo(artist, album)

    @ExperimentalCoroutinesApi
    override suspend fun homeSectionsFlow(): Flow<Result<List<Home>>> {
        val homes = MutableStateFlow<Result<List<Home>>>(value = Result.Loading)
        println("homeSections:Loading")
        val homeSections = mutableListOf<Home>()
        val sections = listOf(
            topArtistsHome(),
            topAlbumsHome(),
            recentArtistsHome(),
            recentAlbumsHome(),
            suggestionsHome(),
            favoritePlaylistHome(),
            genresHome()
        )
        for (section in sections) {
            if (section.arrayList.isNotEmpty()) {
                println("${section.homeSection} -> ${section.arrayList.size}")
                homeSections.add(section)
            }
        }
        if (homeSections.isEmpty()) {
            homes.value = Result.Error
        } else {
            homes.value = Result.Success(homeSections)
        }
        return homes
    }

    override suspend fun homeSections(): List<Home> {
        val homeSections = mutableListOf<Home>()
        val sections = listOf(
            topArtistsHome(),
            topAlbumsHome(),
            recentArtistsHome(),
            recentAlbumsHome(),
            suggestionsHome(),
            favoritePlaylistHome(),
            genresHome()
        )
        for (section in sections) {
            if (section.arrayList.isNotEmpty()) {
                println("${section.homeSection} -> ${section.arrayList.size}")
                homeSections.add(section)
            }
        }
        return homeSections
    }

    suspend fun playlists(): Home {
        val playlist = PlaylistLoader.getAllPlaylists(context)
        return Home(playlist, TOP_ALBUMS)
    }

    override suspend fun suggestionsHome(): Home {
        val songs =
            NotRecentlyPlayedPlaylist(context).getSongs(context).shuffled().takeUnless {
                it.size > 9
            }?.take(9) ?: emptyList<Song>()
        return Home(songs, SUGGESTIONS)
    }

    override suspend fun genresHome(): Home {
        val genres = GenreLoader.getAllGenres(context).shuffled()
        return Home(genres, GENRES)
    }


    override suspend fun recentArtistsHome(): Home {
        val artists = LastAddedSongsLoader.getLastAddedArtists(context).take(5)
        return Home(artists, RECENT_ARTISTS)
    }

    override suspend fun recentAlbumsHome(): Home {
        val albums = LastAddedSongsLoader.getLastAddedAlbums(context).take(5)
        return Home(albums, RECENT_ALBUMS)
    }

    override suspend fun topAlbumsHome(): Home {
        val albums = TopAndRecentlyPlayedTracksLoader.getTopAlbums(context).take(5)
        return Home(albums, TOP_ALBUMS)
    }

    override suspend fun topArtistsHome(): Home {
        val artists = TopAndRecentlyPlayedTracksLoader.getTopArtists(context).take(5)
        return Home(artists, TOP_ARTISTS)
    }

    override suspend fun favoritePlaylistHome(): Home {
        val playlists = PlaylistLoader.getFavoritePlaylist(context).take(5)
        val songs = if (playlists.isNotEmpty())
            PlaylistSongsLoader.getPlaylistSongList(context, playlists[0])
        else emptyList<Song>()

        return Home(songs, FAVOURITES)
    }

    override fun songsFlow(): Flow<Result<List<Song>>> = flow {
        emit(Result.Loading)
        val data = SongLoader.getAllSongs(context)
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun albumsFlow(): Flow<Result<List<Album>>> = flow {
        emit(Result.Loading)
        val data = AlbumLoader.getAllAlbums(context)
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun artistsFlow(): Flow<Result<List<Artist>>> = flow {
        emit(Result.Loading)
        val data = ArtistLoader.getAllArtists(context)
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun playlistsFlow(): Flow<Result<List<Playlist>>> = flow {
        emit(Result.Loading)
        val data = PlaylistLoader.getAllPlaylists(context)
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }

    override fun genresFlow(): Flow<Result<List<Genre>>> = flow {
        emit(Result.Loading)
        val data = GenreLoader.getAllGenres(context)
        if (data.isEmpty()) {
            emit(Result.Error)
        } else {
            emit(Result.Success(data))
        }
    }
}