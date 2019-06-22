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
import android.os.Parcelable;


public class Playlist implements Parcelable {
    public final int id;
    public final String name;

    public Playlist(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Playlist() {
        this.id = -1;
        this.name = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playlist playlist = (Playlist) o;

        if (id != playlist.id) return false;
        return name != null ? name.equals(playlist.name) : playlist.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected Playlist(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };


}
