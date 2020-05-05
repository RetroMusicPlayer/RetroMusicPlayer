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
package code.name.monkey.retromusic.model

import android.os.Parcelable
import code.name.monkey.retromusic.room.SongEntity
import code.name.monkey.retromusic.room.SongQueueEntity
import code.name.monkey.retromusic.room.playlist.PlaylistEntity
import code.name.monkey.retromusic.room.playlist.PlaylistSongEntity
import kotlinx.android.parcel.Parcelize

@Parcelize

open class Song(
    val id: Int,
    val title: String,
    val trackNumber: Int,
    val year: Int,
    val duration: Long,
    val data: String,
    val dateModified: Long,
    val albumId: Int,
    val albumName: String,
    val artistId: Int,
    val artistName: String,
    val composer: String?
) : Parcelable {


    companion object {
        fun toSongEntity(song: Song): SongQueueEntity {
            return SongQueueEntity(
                song.id,
                song.title,
                song.trackNumber,
                song.year,
                song.duration,
                song.data,
                song.dateModified,
                song.albumId,
                song.albumName,
                song.artistId,
                song.artistName,
                song.composer
            )
        }

        fun toSongQueueEntity(song: Song): SongEntity {
            return SongEntity(
                song.id,
                song.title,
                song.trackNumber,
                song.year,
                song.duration,
                song.data,
                song.dateModified,
                song.albumId,
                song.albumName,
                song.artistId,
                song.artistName,
                song.composer
            )
        }

        fun toPlaylistSong(song: Song, playlistEntity: PlaylistEntity): PlaylistSongEntity {
            return PlaylistSongEntity(
                playlistEntity.playlistId,
                playlistEntity.playlistName, song.id,
                song.title,
                song.trackNumber,
                song.year,
                song.duration,
                song.data,
                song.dateModified,
                song.albumId,
                song.albumName,
                song.artistId,
                song.artistName,
                song.composer
            )
        }

        @JvmStatic
        val emptySong = Song(
            -1,
            "",
            -1,
            -1,
            -1,
            "",
            -1,
            -1,
            "",
            -1,
            "",
            ""
        )
    }
}