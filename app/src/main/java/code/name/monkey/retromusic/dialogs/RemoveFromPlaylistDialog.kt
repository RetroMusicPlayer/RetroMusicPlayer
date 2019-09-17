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
import code.name.monkey.retromusic.model.PlaylistSong
import code.name.monkey.retromusic.util.PlaylistsUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet


class RemoveFromPlaylistDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val songs = arguments!!.getParcelableArrayList<PlaylistSong>("songs")

        var title = 0
        var content: CharSequence = ""
        if (songs != null) {
            if (songs.size > 1) {
                title = R.string.remove_songs_from_playlist_title
                content = Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_x_songs_from_playlist, songs.size))
            } else {
                title = R.string.remove_song_from_playlist_title
                content = Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_song_x_from_playlist, songs[0].title))
            }
        }


        return MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))
                .show {
                    title(title)
                    message(text = content)
                    negativeButton(android.R.string.cancel)
                    positiveButton(R.string.remove_action) {
                        if (activity == null)
                            return@positiveButton
                        PlaylistsUtil.removeFromPlaylist(activity!!, songs as MutableList<PlaylistSong>)
                    }
                    cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
                }

    }

    companion object {

        fun create(song: PlaylistSong): RemoveFromPlaylistDialog {
            val list = ArrayList<PlaylistSong>()
            list.add(song)
            return create(list)
        }

        fun create(songs: ArrayList<PlaylistSong>): RemoveFromPlaylistDialog {
            val dialog = RemoveFromPlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}