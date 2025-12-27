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

import android.provider.MediaStore.Audio.AudioColumns
import code.name.monkey.retromusic.ALBUM_ARTIST
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.ArtistSeparator
import code.name.monkey.retromusic.util.PreferenceUtil
import java.text.Collator

interface ArtistRepository {
    fun artists(): List<Artist>

    fun albumArtists(): List<Artist>

    fun albumArtists(query: String): List<Artist>

    fun artists(query: String): List<Artist>

    fun artist(artistId: Long): Artist

    fun albumArtist(artistName: String): Artist
    
    fun artistByName(name: String): Artist
}

class RealArtistRepository(
    private val songRepository: SongRepository, 
    private val albumRepository: AlbumRepository
) : ArtistRepository {

    private fun getSongLoaderSortOrder(): String {
        return PreferenceUtil.artistSortOrder + ", " +
                PreferenceUtil.artistAlbumSortOrder + ", " +
                PreferenceUtil.artistSongSortOrder
    }

    override fun artist(artistId: Long): Artist {
        if (artistId == Artist.VARIOUS_ARTISTS_ID) {
            val songs = songRepository.songs()
            val albums = albumRepository.splitIntoAlbums(songs)
                .filter { it.albumArtist == Artist.VARIOUS_ARTISTS_DISPLAY_NAME }
            return Artist(Artist.VARIOUS_ARTISTS_ID, albums)
        }

        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                AudioColumns.ARTIST_ID + "=?",
                arrayOf(artistId.toString()),
                getSongLoaderSortOrder()
            )
        )
        return Artist(artistId, albumRepository.splitIntoAlbums(songs))
    }
    
    override fun artistByName(name: String): Artist {
        val allSongs = songRepository.songs()

        val songsForArtist = allSongs.filter { song ->
            val sourceName = if (PreferenceUtil.albumArtistsOnly) song.albumArtist else song.artistName
            ArtistSeparator.split(sourceName).any { it.equals(name, ignoreCase = true) }
        }

        val albums = albumRepository.splitIntoAlbums(songsForArtist)
        return Artist(name.hashCode().toLong(), albums, name = name)
    }

    override fun albumArtist(artistName: String): Artist {
        if (artistName == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
            val songs = songRepository.songs()
            val albums = albumRepository.splitIntoAlbums(songs)
                .filter { it.albumArtist == Artist.VARIOUS_ARTISTS_DISPLAY_NAME }
            return Artist(Artist.VARIOUS_ARTISTS_ID, albums, true)
        }

        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                "album_artist" + "=?",
                arrayOf(artistName),
                getSongLoaderSortOrder()
            )
        )
        return Artist(artistName.hashCode().toLong(), albumRepository.splitIntoAlbums(songs), true, name = artistName)
    }

    override fun artists(): List<Artist> {
        val songs = songRepository.songs()
        val artistToSongsMap = mutableMapOf<String, MutableList<Song>>()

        songs.forEach { song ->
            ArtistSeparator.split(song.artistName).forEach { name ->
                val trimmedName = name.trim()
                if (trimmedName.isNotEmpty()) {
                    artistToSongsMap.getOrPut(trimmedName) { mutableListOf() }.add(song)
                }
            }
        }

        val processedArtists = artistToSongsMap.map { (artistName, songsForArtist) ->
            val albums = albumRepository.splitIntoAlbums(songsForArtist)
            Artist(artistName.hashCode().toLong(), albums, name = artistName)
        }

        return sortArtists(processedArtists)
    }

    override fun albumArtists(): List<Artist> {
        val songs = songRepository.songs()
        val artistToSongsMap = mutableMapOf<String, MutableList<Song>>()

        songs.forEach { song ->
            ArtistSeparator.split(song.albumArtist).forEach { name ->
                val trimmedName = name.trim()
                if (trimmedName.isNotEmpty()) {
                    artistToSongsMap.getOrPut(trimmedName) { mutableListOf() }.add(song)
                }
            }
        }

        val processedArtists = artistToSongsMap.map { (artistName, songsForArtist) ->
            val albums = albumRepository.splitIntoAlbums(songsForArtist)
            Artist(artistName.hashCode().toLong(), albums, isAlbumArtist = true, name = artistName)
        }
        
        return sortArtists(processedArtists)
    }

    override fun albumArtists(query: String): List<Artist> {
        return albumArtists().filter { it.name.contains(query, ignoreCase = true) }
    }

    override fun artists(query: String): List<Artist> {
        return artists().filter { it.name.contains(query, ignoreCase = true) }
    }

    private fun sortArtists(artists: List<Artist>): List<Artist> {
        val collator = Collator.getInstance()
        return when (PreferenceUtil.artistSortOrder) {
            SortOrder.ArtistSortOrder.ARTIST_A_Z -> {
                artists.sortedWith { a1, a2 -> collator.compare(a1.name, a2.name) }
            }
            SortOrder.ArtistSortOrder.ARTIST_Z_A -> {
                artists.sortedWith { a1, a2 -> collator.compare(a2.name, a1.name) }
            }
            else -> artists
        }
    }
}