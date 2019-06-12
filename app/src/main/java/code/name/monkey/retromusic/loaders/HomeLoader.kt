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
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist
import io.reactivex.Observable

object HomeLoader {

    fun getRecentAndTopThings(context: Context): Observable<ArrayList<AbsSmartPlaylist>> {
        val objects = ArrayList<AbsSmartPlaylist>()

        return Observable.create { e ->

            HistoryPlaylist(context).getSongs(context).subscribe { songs ->
                if (!songs.isEmpty()) {
                    objects.add(HistoryPlaylist(context))
                }
            }
            LastAddedPlaylist(context).getSongs(context).subscribe { songs ->
                if (!songs.isEmpty()) {
                    objects.add(LastAddedPlaylist(context))
                }
            }
            MyTopTracksPlaylist(context).getSongs(context).subscribe { songs ->
                if (!songs.isEmpty()) {
                    objects.add(MyTopTracksPlaylist(context))
                }
            }

            e.onNext(objects)
            e.onComplete()
        }
    }

    fun getHomeLoader(context: Context): Observable<ArrayList<Playlist>> {
        val playlists = ArrayList<Playlist>()
        PlaylistLoader.getAllPlaylists(context)
                .subscribe { playlists1 ->
                    if (playlists1.size > 0) {
                        for (playlist in playlists1) {
                            PlaylistSongsLoader.getPlaylistSongList(context, playlist)
                                    .subscribe { songs ->
                                        if (songs.size > 0) {
                                            playlists.add(playlist)
                                        }
                                    }
                        }
                    }
                }
        return Observable.just(playlists)
    }
}
