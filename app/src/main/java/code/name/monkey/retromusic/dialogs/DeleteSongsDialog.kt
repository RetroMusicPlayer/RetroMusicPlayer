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
import android.net.Uri
import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.EXTRA_SONG
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.saf.SAFGuideActivity
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.SAFUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteSongsDialog : DialogFragment() {
    @JvmField
    var currentSong: Song? = null

    @JvmField
    var songsToRemove: List<Song>? = null

    private var deleteSongsAsyncTask: DeleteSongsAsyncTask? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val songs = extraNotNull<List<Song>>(EXTRA_SONG).value
        var title = 0
        var message: CharSequence = ""
        if (songs.size > 1) {
            title = R.string.delete_songs_title
            message = HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_x_songs), songs.size),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            title = R.string.delete_song_title
            message = HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_song_x), songs[0].title),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        return MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                if ((songs.size == 1) && MusicPlayerRemote.isPlaying(songs[0])) {
                    MusicPlayerRemote.playNextSong()
                }
                songsToRemove = songs
                deleteSongsAsyncTask = DeleteSongsAsyncTask(this@DeleteSongsDialog)
                deleteSongsAsyncTask?.execute(DeleteSongsAsyncTask.LoadingInfo(songs, null))
            }
            .create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SAFGuideActivity.REQUEST_CODE_SAF_GUIDE -> {
                SAFUtil.openTreePicker(this)
            }
            SAFUtil.REQUEST_SAF_PICK_TREE,
            SAFUtil.REQUEST_SAF_PICK_FILE -> {
                if (deleteSongsAsyncTask != null) {
                    deleteSongsAsyncTask?.cancel(true)
                }
                deleteSongsAsyncTask = DeleteSongsAsyncTask(this)
                deleteSongsAsyncTask?.execute(
                    DeleteSongsAsyncTask.LoadingInfo(
                        requestCode,
                        resultCode,
                        data
                    )
                )
            }
        }
    }

    fun deleteSongs(songs: List<Song>, safUris: List<Uri>?) {
        MusicUtil.deleteTracks(requireActivity(), songs, safUris) { this.dismiss() }
    }

    companion object {

        fun create(song: Song): DeleteSongsDialog {
            val list = ArrayList<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: List<Song>): DeleteSongsDialog {
            val dialog = DeleteSongsDialog()
            val args = Bundle()
            args.putParcelableArrayList(EXTRA_SONG, ArrayList(songs))
            dialog.arguments = args
            return dialog
        }
    }
}

