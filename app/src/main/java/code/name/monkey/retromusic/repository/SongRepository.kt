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
import android.database.Cursor
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.Constants.IS_MUSIC
import code.name.monkey.retromusic.Constants.baseProjection
import code.name.monkey.retromusic.extensions.getInt
import code.name.monkey.retromusic.extensions.getLong
import code.name.monkey.retromusic.extensions.getString
import code.name.monkey.retromusic.extensions.getStringOrNull
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.BlacklistStore
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.getExternalStoragePublicDirectory
import java.text.Collator

/**
 * Created by hemanths on 10/08/17.
 */
interface SongRepository {

    fun songs(): List<Song>

    fun songs(cursor: Cursor?): List<Song>

    fun sortedSongs(cursor: Cursor?): List<Song>

    fun songs(query: String): List<Song>

    fun songsByFilePath(filePath: String, ignoreBlacklist: Boolean = false): List<Song>

    fun song(cursor: Cursor?): Song

    fun song(songId: Long): Song
}

class RealSongRepository(private val context: Context) : SongRepository {

    override fun songs(): List<Song> {
        return sortedSongs(makeSongCursor(null, null))
    }

    override fun songs(cursor: Cursor?): List<Song> {
        val songs = arrayListOf<Song>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getSongFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    override fun sortedSongs(cursor: Cursor?): List<Song> {
        val collator = Collator.getInstance()
        val songs = songs(cursor)
        return when (PreferenceUtil.songSortOrder) {
            SortOrder.SongSortOrder.SONG_A_Z -> {
                songs.sortedWith{ s1, s2 -> collator.compare(s1.title, s2.title) }
            }
            SortOrder.SongSortOrder.SONG_Z_A -> {
                songs.sortedWith{ s1, s2 -> collator.compare(s2.title, s1.title) }
            }
            SortOrder.SongSortOrder.SONG_ALBUM -> {
                songs.sortedWith{ s1, s2 -> collator.compare(s1.albumName, s2.albumName) }
            }
            SortOrder.SongSortOrder.SONG_ALBUM_ARTIST -> {
                songs.sortedWith{ s1, s2 -> collator.compare(s1.albumArtist, s2.albumArtist) }
            }
            SortOrder.SongSortOrder.SONG_ARTIST -> {
                songs.sortedWith{ s1, s2 -> collator.compare(s1.artistName, s2.artistName) }
            }
            SortOrder.SongSortOrder.COMPOSER -> {
                songs.sortedWith{ s1, s2 -> collator.compare(s1.composer, s2.composer) }
            }
            else -> songs
        }
    }

    override fun song(cursor: Cursor?): Song {
        val song: Song = if (cursor != null && cursor.moveToFirst()) {
            getSongFromCursorImpl(cursor)
        } else {
            Song.emptySong
        }
        cursor?.close()
        return song
    }

    override fun songs(query: String): List<Song> {
        return songs(makeSongCursor(AudioColumns.TITLE + " LIKE ?", arrayOf("%$query%")))
    }

    override fun song(songId: Long): Song {
        return song(makeSongCursor(AudioColumns._ID + "=?", arrayOf(songId.toString())))
    }

    override fun songsByFilePath(filePath: String, ignoreBlacklist: Boolean): List<Song> {
        return songs(
            makeSongCursor(
                Constants.DATA + "=?",
                arrayOf(filePath),
                ignoreBlacklist = ignoreBlacklist
            )
        )
    }

    private fun getSongFromCursorImpl(
        cursor: Cursor
    ): Song {
        val id = cursor.getLong(AudioColumns._ID)
        val title = cursor.getString(AudioColumns.TITLE)
        val trackNumber = cursor.getInt(AudioColumns.TRACK)
        val year = cursor.getInt(AudioColumns.YEAR)
        val duration = cursor.getLong(AudioColumns.DURATION)
        val data = cursor.getString(Constants.DATA)
        val dateModified = cursor.getLong(AudioColumns.DATE_MODIFIED)
        val albumId = cursor.getLong(AudioColumns.ALBUM_ID)
        val albumName = cursor.getStringOrNull(AudioColumns.ALBUM)
        val artistId = cursor.getLong(AudioColumns.ARTIST_ID)
        val artistName = cursor.getStringOrNull(AudioColumns.ARTIST)
        val composer = cursor.getStringOrNull(AudioColumns.COMPOSER)
        val albumArtist = cursor.getStringOrNull("album_artist")
        return Song(
            id,
            title,
            trackNumber,
            year,
            duration,
            data,
            dateModified,
            albumId,
            albumName ?: "",
            artistId,
            artistName ?: "",
            composer ?: "",
            albumArtist ?: ""
        )
    }

    @JvmOverloads
    fun makeSongCursor(
        selection: String?,
        selectionValues: Array<String>?,
        sortOrder: String = PreferenceUtil.songSortOrder,
        ignoreBlacklist: Boolean = false
    ): Cursor? {
        var selectionFinal = selection
        var selectionValuesFinal = selectionValues
        if (!ignoreBlacklist) {
            selectionFinal = if (selection != null && selection.trim { it <= ' ' } != "") {
                "$IS_MUSIC AND $selectionFinal"
            } else {
                IS_MUSIC
            }

            // Whitelist
            if (PreferenceUtil.isWhiteList) {
                selectionFinal =
                    selectionFinal + " AND " + Constants.DATA + " LIKE ?"
                selectionValuesFinal = addSelectionValues(
                    selectionValuesFinal, arrayListOf(
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).canonicalPath
                    )
                )
            } else {
                // Blacklist
                val paths = BlacklistStore.getInstance(context).paths
                if (paths.isNotEmpty()) {
                    selectionFinal = generateBlacklistSelection(selectionFinal, paths.size)
                    selectionValuesFinal = addSelectionValues(selectionValuesFinal, paths)
                }
            }

            selectionFinal =
                selectionFinal + " AND " + Media.DURATION + ">= " + (PreferenceUtil.filterLength * 1000)
        }
        val uri = if (VersionUtils.hasQ()) {
            Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            Media.EXTERNAL_CONTENT_URI
        }
        return try {
            context.contentResolver.query(
                uri,
                baseProjection,
                selectionFinal,
                selectionValuesFinal,
                sortOrder
            )
        } catch (ex: SecurityException) {
            return null
        }
    }

    private fun generateBlacklistSelection(
        selection: String?,
        pathCount: Int
    ): String {
        val newSelection = StringBuilder(
            if (selection != null && selection.trim { it <= ' ' } != "") "$selection AND " else "")
        newSelection.append(Constants.DATA + " NOT LIKE ?")
        for (i in 0 until pathCount - 1) {
            newSelection.append(" AND " + Constants.DATA + " NOT LIKE ?")
        }
        return newSelection.toString()
    }

    private fun addSelectionValues(
        selectionValues: Array<String>?,
        paths: ArrayList<String>
    ): Array<String> {
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
