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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet


class RemoveFromPlaylistDialog : DialogFragment() {

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(code.name.monkey.retromusic.R.layout.dialog_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songs = arguments!!.getParcelableArrayList<Song>("songs")
        val content: CharSequence
        if (songs!!.size > 1) {
            content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_x_songs_from_playlist, songs.size), Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_x_songs_from_playlist, songs.size))
            }
        } else {
            content = Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_song_x_from_playlist, songs[0].title))
        }
        bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))
        bannerTitle.text = content;
        actionDelete.apply {
            setIconResource(code.name.monkey.retromusic.R.drawable.ic_delete_white_24dp)
            setText(code.name.monkey.retromusic.R.string.remove_action)
            setTextColor(ThemeStore.textColorSecondary(context))
            setOnClickListener {
                val playlistSongs = ArrayList<PlaylistSong>()
                playlistSongs.addAll(songs as ArrayList<PlaylistSong>)
                PlaylistsUtil.removeFromPlaylist(activity!!, playlistSongs)
                dismiss()
            }
            MaterialUtil.setTint(this)
        }


        actionCancel.apply {
            setIconResource(code.name.monkey.retromusic.R.drawable.ic_close_white_24dp)
            setTextColor(ThemeStore.textColorSecondary(context))
            setOnClickListener { dismiss() }
            MaterialUtil.setTint(this, false)
        }
    }
*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val songs = arguments!!.getParcelableArrayList<PlaylistSong>("songs")

        val title: Int
        val content: CharSequence
        if (songs.size > 1) {
            title = R.string.remove_songs_from_playlist_title
            content = Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_x_songs_from_playlist, songs.size))
        } else {
            title = R.string.remove_song_from_playlist_title
            content = Html.fromHtml(getString(code.name.monkey.retromusic.R.string.remove_song_x_from_playlist, songs[0].title))
        }


        return MaterialDialog(activity!!, BottomSheet()).show {
            title(title)
            message(text = content)
            negativeButton(android.R.string.cancel)
            positiveButton(R.string.remove_action) {
                if (activity == null)
                    return@positiveButton
                PlaylistsUtil.removeFromPlaylist(activity!!, songs as MutableList<PlaylistSong>)
            }
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