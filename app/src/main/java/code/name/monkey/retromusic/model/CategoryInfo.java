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

import code.name.monkey.retromusic.R;


public class CategoryInfo implements Parcelable {
    public static final Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
        public CategoryInfo createFromParcel(Parcel source) {
            return new CategoryInfo(source);
        }

        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };
    public Category category;
    public boolean visible;

    public CategoryInfo(Category category, boolean visible) {
        this.category = category;
        this.visible = visible;
    }


    private CategoryInfo(Parcel source) {
        category = (Category) source.readSerializable();
        visible = source.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(category);
        dest.writeInt(visible ? 1 : 0);
    }

    public enum Category {
        HOME(R.id.action_home, R.string.home, R.drawable.toggle_home),
        SONGS(R.id.action_song, R.string.songs, R.drawable.toggle_audiotrack),
        ALBUMS(R.id.action_album, R.string.albums, R.drawable.toggle_album),
        ARTISTS(R.id.action_artist, R.string.artists, R.drawable.toggle_artist),
        PLAYLISTS(R.id.action_playlist, R.string.playlists, R.drawable.toggle_queue_music),
        GENRES(R.id.action_genre, R.string.genres, R.drawable.toggle_guitar);

        public final int stringRes;
        public final int id;
        public final int icon;

        Category(int id, int stringRes, int icon) {
            this.stringRes = stringRes;
            this.id = id;
            this.icon = icon;
        }
    }
}
