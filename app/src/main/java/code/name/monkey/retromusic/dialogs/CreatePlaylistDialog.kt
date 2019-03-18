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

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PlaylistsUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_playlist.*

import java.util.*

class CreatePlaylistDialog : RoundedBottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val accentColor = ThemeStore.accentColor(Objects.requireNonNull<Context>(context))

        val songs = arguments!!.getParcelableArrayList<Song>("songs")

        MaterialUtil.setTint(actionCreate, true)
        MaterialUtil.setTint(actionCancel, false)
        MaterialUtil.setTint(actionNewPlaylistContainer, false)

        actionNewPlaylist.setHintTextColor(ColorStateList.valueOf(accentColor))
        actionNewPlaylist.setTextColor(ThemeStore.textColorPrimary(context!!))
        bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))


        actionCancel.setOnClickListener { dismiss() }
        actionCreate.setOnClickListener {
            if (activity == null) {
                return@setOnClickListener
            }
            if (!actionNewPlaylist!!.text!!.toString().trim { it <= ' ' }.isEmpty()) {
                val playlistId = PlaylistsUtil
                        .createPlaylist(activity!!, actionNewPlaylist!!.text!!.toString())
                if (playlistId != -1 && activity != null) {
                    if (songs != null) {
                        PlaylistsUtil.addToPlaylist(activity!!, songs, playlistId, true)
                    }
                }
            }
            dismiss()
        }
    }

    companion object {

        @JvmOverloads
        fun create(song: Song? = null): CreatePlaylistDialog {
            val list = ArrayList<Song>()
            if (song != null) {
                list.add(song)
            }
            return create(list)
        }

        fun create(songs: ArrayList<Song>): CreatePlaylistDialog {
            val dialog = CreatePlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}