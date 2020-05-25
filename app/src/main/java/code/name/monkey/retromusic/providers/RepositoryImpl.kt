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
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.loaders.*
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.providers.interfaces.Repository
import code.name.monkey.retromusic.rest.LastFmClient
import code.name.monkey.retromusic.rest.model.LastFmAlbum
import code.name.monkey.retromusic.rest.model.LastFmArtist
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val context: Context) : Repository {

    override suspend fun allAlbums(): List<Album> = AlbumLoader.getAllAlbums(context)

    override suspend fun allArtists(): List<Artist> = ArtistLoader.getAllArtists(context)

    override suspend fun allPlaylists(): List<Playlist> = PlaylistLoader.getAllPlaylists(context)

    override suspend fun allGenres(): List<Genre> = GenreLoader.getAllGenres(context)

    override suspend fun allSongs(): List<Song> = SongLoader.getAllSongs(context)

    override suspend fun albumById(albumId: Int): Album = AlbumLoader.getAlbum(context, albumId)

    override suspend fun artistById(artistId: Int): Artist =
        ArtistLoader.getArtist(context, artistId)

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

    override suspend fun recentArtists(): Home? {
        val artists = LastAddedSongsLoader.getLastAddedArtists(context)
        return if (artists.isNotEmpty()) Home(
            0,
            R.string.recent_artists,
            artists,
            HomeAdapter.RECENT_ARTISTS,
            R.drawable.ic_artist_white_24dp
        ) else null
    }

    override suspend fun recentAlbums(): Home? {
        val albums = LastAddedSongsLoader.getLastAddedAlbums(context)
        return if (albums.isNotEmpty()) {
            Home(
                1,
                R.string.recent_albums,
                albums,
                HomeAdapter.RECENT_ALBUMS,
                R.drawable.ic_album_white_24dp
            )
        } else null
    }

    override suspend fun topAlbums(): Home? {
        val albums = TopAndRecentlyPlayedTracksLoader.getTopAlbums(context)
        return if (albums.isNotEmpty()) {
            Home(
                3,
                R.string.top_albums,
                albums,
                HomeAdapter.TOP_ALBUMS,
                R.drawable.ic_album_white_24dp
            )
        } else null
    }

    override suspend fun topArtists(): Home? {

        val artists = TopAndRecentlyPlayedTracksLoader.getTopArtists(context)
        return if (artists.isNotEmpty()) {
            Home(
                2,
                R.string.top_artists,
                artists,
                HomeAdapter.TOP_ARTISTS,
                R.drawable.ic_artist_white_24dp
            )
        } else null

    }

    override suspend fun favoritePlaylist(): Home? {
        val playlists = PlaylistLoader.getFavoritePlaylist(context)
        return if (playlists.isNotEmpty()) {
            Home(
                4,
                R.string.favorites,
                playlists,
                HomeAdapter.PLAYLISTS,
                R.drawable.ic_favorite_white_24dp
            )
        } else null
    }

    override suspend fun artistInfo(
        name: String,
        lang: String?,
        cache: String?
    ): LastFmArtist = LastFmClient.getApiService().artistInfo(name, lang, cache)


    override suspend fun albumInfo(
        artist: String,
        album: String
    ): LastFmAlbum = LastFmClient.getApiService().albumInfo(artist, album)

}