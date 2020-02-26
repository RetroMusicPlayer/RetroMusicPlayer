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

package code.name.monkey.retromusic.misc

import android.content.Context
import android.text.TextUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.loaders.AlbumLoader
import code.name.monkey.retromusic.loaders.ArtistLoader
import code.name.monkey.retromusic.loaders.SongLoader
import java.util.*

internal class AsyncSearchResultLoader(context: Context, private val query: String) :
    WrappedAsyncTaskLoader<List<Any>>(context) {

    override fun loadInBackground(): List<Any>? {
        val results = ArrayList<Any>()
        if (!TextUtils.isEmpty(query)) {
            val songs = SongLoader.getSongs(context, query.trim { it <= ' ' })
            if (!songs.isEmpty()) {
                results.add(context.resources.getString(R.string.songs))
                results.addAll(songs)
            }

            val artists = ArtistLoader.getArtists(context, query.trim { it <= ' ' })
            if (!artists.isEmpty()) {
                results.add(context.resources.getString(R.string.artists))
                results.addAll(artists)
            }

            val albums = AlbumLoader.getAlbums(context, query.trim { it <= ' ' })
            if (!albums.isEmpty()) {
                results.add(context.resources.getString(R.string.albums))
                results.addAll(albums)
            }
        }
        return results
    }
}