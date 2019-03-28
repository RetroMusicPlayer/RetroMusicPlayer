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

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PlaylistsUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_playlist.*

class RenamePlaylistDialog : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))
        bannerTitle.setText(R.string.rename_playlist_title)

        MaterialUtil.setTint(actionNewPlaylistContainer, false)
        val accentColor = ThemeStore.accentColor(context!!)
        actionNewPlaylist.setHintTextColor(ColorStateList.valueOf(accentColor))
        actionNewPlaylist.setTextColor(ThemeStore.textColorPrimary(context!!))

        actionNewPlaylist.apply {
            var playlistId: Long = 0
            if (arguments != null) {
                playlistId = arguments!!.getLong("playlist_id")
            }
            setText(PlaylistsUtil.getNameForPlaylist(activity!!, playlistId))
        }

        actionCancel.apply {
            MaterialUtil.setTint(this, false)
            setOnClickListener { dismiss() }
            icon = ContextCompat.getDrawable(context, R.drawable.ic_close_white_24dp)
        }

        actionCreate.apply {
            setText(R.string.action_rename)
            setOnClickListener {
                if (actionNewPlaylist.toString().trim { it <= ' ' } != "") {
                    val playlistId = arguments!!.getLong("playlist_id")
                    PlaylistsUtil.renamePlaylist(context!!, playlistId, actionNewPlaylist.text!!.toString())
                }
            }
            MaterialUtil.setTint(this)
            icon = ContextCompat.getDrawable(context, R.drawable.ic_edit_white_24dp)
        }
    }

    companion object {

        fun create(playlistId: Long): RenamePlaylistDialog {
            val dialog = RenamePlaylistDialog()
            val args = Bundle()
            args.putLong("playlist_id", playlistId)
            dialog.arguments = args
            return dialog
        }
    }
}