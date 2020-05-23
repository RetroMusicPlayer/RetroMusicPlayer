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
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import code.name.monkey.retromusic.Constants.BASE_SELECTION
import code.name.monkey.retromusic.Constants.baseProjection
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.BlacklistStore

import code.name.monkey.retromusic.util.PreferenceUtilKT
import java.util.*

/**
 * Created by hemanths on 10/08/17.
 */

object SongLoader {

    fun getAllSongs(
        context: Context
    ): ArrayList<Song> {
        val cursor = makeSongCursor(context, null, null)
        return getSongs(cursor)
    }

    fun getSongs(
        cursor: Cursor?
    ): ArrayList<Song> {
        val songs = arrayListOf<Song>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getSongFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return songs
    }

    fun getSongs(
        context: Context,
        query: String
    ): ArrayList<Song> {
        val cursor = makeSongCursor(context, AudioColumns.TITLE + " LIKE ?", arrayOf("%$query%"))
        return getSongs(cursor)
    }

    fun getSong(
        cursor: Cursor?
    ): Song {
        val song: Song
        if (cursor != null && cursor.moveToFirst()) {
            song = getSongFromCursorImpl(cursor)
        } else {
            song = Song.emptySong
        }
        cursor?.close()
        return song
    }

    @JvmStatic
    fun getSong(context: Context, queryId: Int): Song {
        val cursor = makeSongCursor(context, AudioColumns._ID + "=?", arrayOf(queryId.toString()))
        return getSong(cursor)
    }

    private fun getSongFromCursorImpl(
        cursor: Cursor
    ): Song {
        val id = cursor.getInt(0)
        val title = cursor.getString(1)
        val trackNumber = cursor.getInt(2)
        val year = cursor.getInt(3)
        val duration = cursor.getLong(4)
        val data = cursor.getString(5)
        val dateModified = cursor.getLong(6)
        val albumId = cursor.getInt(7)
        val albumName = cursor.getString(8)
        val artistId = cursor.getInt(9)
        val artistName = cursor.getString(10)
        val composer = cursor.getString(11)

        return Song(
            id, title, trackNumber, year, duration, data, dateModified, albumId,
            albumName ?: "", artistId, artistName, composer ?: ""
        )
    }

    @JvmOverloads
    fun makeSongCursor(
        context: Context,
        selection: String?,
        selectionValues: Array<String>?,
        sortOrder: String = PreferenceUtilKT.songSortOrder
    ): Cursor? {
        var selectionFinal = selection
        var selectionValuesFinal = selectionValues
        selectionFinal = if (selection != null && selection.trim { it <= ' ' } != "") {
            "$BASE_SELECTION AND $selectionFinal"
        } else {
            BASE_SELECTION
        }

        // Blacklist
        val paths = BlacklistStore.getInstance(context).paths
        if (paths.isNotEmpty()) {
            selectionFinal = generateBlacklistSelection(selectionFinal, paths.size)
            selectionValuesFinal = addBlacklistSelectionValues(selectionValuesFinal, paths)
        }

        try {
            return context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                baseProjection,
                selectionFinal + " AND " + MediaStore.Audio.Media.DURATION + ">= " +
                        (PreferenceUtilKT.filterLength * 1000),
                selectionValuesFinal,
                sortOrder
            )
        } catch (e: SecurityException) {
            return null
        }
    }

    private fun generateBlacklistSelection(
        selection: String?,
        pathCount: Int
    ): String {
        val newSelection = StringBuilder(
            if (selection != null && selection.trim { it <= ' ' } != "") "$selection AND " else "")
        newSelection.append(AudioColumns.DATA + " NOT LIKE ?")
        for (i in 0 until pathCount - 1) {
            newSelection.append(" AND " + AudioColumns.DATA + " NOT LIKE ?")
        }
        return newSelection.toString()
    }

    private fun addBlacklistSelectionValues(
        selectionValues: Array<String>?,
        paths: ArrayList<String>
    ): Array<String>? {
        var selectionValuesFinal = selectionValues
        if (selectionValuesFinal == null) {
            selectionValuesFinal = emptyArray()
        }
        val newSelectionValues = Array(selectionValuesFinal.size + paths.size) {
            "n = $it"
        }
        System.arraycopy(selectionValuesFinal, 0, newSelectionValues, 0, selectionValuesFinal.size)
        for (i in selectionValuesFinal.size until newSelectionValues.size) {
            newSelectionValues[i] = paths[i - selectionValuesFinal.size] + "%"
        }
        return newSelectionValues
    }
}
