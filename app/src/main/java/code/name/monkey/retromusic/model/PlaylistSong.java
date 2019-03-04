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

package code.name.monkey.retromusic.model;

import org.jetbrains.annotations.NotNull;

import kotlinx.android.parcel.Parcelize;

/**
 * Created by hemanths on 3/4/19
 */
@Parcelize
public class PlaylistSong extends Song {
    final int playlistId;
    final int idInPlayList;

    public PlaylistSong(int id,
                        @NotNull String title,
                        int trackNumber,
                        int year,
                        long duration,
                        @NotNull String data,
                        long dateModified,
                        int albumId,
                        @NotNull String albumName,
                        int artistId,
                        @NotNull String artistName,
                        int playlistId,
                        int idInPlayList,
                        @NotNull String composer) {
        super(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName, composer);
        this.playlistId = playlistId;
        this.idInPlayList = idInPlayList;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public int getIdInPlayList() {
        return idInPlayList;
    }
}
