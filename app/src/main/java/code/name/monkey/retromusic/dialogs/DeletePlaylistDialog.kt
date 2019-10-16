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
import android.text.Html
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.R.string
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.util.PlaylistsUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import java.util.*


class DeletePlaylistDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlists = arguments!!.getParcelableArrayList<Playlist>("playlist")
        val title: Int
        val content: CharSequence
        //noinspection ConstantConditions
        if (playlists!!.size > 1) {
            title = string.delete_playlists_title
            content = Html.fromHtml(getString(string.delete_x_playlists, playlists.size))
        } else {
            title = string.delete_playlist_title
            content = Html.fromHtml(getString(string.delete_playlist_x, playlists[0].name))
        }

        return MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))
                .show {
                    cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
                    title(title)
                    message(text = content)
                    negativeButton(android.R.string.cancel)
                    positiveButton(R.string.action_delete) {
                        if (activity == null)
                            return@positiveButton
                        PlaylistsUtil.deletePlaylists(activity!!, playlists)
                    }
                    negativeButton(android.R.string.cancel)
                }
    }

    companion object {

        fun create(playlist: Playlist): DeletePlaylistDialog {
            val list = ArrayList<Playlist>()
            list.add(playlist)
            return create(list)
        }

        fun create(playlist: ArrayList<Playlist>): DeletePlaylistDialog {
            val dialog = DeletePlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("playlist", playlist)
            dialog.arguments = args
            return dialog
        }
    }

}