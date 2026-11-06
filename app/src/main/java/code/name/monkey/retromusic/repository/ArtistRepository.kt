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
import code.name.monkey.retromusic.util.ArtistSeparator
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.logD
import java.text.Collator

interface ArtistRepository {
    fun artists(): List<Artist>

    fun artists(albums: List<Album>, albumArtistsOnly: Boolean): List<Artist>

    fun albumArtists(): List<Artist>

    fun albumArtists(query: String): List<Artist>

    fun artists(query: String): List<Artist>

    fun artist(artistId: Long): Artist

    fun albumArtist(artistName: String): Artist

    fun artistByName(name: String): Artist
}

class RealArtistRepository(
    private val songRepository: RealSongRepository,
    private val albumRepository: RealAlbumRepository
) : ArtistRepository {

    private fun getSongLoaderSortOrder(): String {
        return PreferenceUtil.artistSortOrder + ", " +
                PreferenceUtil.artistAlbumSortOrder + ", " +
                PreferenceUtil.artistSongSortOrder
    }

    override fun artist(artistId: Long): Artist {
        if (artistId == Artist.VARIOUS_ARTISTS_ID) {
            // Get Various Artists
            val songs = songRepository.songs(
                songRepository.makeSongCursor(
                    null,
                    null,
                    getSongLoaderSortOrder()
                )
            )
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

    override fun albumArtist(artistName: String): Artist {
        if (artistName == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
            // Get Various Artists
            val songs = songRepository.songs(
                songRepository.makeSongCursor(
                    null,
                    null,
                    getSongLoaderSortOrder()
                )
            )
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
        return Artist(artistName, albumRepository.splitIntoAlbums(songs), true)
    }

    /**
     * Finds an artist by name, searching across all songs and handling multi-artist metadata.
     *
     * This method uses ArtistSeparator to split artist names and find songs where the
     * specified artist appears (even if they're one of multiple artists on a song).
     *
     * @param name The artist name to search for (case-insensitive)
     * @return Artist object with all matching songs grouped into albums,
     *         or empty Artist if no songs found
     */
    override fun artistByName(name: String): Artist {
        logD("artistByName: Searching for artist '$name'")

        // Handle special case for Various Artists
        if (name.equals(Artist.VARIOUS_ARTISTS_DISPLAY_NAME, ignoreCase = true)) {
            logD("artistByName: Detected Various Artists")
            return artist(Artist.VARIOUS_ARTISTS_ID)
        }

        // Get all songs from the media store
        val allSongs = songRepository.songs(
            songRepository.makeSongCursor(
                null,
                null,
                getSongLoaderSortOrder()
            )
        )

        // Filter songs where the artist name appears
        // Uses ArtistSeparator to handle multi-artist metadata like "Artist1 / Artist2"
        val songsForArtist = allSongs.filter { song ->
            val artistNames = ArtistSeparator.split(song.artistName)
            artistNames.any { it.equals(name, ignoreCase = true) }
        }

        logD("artistByName: Found ${songsForArtist.size} songs for '$name'")

        // If no songs found, return empty artist
        if (songsForArtist.isEmpty()) {
            logD("artistByName: No songs found for '$name', returning empty artist")
            return Artist.empty
        }

        // Group songs into albums
        val albums = albumRepository.splitIntoAlbums(songsForArtist)
        logD("artistByName: Grouped into ${albums.size} albums")

        // Generate a stable ID from the artist name
        // Using hashCode ensures the same name always gets the same ID
        val artistId = name.hashCode().toLong()

        return Artist(artistId, albums)
    }

    override fun artists(): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                null, null,
                getSongLoaderSortOrder()
            )
        )
        val artists = splitIntoArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    override fun artists(albums: List<Album>, albumArtistsOnly: Boolean): List<Artist> {
        val artists = if (albumArtistsOnly) {
            splitIntoAlbumArtists(albums)
        } else {
            splitIntoArtists(albums)
        }
        return sortArtists(artists)
    }

    override fun albumArtists(): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                null,
                null,
                "lower($ALBUM_ARTIST)" +
                        if (PreferenceUtil.artistSortOrder == SortOrder.ArtistSortOrder.ARTIST_A_Z) "" else " DESC"
            )
        )
        val artists = splitIntoAlbumArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    override fun albumArtists(query: String): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                "album_artist" + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        val artists = splitIntoAlbumArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    override fun artists(query: String): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                AudioColumns.ARTIST + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        val artists = splitIntoArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }


    private fun splitIntoAlbumArtists(albums: List<Album>): List<Artist> {
        return albums.groupBy { it.albumArtist }
            .filter {
                !it.key.isNullOrEmpty()
            }
            .map {
                val currentAlbums = it.value
                if (currentAlbums.isNotEmpty()) {
                    if (currentAlbums[0].albumArtist == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
                        Artist(Artist.VARIOUS_ARTISTS_ID, currentAlbums, true)
                    } else {
                        Artist(currentAlbums[0].artistId, currentAlbums, true)
                    }
                } else {
                    Artist.empty
                }
            }
    }


    fun splitIntoArtists(albums: List<Album>): List<Artist> {
        return albums.groupBy { it.artistId }
            .map { Artist(it.key, it.value) }
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
