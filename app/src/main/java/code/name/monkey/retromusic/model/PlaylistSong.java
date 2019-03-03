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

import android.os.Parcel;

public class PlaylistSong extends Song {
    public static final Creator<PlaylistSong> CREATOR = new Creator<PlaylistSong>() {
        public PlaylistSong createFromParcel(Parcel source) {
            return new PlaylistSong(source);
        }

        public PlaylistSong[] newArray(int size) {
            return new PlaylistSong[size];
        }
    };
    public final int playlistId;
    public final int idInPlayList;

    public PlaylistSong(int id, String title, int trackNumber, int year, long duration, String data, int dateModified, int albumId, String albumName, int artistId, String artistName, final int playlistId, final int idInPlayList, String composer) {
        super(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName, composer);
        this.playlistId = playlistId;
        this.idInPlayList = idInPlayList;
    }

    protected PlaylistSong(Parcel in) {
        super(in);
        this.playlistId = in.readInt();
        this.idInPlayList = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PlaylistSong that = (PlaylistSong) o;

        if (playlistId != that.playlistId) return false;
        return idInPlayList == that.idInPlayList;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + playlistId;
        result = 31 * result + idInPlayList;
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                "PlaylistSong{" +
                "playlistId=" + playlistId +
                ", idInPlayList=" + idInPlayList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.playlistId);
        dest.writeInt(this.idInPlayList);
    }
}
