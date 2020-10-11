/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic

import androidx.annotation.IntDef

@IntDef(
    RECENT_ALBUMS,
    TOP_ALBUMS,
    RECENT_ARTISTS,
    TOP_ARTISTS,
    SUGGESTIONS,
    FAVOURITES,
    GENRES,
    PLAYLISTS
)
@Retention(AnnotationRetention.SOURCE)
annotation class HomeSection

const val RECENT_ALBUMS = 3
const val TOP_ALBUMS = 1
const val RECENT_ARTISTS = 2
const val TOP_ARTISTS = 0
const val SUGGESTIONS = 5
const val FAVOURITES = 4
const val GENRES = 6
const val PLAYLISTS = 7
const val HISTORY_PLAYLIST = 8
const val LAST_ADDED_PLAYLIST = 9
const val TOP_PLAYED_PLAYLIST = 10
