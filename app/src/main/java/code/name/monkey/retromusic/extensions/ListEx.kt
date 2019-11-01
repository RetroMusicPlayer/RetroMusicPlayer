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

package code.name.monkey.retromusic.extensions

import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song

/**
 * Created by hemanths on 2019-11-01.
 */


fun ArrayList<Song>.lastElement(): Boolean {
    println("${this.size} ${this.indexOf(MusicPlayerRemote.currentSong)}")
    return this.size - 1 == this.indexOf(MusicPlayerRemote.currentSong)
}

fun ArrayList<Song>.fistElement(): Boolean {
    return 0 == this.indexOf(MusicPlayerRemote.currentSong)
}
