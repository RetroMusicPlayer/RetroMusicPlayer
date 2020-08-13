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

import android.content.ContentResolver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.MediaStore.Audio.PlaylistsColumns
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.PlaylistSong
import code.name.monkey.retromusic.model.Song

/**
 * Created by hemanths on 16/08/17.
 */
interface PlaylistRepository {
    fun playlist(cursor: Cursor?): Playlist

    fun searchPlaylist(query: String): List<Playlist>

    fun playlist(playlistName: String): Playlist

    fun playlists(): List<Playlist>

    fun playlists(cursor: Cursor?): List<Playlist>

    fun favoritePlaylist(playlistName: String): List<Playlist>

    fun deletePlaylist(playlistId: Int)

    fun playlist(playlistId: Int): Playlist

    fun playlistSongs(playlistId: Int): List<Song>
}

class RealPlaylistRepository(
    private val contentResolver: ContentResolver
) : PlaylistRepository {

    override fun playlist(cursor: Cursor?): Playlist {
        var playlist = Playlist()
        if (cursor != null && cursor.moveToFirst()) {
            playlist = getPlaylistFromCursorImpl(cursor)
        }
        cursor?.close()
        return playlist
    }

    override fun playlist(playlistName: String): Playlist {
        return playlist(makePlaylistCursor(PlaylistsColumns.NAME + "=?", arrayOf(playlistName)))
    }

    override fun playlist(playlistId: Int): Playlist {
        return playlist(
            makePlaylistCursor(
                BaseColumns._ID + "=?",
                arrayOf(playlistId.toString())
            )
        )
    }

    override fun searchPlaylist(query: String): List<Playlist> {
        return playlists(makePlaylistCursor(PlaylistsColumns.NAME + "=?", arrayOf(query)))
    }

    override fun playlists(): List<Playlist> {
        return playlists(makePlaylistCursor(null, null))
    }

    override fun playlists(cursor: Cursor?): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playlists.add(getPlaylistFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return playlists
    }

    override fun favoritePlaylist(playlistName: String): List<Playlist> {
        return playlists(
            makePlaylistCursor(
                PlaylistsColumns.NAME + "=?",
                arrayOf(playlistName)
            )
        )
    }

    override fun deletePlaylist(playlistId: Int) {
        val localUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val localStringBuilder = StringBuilder()
        localStringBuilder.append("_id IN (")
        localStringBuilder.append(playlistId)
        localStringBuilder.append(")")
        contentResolver.delete(localUri, localStringBuilder.toString(), null)
    }

    private fun getPlaylistFromCursorImpl(
        cursor: Cursor
    ): Playlist {
        val id = cursor.getInt(0)
        val name = cursor.getString(1)
        return Playlist(id, name)
    }

    override fun playlistSongs(playlistId: Int): List<Song> {
        val songs = arrayListOf<Song>()
        val cursor = makePlaylistSongCursor(playlistId)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getPlaylistSongFromCursorImpl(cursor, playlistId))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    private fun getPlaylistSongFromCursorImpl(cursor: Cursor, playlistId: Int): PlaylistSong {
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
        val idInPlaylist = cursor.getInt(11)
        val composer = cursor.getString(12)
        val albumArtist = cursor.getString(13)
        return PlaylistSong(
            id,
            title,
            trackNumber,
            year,
            duration,
            data,
            dateModified,
            albumId,
            albumName,
            artistId,
            artistName,
            playlistId,
            idInPlaylist,
            composer,
            albumArtist
        )
    }

    private fun makePlaylistCursor(
        selection: String?,
        values: Array<String>?
    ): Cursor? {
        return contentResolver.query(
            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
            arrayOf(
                BaseColumns._ID, /* 0 */
                PlaylistsColumns.NAME /* 1 */
            ),
            selection,
            values,
            MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER
        )
    }


    private fun makePlaylistSongCursor(playlistId: Int): Cursor? {
        return contentResolver.query(
            MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId.toLong()),
            arrayOf(
                MediaStore.Audio.Playlists.Members.AUDIO_ID, // 0
                MediaStore.Audio.AudioColumns.TITLE, // 1
                MediaStore.Audio.AudioColumns.TRACK, // 2
                MediaStore.Audio.AudioColumns.YEAR, // 3
                MediaStore.Audio.AudioColumns.DURATION, // 4
                MediaStore.Audio.AudioColumns.DATA, // 5
                MediaStore.Audio.AudioColumns.DATE_MODIFIED, // 6
                MediaStore.Audio.AudioColumns.ALBUM_ID, // 7
                MediaStore.Audio.AudioColumns.ALBUM, // 8
                MediaStore.Audio.AudioColumns.ARTIST_ID, // 9
                MediaStore.Audio.AudioColumns.ARTIST, // 10
                MediaStore.Audio.Playlists.Members._ID,//11
                MediaStore.Audio.AudioColumns.COMPOSER,//12
                "album_artist"//13
            ), Constants.IS_MUSIC, null, MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER
        )
    }
}
