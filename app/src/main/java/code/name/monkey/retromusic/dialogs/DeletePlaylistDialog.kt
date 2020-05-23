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
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.EXTRA_PLAYLIST
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.util.PlaylistsUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeletePlaylistDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlists = extraNotNull<List<Playlist>>(EXTRA_PLAYLIST).value
        val title: Int
        val message: CharSequence
        //noinspection ConstantConditions
        if (playlists.size > 1) {
            title = R.string.delete_playlists_title
            message = HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_x_playlists), playlists.size),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            title = R.string.delete_playlist_title
            message = HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_playlist_x), playlists[0].name),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        return MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                PlaylistsUtil.deletePlaylists(requireContext(), playlists)
            }
            .create()
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
            args.putParcelableArrayList(EXTRA_PLAYLIST, playlist)
            dialog.arguments = args
            return dialog
        }
    }
}