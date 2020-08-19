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

package io.github.muntashirakon.music.repository

import android.provider.MediaStore.Audio.AudioColumns
import io.github.muntashirakon.music.helper.SortOrder
import io.github.muntashirakon.music.model.Album
import io.github.muntashirakon.music.model.Song
import io.github.muntashirakon.music.util.PreferenceUtil
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by hemanths on 11/08/17.
 */
interface AlbumRepository {
    fun albums(): List<Album>

    fun albums(query: String): List<Album>

    fun album(albumId: Int): Album
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

    override fun album(albumId: Int): Album {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                AudioColumns.ALBUM_ID + "=?",
                arrayOf(albumId.toString()),
                getSongLoaderSortOrder()
            )
        )
        val album = Album(ArrayList(songs))
        sortAlbumSongs(album)
        return album
    }

    fun splitIntoAlbums(
        songs: List<Song>?
    ): List<Album> {
        val albums = ArrayList<Album>()
        if (songs != null) {
            for (song in songs) {
                getOrCreateAlbum(albums, song.albumId).songs?.add(song)
            }
        }
        for (album in albums) {
            sortAlbumSongs(album)
        }
        return albums
    }

    private fun getOrCreateAlbum(
        albums: ArrayList<Album>,
        albumId: Int
    ): Album {
        for (album in albums) {
            if (album.songs!!.isNotEmpty() && album.songs[0].albumId == albumId) {
                return album
            }
        }
        val album = Album()
        albums.add(album)
        return album
    }

    private fun sortAlbumSongs(album: Album) {
        when (PreferenceUtil.albumDetailSongSortOrder) {
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST -> album.songs?.sortWith(Comparator { o1, o2 ->
                o1.trackNumber.compareTo(
                    o2.trackNumber
                )
            })
            SortOrder.AlbumSongSortOrder.SONG_A_Z -> album.songs?.sortWith(Comparator { o1, o2 ->
                o1.title.compareTo(
                    o2.title
                )
            })
            SortOrder.AlbumSongSortOrder.SONG_Z_A -> album.songs?.sortWith(Comparator { o1, o2 ->
                o2.title.compareTo(
                    o1.title
                )
            })
            SortOrder.AlbumSongSortOrder.SONG_DURATION -> album.songs?.sortWith(Comparator { o1, o2 ->
                o1.duration.compareTo(
                    o2.duration
                )
            })
        }
    }

    private fun getSongLoaderSortOrder(): String {
        return PreferenceUtil.albumSortOrder + ", " +
                PreferenceUtil.albumSongSortOrder
    }


}
