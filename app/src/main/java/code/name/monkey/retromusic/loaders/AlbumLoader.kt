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
import android.provider.MediaStore.Audio.AudioColumns
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import java.util.*

/**
 * Created by hemanths on 11/08/17.
 */

open class AlbumLoader {
    companion object {
        fun getAllAlbums(context: Context): Observable<ArrayList<Album>> {
            val songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                    context, null, null,
                    getSongLoaderSortOrder())
            )

            return splitIntoAlbums(songs)
        }

        fun getAlbums(context: Context,
                      query: String): Observable<ArrayList<Album>> {
            val songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                    context,
                    AudioColumns.ALBUM + " LIKE ?",
                    arrayOf("%$query%"),
                    getSongLoaderSortOrder())
            )
            return splitIntoAlbums(songs)
        }

        fun getAlbum(context: Context, albumId: Int): Observable<Album> {
            return Observable.create { e ->
                val songs = SongLoader.getSongs(SongLoader
                        .makeSongCursor(context, AudioColumns.ALBUM_ID + "=?",
                                arrayOf(albumId.toString()), getSongLoaderSortOrder()))
                songs.subscribe { songs1 ->
                    e.onNext(Album(songs1))
                    e.onComplete()
                }
            }
        }

        fun splitIntoAlbums(
                songs: Observable<ArrayList<Song>>?): Observable<ArrayList<Album>> {
            return Observable.create { e ->
                val albums = ArrayList<Album>()
                songs?.subscribe { songs1 ->
                    for (song in songs1) {
                        getOrCreateAlbum(albums, song.albumId).subscribe { album -> album.songs!!.add(song) }
                    }
                }
                e.onNext(albums)
                e.onComplete()
            }
        }

        fun splitIntoAlbums(songs: ArrayList<Song>?): ArrayList<Album> {
            val albums = ArrayList<Album>()
            if (songs != null) {
                for (song in songs) {
                    getOrCreateAlbum(albums, song.albumId).subscribe { album -> album.songs!!.add(song) }
                }
            }
            return albums
        }

        private fun getOrCreateAlbum(albums: ArrayList<Album>, albumId: Int): Observable<Album> {
            return Observable.create { e ->
                for (album in albums) {
                    if (!album.songs!!.isEmpty() && album.songs[0].albumId == albumId) {
                        e.onNext(album)
                        e.onComplete()
                        return@create
                    }
                }
                val album = Album()
                albums.add(album)
                e.onNext(album)
                e.onComplete()
            }
        }

        private fun getSongLoaderSortOrder(): String {
            return PreferenceUtil.getInstance().albumSortOrder + ", " +
                    //PreferenceUtil.getInstance().getAlbumSongSortOrder() + "," +
                    PreferenceUtil.getInstance().albumDetailSongSortOrder
        }
    }
}
