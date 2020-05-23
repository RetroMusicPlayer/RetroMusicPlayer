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

package code.name.monkey.retromusic.helper

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.Song
import java.util.*

object SearchQueryHelper {
    private const val TITLE_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.TITLE + ") = ?"
    private const val ALBUM_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ALBUM + ") = ?"
    private const val ARTIST_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ARTIST + ") = ?"
    private const val AND = " AND "
    var songs = ArrayList<Song>()

    @JvmStatic
    fun getSongs(context: Context, extras: Bundle): ArrayList<Song> {
        val query = extras.getString(SearchManager.QUERY, null)
        val artistName = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST, null)
        val albumName = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM, null)
        val titleName = extras.getString(MediaStore.EXTRA_MEDIA_TITLE, null)

        var songs = ArrayList<Song>()
        if (artistName != null && albumName != null && titleName != null) {
            songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    ARTIST_SELECTION + AND + ALBUM_SELECTION + AND + TITLE_SELECTION,
                    arrayOf(
                        artistName.toLowerCase(),
                        albumName.toLowerCase(),
                        titleName.toLowerCase()
                    )
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (artistName != null && titleName != null) {
            songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    ARTIST_SELECTION + AND + TITLE_SELECTION,
                    arrayOf(artistName.toLowerCase(), titleName.toLowerCase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (albumName != null && titleName != null) {
            songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    ALBUM_SELECTION + AND + TITLE_SELECTION,
                    arrayOf(albumName.toLowerCase(), titleName.toLowerCase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (artistName != null) {
            songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    ARTIST_SELECTION,
                    arrayOf(artistName.toLowerCase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (albumName != null) {
            songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    ALBUM_SELECTION,
                    arrayOf(albumName.toLowerCase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (titleName != null) {
            songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    TITLE_SELECTION,
                    arrayOf(titleName.toLowerCase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        songs =
            SongLoader.getSongs(
                SongLoader.makeSongCursor(
                    context,
                    ARTIST_SELECTION,
                    arrayOf(query.toLowerCase())
                )
            )

        if (songs.isNotEmpty()) {
            return songs
        }
        songs = SongLoader.getSongs(
            SongLoader.makeSongCursor(
                context,
                ALBUM_SELECTION,
                arrayOf(query.toLowerCase())
            )
        )
        if (songs.isNotEmpty()) {
            return songs
        }
        songs = SongLoader.getSongs(
            SongLoader.makeSongCursor(
                context,
                TITLE_SELECTION,
                arrayOf(query.toLowerCase())
            )
        )
        return if (songs.isNotEmpty()) {
            songs
        } else ArrayList()
    }
}
