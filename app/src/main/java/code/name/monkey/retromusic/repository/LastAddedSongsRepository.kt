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

import android.database.Cursor
import android.provider.MediaStore
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.ArtistSeparator
import code.name.monkey.retromusic.util.PreferenceUtil

/**
 * Created by hemanths on 16/08/17.
 */
interface LastAddedRepository {
    fun recentSongs(): List<Song>

    fun recentAlbums(): List<Album>

    fun recentArtists(): List<Artist>
}

class RealLastAddedRepository(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) : LastAddedRepository {
    override fun recentSongs(): List<Song> {
        return songRepository.songs(makeLastAddedCursor())
    }

    override fun recentAlbums(): List<Album> {
        return albumRepository.splitIntoAlbums(recentSongs())
    }

    override fun recentArtists(): List<Artist> {
        val recentSongs = recentSongs()
        val artistNames = mutableSetOf<String>()

        recentSongs.forEach { song ->
            val sourceName = if (PreferenceUtil.albumArtistsOnly) song.albumArtist else song.artistName
            ArtistSeparator.split(sourceName).forEach { name ->
                val trimmedName = name.trim()
                if (trimmedName.isNotEmpty()) {
                    artistNames.add(trimmedName)
                }
            }
        }
        
        val allArtists = if (PreferenceUtil.albumArtistsOnly) artistRepository.albumArtists() else artistRepository.artists()
        return allArtists.filter { artist -> artistNames.contains(artist.name) }
    }

    private fun makeLastAddedCursor(): Cursor? {
        val cutoff = PreferenceUtil.lastAddedCutoff
        return songRepository.makeSongCursor(
            MediaStore.Audio.Media.DATE_ADDED + ">?",
            arrayOf(cutoff.toString()),
            MediaStore.Audio.Media.DATE_ADDED + " DESC"
        )
    }
}