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

package code.name.monkey.retromusic.model

import java.util.*


class Album {
    val songs: ArrayList<Song>?

    val id: Int
        get() = safeGetFirstSong().albumId

    val title: String?
        get() = safeGetFirstSong().albumName

    val artistId: Int
        get() = safeGetFirstSong().artistId

    val artistName: String?
        get() = safeGetFirstSong().artistName

    val year: Int
        get() = safeGetFirstSong().year

    val dateModified: Long
        get() = safeGetFirstSong().dateModified

    val songCount: Int
        get() = songs!!.size

    constructor(songs: ArrayList<Song>) {
        this.songs = songs
    }

    constructor() {
        this.songs = ArrayList()
    }

    fun safeGetFirstSong(): Song {
        return if (songs!!.isEmpty()) Song.emptySong else songs[0]
    }
}
