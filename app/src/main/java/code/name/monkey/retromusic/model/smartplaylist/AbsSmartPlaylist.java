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

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import kotlinx.android.parcel.Parcelize;

@Parcelize
public abstract class AbsSmartPlaylist extends AbsCustomPlaylist {
    @DrawableRes
    public final int iconRes;

    public AbsSmartPlaylist(final @NonNull String name, final int iconRes) {
        super(-Math.abs(31 * name.hashCode() + (iconRes * name.hashCode() * 31 * 31)), name);
        this.iconRes = iconRes;
    }

    public abstract void clear(@NonNull Context context);

}
