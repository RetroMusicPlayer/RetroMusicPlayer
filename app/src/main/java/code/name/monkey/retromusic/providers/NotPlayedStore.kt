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

package code.name.monkey.retromusic.providers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.Song
import io.reactivex.schedulers.Schedulers

class NotPlayedStore(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    private val dataBaseCreate = "CREATE TABLE IF NOT EXISTS  $NAME ( $ID LONG PRIMARY KEY )"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(dataBaseCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $NAME")
    }

    fun removeSong(id: Long) {
        val db = writableDatabase
        db.apply {
            beginTransaction()
            delete(NAME, "$ID = $id", null)
            setTransactionSuccessful()
            endTransaction()
            close()
        }
    }

    fun addAllSongs(songs: ArrayList<Song>) {
        SongLoader.getAllSongs(context)
                .map {
                    val database = writableDatabase;
                    database.apply {
                        val contentValues = ContentValues()
                        for (song in songs) {
                            contentValues.put(ID, song.id)
                            insert(NAME, null, contentValues)
                        }
                        setTransactionSuccessful()
                        endTransaction()
                    }
                    return@map true
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    companion object {
        const val NAME = "not_played_songs"
        const val ID = "song_id"
        const val DATABASE_NAME = "not_played.db"
        private const val VERSION = 1
        private var sInstance: NotPlayedStore? = null

        @Synchronized
        fun getInstance(context: Context): NotPlayedStore {
            if (sInstance == null) {
                sInstance = NotPlayedStore(context.applicationContext)
            }
            return sInstance!!
        }
    }
}
