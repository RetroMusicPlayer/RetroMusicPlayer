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
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems


class SongShareDialog : DialogFragment() {
    /* override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.dialog_file_share, container, false)
     }

     @SuppressLint("StringFormatInvalid")
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         val song: Song = arguments!!.getParcelable("song") ?: return

         dialogTitle.setTextColor(ThemeStore.textColorPrimary(context!!))
         audioText.apply {
             text = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
             setTextColor(ThemeStore.textColorSecondary(context!!))
             setOnClickListener {
                 val currentlyListening = getString(code.name.monkey.retromusic.R.string.currently_listening_to_x_by_x, song.title, song.artistName)
                 activity!!.startActivity(Intent.createChooser(Intent().setAction(Intent.ACTION_SEND)
                         .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                         .setType("text/plain"), null))
                 dismiss()
             }
             icon = ContextCompat.getDrawable(context, code.name.monkey.retromusic.R.drawable.ic_text_fields_black_24dp)
             MaterialUtil.setTint(this)
         }

         audioFile.apply {
             setTextColor(ThemeStore.textColorSecondary(context!!))
             setOnClickListener {
                 activity!!.startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(song, context), null))
                 dismiss()
             }
             icon = ContextCompat.getDrawable(context, code.name.monkey.retromusic.R.drawable.ic_share_white_24dp)
             MaterialUtil.setTint(this, false)
         }

     }*/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val song: Song? = arguments!!.getParcelable("song")
        val currentlyListening: String = getString(R.string.currently_listening_to_x_by_x, song?.title, song?.artistName)

        return MaterialDialog(activity!!, BottomSheet())
                .title(R.string.what_do_you_want_to_share)
                .show {
                    listItems(items = listOf(getString(code.name.monkey.retromusic.R.string.the_audio_file), "\u201C" + currentlyListening + "\u201D")) { dialog, index, text ->
                        when (index) {
                            0 -> {
                                startActivity(Intent.createChooser(song?.let { MusicUtil.createShareSongFileIntent(it, context) }, null))
                            }
                            1 -> {
                                activity!!.startActivity(
                                        Intent.createChooser(
                                                Intent()
                                                        .setAction(Intent.ACTION_SEND)
                                                        .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                                                        .setType("text/plain"),
                                                null
                                        )
                                )
                            }
                        }
                    }
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
