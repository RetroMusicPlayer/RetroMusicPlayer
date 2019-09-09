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
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore.Audio.Genres
import code.name.monkey.retromusic.Constants.baseProjection
import code.name.monkey.retromusic.Constants.BASE_SELECTION
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import java.util.*


object GenreLoader {

    fun getAllGenresFlowable(context: Context): Observable<ArrayList<Genre>> {
        return getGenresFromCursorFlowable(context, makeGenreCursor(context))
    }

    fun getAllGenres(context: Context): ArrayList<Genre> {
        return getGenresFromCursor(context, makeGenreCursor(context))
    }

    fun getSongsFlowable(context: Context, genreId: Int): Observable<ArrayList<Song>> {
        // The genres table only stores songs that have a genre specified,
        // so we need to get songs without a genre a different way.
        return if (genreId == -1) {
            getSongsWithNoGenreFlowable(context)
        } else SongLoader.getSongsFlowable(makeGenreSongCursor(context, genreId))

    }

    fun getSongs(context: Context, genreId: Int): ArrayList<Song> {
        // The genres table only stores songs that have a genre specified,
        // so we need to get songs without a genre a different way.
        return if (genreId == -1) {
            getSongsWithNoGenre(context)
        } else SongLoader.getSongs(makeGenreSongCursor(context, genreId))

    }

    private fun getGenreFromCursor(context: Context, cursor: Cursor): Genre {
        val id = cursor.getInt(0)
        val name = cursor.getString(1)
        val songCount = getSongs(context, id).size
        return Genre(id, name, songCount)

    }

    private fun getSongsWithNoGenreFlowable(context: Context): Observable<ArrayList<Song>> {
        val selection = BaseColumns._ID + " NOT IN " +
                "(SELECT " + Genres.Members.AUDIO_ID + " FROM audio_genres_map)"
        return SongLoader.getSongsFlowable(SongLoader.makeSongCursor(context, selection, null))
    }

    private fun getSongsWithNoGenre(context: Context): ArrayList<Song> {
        val selection = BaseColumns._ID + " NOT IN " +
                "(SELECT " + Genres.Members.AUDIO_ID + " FROM audio_genres_map)"
        return SongLoader.getSongs(SongLoader.makeSongCursor(context, selection, null))
    }

    private fun hasSongsWithNoGenre(context: Context): Boolean {
        val allSongsCursor = SongLoader.makeSongCursor(context, null, null)
        val allSongsWithGenreCursor = makeAllSongsWithGenreCursor(context)

        if (allSongsCursor == null || allSongsWithGenreCursor == null) {
            return false
        }

        val hasSongsWithNoGenre = allSongsCursor.count > allSongsWithGenreCursor.count
        allSongsCursor.close()
        allSongsWithGenreCursor.close()
        return hasSongsWithNoGenre
    }

    private fun makeAllSongsWithGenreCursor(context: Context): Cursor? {
        try {
            return context.contentResolver.query(
                    Uri.parse("content://media/external/audio/genres/all/members"),
                    arrayOf(Genres.Members.AUDIO_ID), null, null, null)
        } catch (e: SecurityException) {
            return null
        }

    }

    private fun makeGenreSongCursor(context: Context, genreId: Int): Cursor? {
        try {
            return context.contentResolver.query(
                    Genres.Members.getContentUri("external", genreId.toLong()),
                    baseProjection, BASE_SELECTION, null, PreferenceUtil.getInstance(context).songSortOrder)
        } catch (e: SecurityException) {
            return null
        }

    }

    private fun getGenresFromCursorFlowable(context: Context, cursor: Cursor?): Observable<ArrayList<Genre>> {
        return Observable.create { e ->
            val genres = ArrayList<Genre>()
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val genre = getGenreFromCursor(context, cursor)
                        if (genre.songCount > 0) {
                            genres.add(genre)
                        } else {
                            // try to remove the empty genre from the media store
                            try {
                                context.contentResolver.delete(Genres.EXTERNAL_CONTENT_URI, Genres._ID + " == " + genre.id, null)
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                // nothing we can do then
                            }

                        }
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
            e.onNext(genres)
            e.onComplete()
        }
    }

    private fun getGenresFromCursor(context: Context, cursor: Cursor?): ArrayList<Genre> {
        val genres = arrayListOf<Genre>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val genre = getGenreFromCursor(context, cursor)
                    if (genre.songCount > 0) {
                        genres.add(genre)
                    } else {
                        // try to remove the empty genre from the media store
                        try {
                            context.contentResolver.delete(Genres.EXTERNAL_CONTENT_URI, Genres._ID + " == " + genre.id, null)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // nothing we can do then
                        }

                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return genres
    }


    private fun makeGenreCursor(context: Context): Cursor? {
        val projection = arrayOf(Genres._ID, Genres.NAME)

        try {
            return context.contentResolver.query(
                    Genres.EXTERNAL_CONTENT_URI,
                    projection, null, null, PreferenceUtil.getInstance(context).genreSortOrder)
        } catch (e: SecurityException) {
            return null
        }

    }

}
