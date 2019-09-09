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

package code.name.monkey.retromusic.helper.menu


import android.app.Activity
import android.content.Context
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.dialogs.DeletePlaylistDialog
import code.name.monkey.retromusic.dialogs.RenamePlaylistDialog
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.misc.WeakContextAsyncTask
import code.name.monkey.retromusic.model.AbsCustomPlaylist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PlaylistsUtil
import java.util.*


object PlaylistMenuHelper {

    fun handleMenuClick(activity: AppCompatActivity,
                        playlist: Playlist, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                MusicPlayerRemote.openQueue(getPlaylistSongs(activity, playlist), 9, true)
                return true
            }
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(getPlaylistSongs(activity, playlist))
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(getPlaylistSongs(activity, playlist))
                        .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(getPlaylistSongs(activity, playlist))
                return true
            }
            R.id.action_rename_playlist -> {
                RenamePlaylistDialog.create(playlist.id.toLong())
                        .show(activity.supportFragmentManager, "RENAME_PLAYLIST")
                return true
            }
            R.id.action_delete_playlist -> {
                DeletePlaylistDialog.create(playlist)
                        .show(activity.supportFragmentManager, "DELETE_PLAYLIST")
                return true
            }
            R.id.action_save_playlist -> {
                SavePlaylistAsyncTask(activity).execute(playlist)
                return true
            }
        }
        return false
    }

    private fun getPlaylistSongs(activity: Activity,
                                 playlist: Playlist): ArrayList<Song> {
        return if (playlist is AbsCustomPlaylist) {
            playlist.getSongs(activity)
        } else {
            PlaylistSongsLoader.getPlaylistSongList(activity, playlist)
        }
    }

    private class SavePlaylistAsyncTask internal constructor(context: Context) : WeakContextAsyncTask<Playlist, String, String>(context) {

        override fun doInBackground(vararg params: Playlist): String {
            return String.format(App.getContext().getString(R.string
                    .saved_playlist_to), PlaylistsUtil.savePlaylist(App.getContext(), params[0]))
        }

        override fun onPostExecute(string: String) {
            super.onPostExecute(string)
            val context = context
            if (context != null) {
                Toast.makeText(context, string, Toast.LENGTH_LONG).show()
            }
        }
    }
}
