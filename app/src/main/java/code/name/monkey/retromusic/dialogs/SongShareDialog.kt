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
package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.EXTRA_SONG
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.ShareInstagramStory
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil

class SongShareDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val song: Song? = requireArguments().getParcelable(EXTRA_SONG)
        val listening: String =
            String.format(
                getString(R.string.currently_listening_to_x_by_x),
                song?.title,
                song?.artistName
            )
        return materialDialog(R.string.what_do_you_want_to_share)
            .setItems(
                arrayOf(
                    getString(R.string.the_audio_file),
                    "\u201C" + listening + "\u201D",
                    getString(R.string.social_stories)
                )
            ) { _, which ->
                withAction(which, song, listening)
            }
            .create()
            .colorButtons()
    }

    private fun withAction(
        which: Int,
        song: Song?,
        currentlyListening: String
    ) {
        when (which) {
            0 -> {
                startActivity(Intent.createChooser(song?.let {
                    MusicUtil.createShareSongFileIntent(
                        it,
                        requireContext()
                    )
                }, null))
            }
            1 -> {
                startActivity(
                    Intent.createChooser(
                        Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                            .setType("text/plain"),
                        null
                    )
                )
            }
            2 -> {
                if (song != null) {
                    startActivity(
                        Intent(
                            requireContext(),
                            ShareInstagramStory::class.java
                        ).putExtra(
                            ShareInstagramStory.EXTRA_SONG,
                            song
                        )
                    )
                }
            }
        }
    }

    companion object {

        fun create(song: Song): SongShareDialog {
            val dialog = SongShareDialog()
            val args = Bundle()
            args.putParcelable(EXTRA_SONG, song)
            dialog.arguments = args
            return dialog
        }
    }
}
