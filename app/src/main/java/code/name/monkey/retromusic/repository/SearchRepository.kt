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

package code.name.monkey.retromusic.repository

import android.content.Context
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.fragments.search.Filter
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Song

class RealSearchRepository(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val roomRepository: RoomRepository,
    private val genreRepository: GenreRepository,
) {
    suspend fun searchAll(context: Context, query: String?, filter: Filter): MutableList<Any> {
        val results = mutableListOf<Any>()
        if (query.isNullOrEmpty()) return results
        query.let { searchString ->

            /** Songs **/
            val songs: List<Song> = if (filter == Filter.SONGS || filter == Filter.NO_FILTER) {
                songRepository.songs(searchString)
            } else {
                emptyList()
            }
            if (songs.isNotEmpty()) {
                results.add(context.resources.getString(R.string.songs))
                results.addAll(songs)
            }

            /** Artists **/
            val artists: List<Artist> =
                if (filter == Filter.ARTISTS || filter == Filter.NO_FILTER) {
                    artistRepository.artists(searchString)
                } else {
                    emptyList()
                }
            if (artists.isNotEmpty()) {
                results.add(context.resources.getString(R.string.artists))
                results.addAll(artists)
            }

            /** Albums **/
            val albums: List<Album> = if (filter == Filter.ALBUMS || filter == Filter.NO_FILTER) {
                albumRepository.albums(searchString)
            } else {
                emptyList()
            }
            if (albums.isNotEmpty()) {
                results.add(context.resources.getString(R.string.albums))
                results.addAll(albums)
            }

            /** Album-Artists **/
            val albumArtists: List<Artist> =
                if (filter == Filter.ALBUM_ARTISTS || filter == Filter.NO_FILTER) {
                    artistRepository.albumArtists(searchString)
                } else {
                    emptyList()
                }
            if (albumArtists.isNotEmpty()) {
                results.add(context.resources.getString(R.string.album_artist))
                results.addAll(albumArtists)
            }

            /** Genres **/
            val genres: List<Genre> = if (filter == Filter.GENRES || filter == Filter.NO_FILTER) {
                genreRepository.genres(query)
            } else {
                emptyList()
            }
            if (genres.isNotEmpty()) {
                results.add(context.resources.getString(R.string.genres))
                results.addAll(genres)
            }

            /** Playlists **/
            val playlist: List<PlaylistWithSongs> =
                if (filter == Filter.PLAYLISTS || filter == Filter.NO_FILTER) {
                    roomRepository.playlistWithSongs().filter { playlist ->
                        playlist.playlistEntity.playlistName.lowercase().contains(searchString.lowercase())
                    }
                } else {
                    emptyList()
                }

            if (playlist.isNotEmpty()) {
                results.add(context.getString(R.string.playlists))
                results.addAll(playlist)
            }
        }
        return results
    }
}
