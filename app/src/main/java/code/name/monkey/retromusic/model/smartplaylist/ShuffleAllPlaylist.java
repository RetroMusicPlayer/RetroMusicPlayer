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

package code.name.monkey.retromusic.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;


import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.model.Song;

import java.util.ArrayList;

import io.reactivex.Observable;

public class ShuffleAllPlaylist extends AbsSmartPlaylist {

    public static final Parcelable.Creator<ShuffleAllPlaylist> CREATOR = new Parcelable.Creator<ShuffleAllPlaylist>() {
        public ShuffleAllPlaylist createFromParcel(Parcel source) {
            return new ShuffleAllPlaylist(source);
        }

        public ShuffleAllPlaylist[] newArray(int size) {
            return new ShuffleAllPlaylist[size];
        }
    };

    public ShuffleAllPlaylist(@NonNull Context context) {
        super(context.getString(R.string.action_shuffle_all), R.drawable.ic_shuffle_white_24dp);
    }

    protected ShuffleAllPlaylist(Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    public Observable<ArrayList<Song>> getSongs(@NonNull Context context) {
        return SongLoader.INSTANCE.getAllSongs(context);
    }

    @Override
    public void clear(@NonNull Context context) {
        // Shuffle all is not a real "Smart Playlist"
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
