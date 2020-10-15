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
package io.github.muntashirakon.music.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import io.github.muntashirakon.music.EXTRA_PLAYLIST
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.db.PlaylistEntity
import io.github.muntashirakon.music.extensions.colorButtons
import io.github.muntashirakon.music.extensions.extraNotNull
import io.github.muntashirakon.music.extensions.materialDialog
import io.github.muntashirakon.music.fragments.LibraryViewModel
import io.github.muntashirakon.music.fragments.ReloadType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeletePlaylistDialog : DialogFragment() {

    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {

        fun create(playlist: PlaylistEntity): DeletePlaylistDialog {
            val list = mutableListOf<PlaylistEntity>()
            list.add(playlist)
            return create(list)
        }

        fun create(playlists: List<PlaylistEntity>): DeletePlaylistDialog {
            return DeletePlaylistDialog().apply {
                arguments = bundleOf(EXTRA_PLAYLIST to playlists)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlists = extraNotNull<List<PlaylistEntity>>(EXTRA_PLAYLIST).value
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
                String.format(getString(R.string.delete_playlist_x), playlists[0].playlistName),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        return materialDialog(title)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                libraryViewModel.deleteSongsFromPlaylist(playlists)
                libraryViewModel.deleteRoomPlaylist(playlists)
                libraryViewModel.forceReload(ReloadType.Playlists)
            }
            .create()
            .colorButtons()
    }
}
