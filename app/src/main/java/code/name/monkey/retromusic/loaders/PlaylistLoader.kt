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
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.MediaStore.Audio.PlaylistsColumns
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Playlist
import io.reactivex.Observable
import java.util.*

/**
 * Created by hemanths on 16/08/17.
 */

object PlaylistLoader {
    private fun makePlaylistCursor(context: Context, selection: String?, values: Array<String>?): Cursor? {
        return try {
            context.contentResolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                    arrayOf(
                            /* 0 */
                            BaseColumns._ID,
                            /* 1 */
                            PlaylistsColumns.NAME), selection, values, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER)

        } catch (e: SecurityException) {
            null
        }
    }

    private fun getPlaylist(cursor: Cursor?): Observable<Playlist> {
        return Observable.create { e ->
            var playlist = Playlist()

            if (cursor != null && cursor.moveToFirst()) {
                playlist = getPlaylistFromCursorImpl(cursor)
            }
            cursor?.close()

            e.onNext(playlist)
            e.onComplete()
        }


    }

    fun getPlaylist(context: Context, playlistName: String): Observable<Playlist> {
        return getPlaylist(makePlaylistCursor(
                context,
                PlaylistsColumns.NAME + "=?",
                arrayOf(playlistName)
        ))
    }

    fun getPlaylist(context: Context, playlistId: Int): Observable<Playlist> {
        return getPlaylist(makePlaylistCursor(
                context,
                BaseColumns._ID + "=?",
                arrayOf(playlistId.toString())
        ))
    }

    private fun getPlaylistFromCursorImpl(cursor: Cursor): Playlist {

        val id = cursor.getInt(0)
        val name = cursor.getString(1)
        return Playlist(id, name)
    }


    private fun getAllPlaylists(cursor: Cursor?): Observable<ArrayList<Playlist>> {
        return Observable.create { e ->
            val playlists = ArrayList<Playlist>()

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    playlists.add(getPlaylistFromCursorImpl(cursor))
                } while (cursor.moveToNext())
            }
            cursor?.close()

            e.onNext(playlists)
            e.onComplete()
        }
    }

    fun getAllPlaylists(context: Context): Observable<ArrayList<Playlist>> {
        return getAllPlaylists(makePlaylistCursor(context, null, null))
    }

    fun getFavoritePlaylist(context: Context): Observable<ArrayList<Playlist>> {
        return getAllPlaylists(makePlaylistCursor(
                context,
                PlaylistsColumns.NAME + "=?",
                arrayOf(context.getString(R.string.favorites))))
    }

    fun deletePlaylists(context: Context, playlistId: Long) {
        val localUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val localStringBuilder = StringBuilder()
        localStringBuilder.append("_id IN (")
        localStringBuilder.append(playlistId)
        localStringBuilder.append(")")
        context.contentResolver.delete(localUri, localStringBuilder.toString(), null)
    }
}
