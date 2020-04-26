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
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import code.name.monkey.retromusic.room.SongEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "playing_queue")
open class Song(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "track_number") val trackNumber: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "date_modified") val dateModified: Long,
    @ColumnInfo(name = "album_id") val albumId: Int,
    @ColumnInfo(name = "album_name") val albumName: String,
    @ColumnInfo(name = "artist_id") val artistId: Int,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "composer") val composer: String?
) : Parcelable {


    companion object {
        fun toSongEntity(song: Song): SongEntity {
           return  SongEntity(
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