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

package io.github.muntashirakon.music.helper.menu

import android.app.Activity
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.dialogs.AddToPlaylistDialog
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.loaders.GenreLoader
import io.github.muntashirakon.music.model.Genre
import io.github.muntashirakon.music.model.Song
import java.util.*

object GenreMenuHelper {
    fun handleMenuClick(activity: AppCompatActivity, genre: Genre, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                MusicPlayerRemote.openQueue(getGenreSongs(activity, genre), 0, true)
                return true
            }
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(getGenreSongs(activity, genre))
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(getGenreSongs(activity, genre))
                    .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(getGenreSongs(activity, genre))
                return true
            }
        }
        return false
    }

    private fun getGenreSongs(activity: Activity, genre: Genre): ArrayList<Song> {
        return GenreLoader.getSongs(activity, genre.id)
    }
}
