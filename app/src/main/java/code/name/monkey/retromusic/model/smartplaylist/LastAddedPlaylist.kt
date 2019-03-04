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

package code.name.monkey.retromusic.model.smartplaylist

import android.content.Context

import java.util.ArrayList
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.loaders.LastAddedSongsLoader
import code.name.monkey.retromusic.model.Song
import io.reactivex.Observable

class LastAddedPlaylist(context: Context) : AbsSmartPlaylist(context.getString(R.string.last_added), R.drawable.ic_library_add_white_24dp) {

    override fun getSongs(context: Context): Observable<ArrayList<Song>> {
        return LastAddedSongsLoader.getLastAddedSongs(context)
    }

    override fun clear(context: Context) {}
}
