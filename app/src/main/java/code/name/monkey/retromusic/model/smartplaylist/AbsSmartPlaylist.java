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

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;


public abstract class AbsSmartPlaylist extends AbsCustomPlaylist {
    @DrawableRes
    public final int iconRes;

    public AbsSmartPlaylist(final String name, final int iconRes) {
        super(-Math.abs(31 * name.hashCode() + (iconRes * name.hashCode() * 31 * 31)), name);
        this.iconRes = iconRes;
    }

    public AbsSmartPlaylist() {
        super();
        this.iconRes = R.drawable.ic_queue_music_white_24dp;
    }

    public abstract void clear(Context context);

    public boolean isClearable() {
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + iconRes;
        return result;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (super.equals(obj)) {
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AbsSmartPlaylist other = (AbsSmartPlaylist) obj;
            return iconRes == other.iconRes;
        }
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.iconRes);
    }

    protected AbsSmartPlaylist(Parcel in) {
        super(in);
        this.iconRes = in.readInt();
    }
}
