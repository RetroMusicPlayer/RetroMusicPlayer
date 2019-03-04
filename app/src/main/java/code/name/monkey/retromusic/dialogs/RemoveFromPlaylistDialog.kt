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

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.PlaylistSong
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PlaylistsUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_remove_from_playlist.*

class RemoveFromPlaylistDialog : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_remove_from_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songs = arguments!!.getParcelableArrayList<Song>("songs")
        val title: Int
        val content: CharSequence
        if (songs!!.size > 1) {
            title = R.string.remove_songs_from_playlist_title
            content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(getString(R.string.remove_x_songs_from_playlist, songs.size), Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(getString(R.string.remove_x_songs_from_playlist, songs.size))
            }
        } else {
            title = R.string.remove_song_from_playlist_title
            content = Html.fromHtml(getString(R.string.remove_song_x_from_playlist, songs[0].title))
        }
        actionDelete.apply {
            text = content
            setTextColor(ThemeStore.textColorSecondary(context))
            setOnClickListener {
                PlaylistsUtil.removeFromPlaylist(activity!!, songs as ArrayList<PlaylistSong>)
                dismiss()
            }
        }
        bannerTitle.apply {
            setText(title)
            setTextColor(ThemeStore.textColorPrimary(context))
        }

        actionCancel.apply {
            setTextColor(ThemeStore.textColorSecondary(context))
            setOnClickListener { dismiss() }
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