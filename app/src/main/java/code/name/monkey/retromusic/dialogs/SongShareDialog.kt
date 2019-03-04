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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_file_share.*


class SongShareDialog : RoundedBottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_file_share, container, false)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val song = arguments!!.getParcelable<Song>("song")
        dialogTitle.setTextColor(ThemeStore.textColorPrimary(context!!))

        audioText.apply {
            text = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
            setTextColor(ThemeStore.textColorSecondary(context!!))
            setOnClickListener {
                val currentlyListening = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
                activity!!.startActivity(Intent.createChooser(Intent().setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                        .setType("text/plain"), null))
                dismiss()
            }
            icon = ContextCompat.getDrawable(context, R.drawable.ic_text_fields_black_24dp)
            MaterialUtil.setTint(this)
        }

        audioFile.apply {
            setTextColor(ThemeStore.textColorSecondary(context!!))
            setOnClickListener {
                activity!!.startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(song, context), null))
                dismiss()
            }
            icon = ContextCompat.getDrawable(context, R.drawable.ic_share_white_24dp)
            MaterialUtil.setTint(this, false)
        }

    }

    companion object {

        fun create(song: Song): SongShareDialog {
            val dialog = SongShareDialog()
            val args = Bundle()
            args.putParcelable("song", song)
            dialog.arguments = args
            return dialog
        }
    }
}
