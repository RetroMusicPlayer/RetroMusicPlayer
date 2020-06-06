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

package code.name.monkey.retromusic.loaders

import android.content.Context
import android.provider.MediaStore.Audio.AudioColumns
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by hemanths on 11/08/17.
 */

object AlbumLoader {

    fun getAlbums(
        context: Context,
        query: String
    ): ArrayList<Album> {
        val songs = SongLoader.getSongs(
            SongLoader.makeSongCursor(
                context,
                AudioColumns.ALBUM + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        return splitIntoAlbums(songs)
    }

    @JvmStatic
    fun getAlbum(
        context: Context,
        albumId: Int
    ): Album {
        val songs = SongLoader.getSongs(
            SongLoader.makeSongCursor(
                context,
                AudioColumns.ALBUM_ID + "=?",
                arrayOf(albumId.toString()),
                getSongLoaderSortOrder()
            )
        )
        val album = Album(songs)
        sortSongsByTrackNumber(album)
        return album
    }

    fun getAllAlbums(
        context: Context
    ): ArrayList<Album> {
        val songs = SongLoader.getSongs(
            SongLoader.makeSongCursor(
                context,
                null,
                null,
                getSongLoaderSortOrder()
            )
        )
        return splitIntoAlbums(songs)
    }

    fun splitIntoAlbums(
        songs: ArrayList<Song>?
    ): ArrayList<Album> {
        val albums = ArrayList<Album>()
        if (songs != null) {
            for (song in songs) {
                getOrCreateAlbum(albums, song.albumId).songs?.add(song)
            }
        }
        for (album in albums) {
            sortSongsByTrackNumber(album)
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

    private fun sortSongsByTrackNumber(album: Album) {
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
