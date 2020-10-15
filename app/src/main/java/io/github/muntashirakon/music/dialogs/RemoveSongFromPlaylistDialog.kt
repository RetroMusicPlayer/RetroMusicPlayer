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
package io.github.muntashirakon.music.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import io.github.muntashirakon.music.EXTRA_SONG
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.db.SongEntity
import io.github.muntashirakon.music.extensions.colorButtons
import io.github.muntashirakon.music.extensions.extraNotNull
import io.github.muntashirakon.music.extensions.materialDialog
import io.github.muntashirakon.music.fragments.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RemoveSongFromPlaylistDialog : DialogFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {
        fun create(song: SongEntity): RemoveSongFromPlaylistDialog {
            val list = mutableListOf<SongEntity>()
            list.add(song)
            return create(list)
        }

        fun create(songs: List<SongEntity>): RemoveSongFromPlaylistDialog {
            return RemoveSongFromPlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_SONG to songs
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val songs = extraNotNull<List<SongEntity>>(EXTRA_SONG).value
        val pair = if (songs.size > 1) {
            Pair(
                R.string.remove_songs_from_playlist_title,
                HtmlCompat.fromHtml(
                    String.format(getString(R.string.remove_x_songs_from_playlist), songs.size),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
        } else {
            Pair(
                R.string.remove_song_from_playlist_title,
                HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.remove_song_x_from_playlist),
                        songs[0].title
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
        }
        return materialDialog(pair.first)
            .setMessage(pair.second)
            .setPositiveButton(R.string.remove_action) { _, _ ->
                libraryViewModel.deleteSongsInPlaylist(songs)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .colorButtons()
    }
}
