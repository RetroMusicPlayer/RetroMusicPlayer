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
import code.name.monkey.retromusic.helper.ShuffleHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.BlacklistStore
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import java.util.*


/**
 * Created by hemanths on 10/08/17.
 */

object SongLoader {
    fun getAllSongsFlowable(
            context: Context
    ): Observable<ArrayList<Song>> {
        val cursor = makeSongCursor(context, null, null)
        return getSongsFlowable(cursor)
    }

    fun getAllSongs(
            context: Context
    ): ArrayList<Song> {
        val cursor = makeSongCursor(context, null, null)
        return getSongs(cursor)
    }

    fun getSongsFlowable(
            cursor: Cursor?
    ): Observable<ArrayList<Song>> {
        return Observable.create { e ->
            val songs = ArrayList<Song>()
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    songs.add(getSongFromCursorImpl(cursor))
                } while (cursor.moveToNext())
            }

            cursor?.close()
            e.onNext(songs)
            e.onComplete()
        }
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

    fun getSongsFlowable(
            context: Context,
            query: String
    ): Observable<ArrayList<Song>> {
        val cursor = makeSongCursor(context, AudioColumns.TITLE + " LIKE ?", arrayOf("%$query%"))
        return getSongsFlowable(cursor)
    }

    fun getSongs(
            context: Context,
            query: String
    ): ArrayList<Song> {
        val cursor = makeSongCursor(context, AudioColumns.TITLE + " LIKE ?", arrayOf("%$query%"))
        return getSongs(cursor)
    }


    private fun getSongFlowable(
            cursor: Cursor?
    ): Observable<Song> {
        return Observable.create { e ->
            val song: Song = if (cursor != null && cursor.moveToFirst()) {
                getSongFromCursorImpl(cursor)
            } else {
                Song.emptySong
            }
            cursor?.close()
            e.onNext(song)
            e.onComplete()
        }
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

    fun getSongFlowable(
            context: Context,
            queryId: Int
    ): Observable<Song> {
        val cursor = makeSongCursor(context, AudioColumns._ID + "=?",
                arrayOf(queryId.toString()))
        return getSongFlowable(cursor)
    }

    fun getSong(context: Context, queryId: Int): Song {
        val cursor = makeSongCursor(context, AudioColumns._ID + "=?", arrayOf(queryId.toString()))
        return getSong(cursor)
    }

    fun suggestSongs(
            context: Context
    ): Observable<ArrayList<Song>> {
        return SongLoader.getAllSongsFlowable(context)
                .flatMap {
                    val list = ArrayList<Song>()
                    ShuffleHelper.makeShuffleList(it, -1)
                    if (it.size >= 7) {
                        list.addAll(it.subList(0, 7))
                    }
                    return@flatMap Observable.just(list)
                }
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

        return Song(id, title, trackNumber, year, duration, data, dateModified, albumId,
                albumName ?: "", artistId, artistName, composer ?: "")
    }


    @JvmOverloads
    fun makeSongCursor(
            context: Context,
            selection: String?,
            selectionValues: Array<String>?,
            sortOrder: String = PreferenceUtil.getInstance(context).songSortOrder
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
            return context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    baseProjection, selectionFinal + " AND " + MediaStore.Audio.Media.DURATION + ">= " + (PreferenceUtil.getInstance(context).filterLength * 1000), selectionValuesFinal, sortOrder)
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
