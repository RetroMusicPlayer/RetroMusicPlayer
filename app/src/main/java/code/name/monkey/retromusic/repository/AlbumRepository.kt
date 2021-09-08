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
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil


/**
 * Created by hemanths on 11/08/17.
 */
interface AlbumRepository {
    fun albums(): List<Album>

    fun albums(query: String): List<Album>

    fun album(albumId: Long): Album
}

class RealAlbumRepository(private val songRepository: RealSongRepository) :
    AlbumRepository {

    override fun albums(): List<Album> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                null,
                null,
                getSongLoaderSortOrder()
            )
        )
        return splitIntoAlbums(songs)
    }

    override fun albums(query: String): List<Album> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                AudioColumns.ALBUM + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        return splitIntoAlbums(songs)
    }

    override fun album(albumId: Long): Album {
        val cursor = songRepository.makeSongCursor(
            AudioColumns.ALBUM_ID + "=?",
            arrayOf(albumId.toString()),
            getSongLoaderSortOrder()
        )
        val songs = songRepository.songs(cursor)
        val album = Album(albumId, songs)
        sortAlbumSongs(album)
        return album
    }

    // We don't need sorted list of songs (with sortAlbumSongs())
    // cuz we are just displaying Albums(Cover Arts) anyway and not songs
    fun splitIntoAlbums(
        songs: List<Song>
    ): List<Album> {
        return if (PreferenceUtil.albumSortOrder != SortOrder.AlbumSortOrder.ALBUM_NUMBER_OF_SONGS) songs.groupBy { it.albumId }
            .map { Album(it.key, it.value) }
        // We can't sort Album with the help of MediaStore so a hack
        else songs.groupBy { it.albumId }.map { Album(it.key, it.value) }
            .sortedByDescending { it.songCount }
    }

    private fun sortAlbumSongs(album: Album): Album {
        val songs = when (PreferenceUtil.albumDetailSongSortOrder) {
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST -> album.songs.sortedWith { o1, o2 ->
                o1.trackNumber.compareTo(o2.trackNumber)
            }
            SortOrder.AlbumSongSortOrder.SONG_A_Z -> album.songs.sortedWith { o1, o2 ->
                o1.title.compareTo(o2.title)
            }
            SortOrder.AlbumSongSortOrder.SONG_Z_A -> album.songs.sortedWith { o1, o2 ->
                o2.title.compareTo(o1.title)
            }
            SortOrder.AlbumSongSortOrder.SONG_DURATION -> album.songs.sortedWith { o1, o2 ->
                o1.duration.compareTo(o2.duration)
            }
            else -> throw IllegalArgumentException("invalid ${PreferenceUtil.albumDetailSongSortOrder}")
        }
        return album.copy(songs = songs)
    }

    private fun getSongLoaderSortOrder(): String {
        var albumSortOrder = PreferenceUtil.albumSortOrder
        if (albumSortOrder == SortOrder.AlbumSortOrder.ALBUM_NUMBER_OF_SONGS)
            albumSortOrder = SortOrder.AlbumSortOrder.ALBUM_A_Z
        return albumSortOrder + ", " +
                PreferenceUtil.albumSongSortOrder
    }
}
