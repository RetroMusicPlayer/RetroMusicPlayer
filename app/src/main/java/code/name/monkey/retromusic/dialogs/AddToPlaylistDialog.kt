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
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems


class AddToPlaylistDialog : DialogFragment() {

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog {
        val cntx = requireContext()
        val playlists = PlaylistLoader.getAllPlaylists(cntx)
        val playlistNames: MutableList<String> = mutableListOf()
        playlistNames.add(cntx.resources.getString(R.string.action_new_playlist))
        for (p in playlists) {
            playlistNames.add(p.name)
        }

        return MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.add_playlist_title)
            cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
            listItems(items = playlistNames) { dialog, index, _ ->
                val songs = arguments!!.getParcelableArrayList<Song>("songs") ?: return@listItems
                if (index == 0) {
                    dialog.dismiss()
                    activity?.supportFragmentManager?.let { CreatePlaylistDialog.create(songs).show(it, "ADD_TO_PLAYLIST") }
                } else {
                    dialog.dismiss()
                    PlaylistsUtil.addToPlaylist(cntx, songs, playlists[index - 1].id, true)
                }
            }
        }
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