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
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import java.util.*
import kotlin.collections.ArrayList

object ArtistLoader {
    private fun getSongLoaderSortOrder(): String {
        return PreferenceUtil.getInstance().artistSortOrder + ", " +
                PreferenceUtil.getInstance().artistAlbumSortOrder + ", " +
                PreferenceUtil.getInstance().albumDetailSongSortOrder + ", " +
                PreferenceUtil.getInstance().artistDetailSongSortOrder
    }

    fun getArtist(context: Context, artistId: Int): Observable<Artist> {
        return Observable.create { e ->
            SongLoader.getSongs(SongLoader.makeSongCursor(context, AudioColumns.ARTIST_ID + "=?",
                    arrayOf(artistId.toString()),
                    getSongLoaderSortOrder()))
                    .subscribe { songs ->
                        val artist = Artist(AlbumLoader.splitIntoAlbums(songs))
                        e.onNext(artist)
                        e.onComplete()
                    }
        }
    }

    fun getAllArtists(context: Context): Observable<ArrayList<Artist>> {
        return Observable.create { e ->
            SongLoader
                    .getSongs(SongLoader.makeSongCursor(
                            context, null, null,
                            getSongLoaderSortOrder())
                    ).subscribe { songs ->
                        e.onNext(splitIntoArtists(AlbumLoader.splitIntoAlbums(songs)))
                        e.onComplete()
                    }
        }

    }

    fun getArtists(context: Context, query: String): Observable<ArrayList<Artist>> {
        return Observable.create { e ->
            SongLoader.getSongs(SongLoader.makeSongCursor(
                    context,
                    AudioColumns.ARTIST + " LIKE ?",
                    arrayOf("%$query%"),
                    getSongLoaderSortOrder())
            ).subscribe { songs ->
                e.onNext(splitIntoArtists(AlbumLoader.splitIntoAlbums(songs)))
                e.onComplete()
            }
        }
    }

    fun splitIntoArtists(albums: ArrayList<Album>?): ArrayList<Artist> {
        val artists = ArrayList<Artist>()
        if (albums != null) {
            for (album in albums) {
                getOrCreateArtist(artists, album.artistId).albums!!.add(album)
            }
        }
        return artists
    }

    private fun getOrCreateArtist(artists: ArrayList<Artist>, artistId: Int): Artist {
        for (artist in artists) {
            if (!artist.albums!!.isEmpty() && !artist.albums[0].songs!!.isEmpty() && artist.albums[0].songs!![0].artistId == artistId) {
                return artist
            }
        }
        val album = Artist()
        artists.add(album)
        return album
    }

    fun splitIntoArtists(albums: Observable<ArrayList<Album>>): Observable<ArrayList<Artist>> {
        return Observable.create { e ->
            val artists = ArrayList<Artist>()
            albums.subscribe { localAlbums ->
                if (localAlbums != null) {
                    for (album in localAlbums) {
                        getOrCreateArtist(artists, album.artistId).albums!!.add(album)
                    }
                }
                e.onNext(artists)
                e.onComplete()
            }
        }
    }
}
