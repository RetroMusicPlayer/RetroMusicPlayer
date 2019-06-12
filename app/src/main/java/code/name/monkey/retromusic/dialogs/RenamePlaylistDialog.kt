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
import android.provider.MediaStore.Audio.Playlists.Members.PLAYLIST_ID
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.R.layout
import code.name.monkey.retromusic.R.string
import code.name.monkey.retromusic.util.PlaylistsUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RenamePlaylistDialog : DialogFragment() {

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
    }*/
    private lateinit var playlistView: TextInputEditText
    private lateinit var actionNewPlaylistContainer: TextInputLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val materialDialog = MaterialDialog(activity!!, BottomSheet())
                .show {
                    title(string.rename_playlist_title)
                    customView(layout.dialog_playlist)
                    negativeButton(android.R.string.cancel)
                    positiveButton(string.action_rename) {
                        if (playlistView.toString().trim { it <= ' ' } != "") {
                            val playlistId = arguments!!.getLong(PLAYLIST_ID)
                            PlaylistsUtil.renamePlaylist(context, playlistId, playlistView.text!!.toString())
                        }
                    }
                    getActionButton(WhichButton.POSITIVE).updateTextColor(ThemeStore.accentColor(context))
                }

        val dialogView = materialDialog.getCustomView()
        playlistView = dialogView.findViewById(R.id.actionNewPlaylist)
        actionNewPlaylistContainer = dialogView.findViewById(R.id.actionNewPlaylistContainer)

        MaterialUtil.setTint(actionNewPlaylistContainer, false)

        val playlistId = arguments!!.getLong(PLAYLIST_ID)
        playlistView.setText(PlaylistsUtil.getNameForPlaylist(context!!, playlistId), TextView.BufferType.EDITABLE)
        return materialDialog
    }

    companion object {

        fun create(playlistId: Long): RenamePlaylistDialog {
            val dialog = RenamePlaylistDialog()
            val args = Bundle()
            args.putLong(PLAYLIST_ID, playlistId)
            dialog.arguments = args
            return dialog
        }
    }
}