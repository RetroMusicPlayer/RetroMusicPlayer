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

package code.name.monkey.retromusic.loaders

import android.content.Context
import android.text.TextUtils
import code.name.monkey.retromusic.R
import io.reactivex.Observable
import java.util.*

object SearchLoader {

    fun searchAll(context: Context, query: String?): Observable<ArrayList<Any>> {
        val results = ArrayList<Any>()
        return Observable.create { e ->
            if (!TextUtils.isEmpty(query)) {
                SongLoader.getSongs(context, query!!)
                        .subscribe { songs ->
                            if (!songs.isEmpty()) {
                                results.add(context.resources.getString(R.string.songs))
                                results.addAll(songs)
                            }
                        }

                ArtistLoader.getArtists(context, query)
                        .subscribe { artists ->
                            if (!artists.isEmpty()) {
                                results.add(context.resources.getString(R.string.artists))
                                results.addAll(artists)
                            }
                        }
                AlbumLoader.getAlbums(context, query)
                        .subscribe { albums ->
                            if (!albums.isEmpty()) {
                                results.add(context.resources.getString(R.string.albums))
                                results.addAll(albums)
                            }
                        }
            }
            e.onNext(results)
            e.onComplete()
        }
    }
}
