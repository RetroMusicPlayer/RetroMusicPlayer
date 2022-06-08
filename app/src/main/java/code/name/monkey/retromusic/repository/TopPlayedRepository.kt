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
import android.provider.BaseColumns
import android.provider.MediaStore
import code.name.monkey.retromusic.Constants.NUMBER_OF_TOP_TRACKS
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.HistoryStore
import code.name.monkey.retromusic.providers.SongPlayCountStore
import code.name.monkey.retromusic.util.PreferenceUtil


/**
 * Created by hemanths on 16/08/17.
 */

interface TopPlayedRepository {
    fun recentlyPlayedTracks(): List<Song>

    fun topTracks(): List<Song>

    fun notRecentlyPlayedTracks(): List<Song>

    fun topAlbums(): List<Album>

    fun topArtists(): List<Artist>
}

class RealTopPlayedRepository(
    private val context: Context,
    private val songRepository: RealSongRepository,
    private val albumRepository: RealAlbumRepository,
    private val artistRepository: RealArtistRepository
) : TopPlayedRepository {

    override fun recentlyPlayedTracks(): List<Song> {
        return songRepository.songs(makeRecentTracksCursorAndClearUpDatabase())
    }

    override fun topTracks(): List<Song> {
        return songRepository.songs(makeTopTracksCursorAndClearUpDatabase())
    }

    override fun notRecentlyPlayedTracks(): List<Song> {
        val allSongs = mutableListOf<Song>().apply {
            addAll(
                songRepository.songs(
                    songRepository.makeSongCursor(
                        null, null,
                        MediaStore.Audio.Media.DATE_ADDED + " ASC"
                    )
                )
            )
        }
        val playedSongs = songRepository.songs(
            makePlayedTracksCursorAndClearUpDatabase()
        )
        val notRecentlyPlayedSongs = songRepository.songs(
            makeNotRecentTracksCursorAndClearUpDatabase()
        )
        allSongs.removeAll(playedSongs.toSet())
        allSongs.addAll(notRecentlyPlayedSongs)
        return allSongs
    }

    override fun topAlbums(): List<Album> {
        return albumRepository.splitIntoAlbums(topTracks(), sorted = false)
    }

    override fun topArtists(): List<Artist> {
        return artistRepository.splitIntoArtists(topAlbums())
    }


    private fun makeTopTracksCursorAndClearUpDatabase(): Cursor? {
        val retCursor = makeTopTracksCursorImpl()
        // clean up the databases with any ids not found
        if (retCursor != null) {
            val missingIds = retCursor.missingIds
            if (missingIds != null && missingIds.size > 0) {
                for (id in missingIds) {
                    SongPlayCountStore.getInstance(context).removeItem(id)
                }
            }
        }
        return retCursor
    }

    private fun makeRecentTracksCursorImpl(): SortedLongCursor? {
        // first get the top results ids from the internal database
        val songs = HistoryStore.getInstance(context).queryRecentIds()
        songs.use {
            return makeSortedCursor(
                it,
                it.getColumnIndex(HistoryStore.RecentStoreColumns.ID)
            )
        }
    }

    private fun makeTopTracksCursorImpl(): SortedLongCursor? {
        // first get the top results ids from the internal database
        val cursor =
            SongPlayCountStore.getInstance(context).getTopPlayedResults(NUMBER_OF_TOP_TRACKS)

        cursor.use { songs ->
            return makeSortedCursor(
                songs,
                songs.getColumnIndex(SongPlayCountStore.SongPlayCountColumns.ID)
            )
        }
    }

    private fun makeSortedCursor(
        cursor: Cursor?, idColumn: Int
    ): SortedLongCursor? {

        if (cursor != null && cursor.moveToFirst()) {
            // create the list of ids to select against
            val selection = StringBuilder()
            selection.append(BaseColumns._ID)
            selection.append(" IN (")

            // this tracks the order of the ids
            val order = LongArray(cursor.count)

            var id = cursor.getLong(idColumn)
            selection.append(id)
            order[cursor.position] = id

            while (cursor.moveToNext()) {
                selection.append(",")

                id = cursor.getLong(idColumn)
                order[cursor.position] = id
                selection.append(id.toString())
            }

            selection.append(")")

            // get a list of songs with the data given the selection statement
            val songCursor = songRepository.makeSongCursor(selection.toString(), null)
            if (songCursor != null) {
                // now return the wrapped TopTracksCursor to handle sorting given order
                return SortedLongCursor(
                    songCursor,
                    order,
                    BaseColumns._ID
                )
            }
        }

        return null
    }

    private fun makeRecentTracksCursorAndClearUpDatabase(): Cursor? {
        return makeRecentTracksCursorAndClearUpDatabaseImpl(
            ignoreCutoffTime = false,
            reverseOrder = false
        )
    }

    private fun makePlayedTracksCursorAndClearUpDatabase(): Cursor? {
        return makeRecentTracksCursorAndClearUpDatabaseImpl(
            ignoreCutoffTime = true,
            reverseOrder = false
        )
    }

    private fun makeNotRecentTracksCursorAndClearUpDatabase(): Cursor? {
        return makeRecentTracksCursorAndClearUpDatabaseImpl(
            ignoreCutoffTime = false,
            reverseOrder = true
        )
    }

    private fun makeRecentTracksCursorAndClearUpDatabaseImpl(
        ignoreCutoffTime: Boolean,
        reverseOrder: Boolean
    ): SortedLongCursor? {
        val retCursor = makeRecentTracksCursorImpl(ignoreCutoffTime, reverseOrder)
        // clean up the databases with any ids not found
        // clean up the databases with any ids not found
        if (retCursor != null) {
            val missingIds = retCursor.missingIds
            if (missingIds != null && missingIds.size > 0) {
                for (id in missingIds) {
                    HistoryStore.getInstance(context).removeSongId(id)
                }
            }
        }
        return retCursor
    }

    private fun makeRecentTracksCursorImpl(
        ignoreCutoffTime: Boolean,
        reverseOrder: Boolean
    ): SortedLongCursor? {
        val cutoff =
            (if (ignoreCutoffTime) 0 else PreferenceUtil.getRecentlyPlayedCutoffTimeMillis()).toLong()
        val songs =
            HistoryStore.getInstance(context).queryRecentIds(cutoff * if (reverseOrder) -1 else 1)
        return songs.use {
            makeSortedCursor(
                it,
                it.getColumnIndex(HistoryStore.RecentStoreColumns.ID)
            )
        }
    }
}
