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

package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.loaders.PlaylistLoader
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PlaylistsUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class AddToPlaylistDialog : DialogFragment() {

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog {
        val cntx = requireContext()
        val playlists = PlaylistLoader.getAllPlaylists(cntx).blockingFirst()
        val playlistNames = arrayOfNulls<CharSequence>(playlists.size + 1)
        playlistNames[0] = cntx.resources.getString(code.name.monkey.retromusic.R.string.action_new_playlist)
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.add_playlist_title)
                .setItems(playlistNames) { dialog, which ->
                    val songs = arguments!!.getParcelableArrayList<Song>("songs") ?: return@setItems
                    if (which == 0) {
                        dialog.dismiss()
                        activity?.supportFragmentManager?.let { CreatePlaylistDialog.create(songs).show(it, "ADD_TO_PLAYLIST") }
                    } else {
                        dialog.dismiss()
                        PlaylistsUtil.addToPlaylist(cntx, songs, playlists[which - 1].id, true)
                    }
                }
                .create()
    }

    companion object {

        fun create(song: Song): AddToPlaylistDialog {
            val list = ArrayList<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: ArrayList<Song>): AddToPlaylistDialog {
            val dialog = AddToPlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}