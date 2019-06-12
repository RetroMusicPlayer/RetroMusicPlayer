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
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet


class DeleteSongsDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val songs = arguments!!.getParcelableArrayList<Song>("songs")
        val title: Int
        val content: CharSequence
        if (songs.size > 1) {
            title = R.string.delete_songs_title
            content = Html.fromHtml(getString(R.string.delete_x_songs, songs.size))
        } else {
            title = R.string.delete_song_title
            content = Html.fromHtml(getString(R.string.delete_song_x, songs.get(0).title))
        }
        return MaterialDialog(activity!!, BottomSheet()).show {
            title(title)
            message(text = content)
            negativeButton(android.R.string.cancel)
            positiveButton(R.string.action_delete) {
                if (activity == null)
                    return@positiveButton
                MusicUtil.deleteTracks(activity!!, songs);
            }
        }
    }


    companion object {

        fun create(song: Song): DeleteSongsDialog {
            val list = ArrayList<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: ArrayList<Song>): DeleteSongsDialog {
            val dialog = DeleteSongsDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}

