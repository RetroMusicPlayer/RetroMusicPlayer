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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.loaders.PlaylistLoader
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.playlist.AddToPlaylist
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_to_playlist.*


class AddToPlaylistDialog : RoundedBottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_add_to_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songs = arguments!!.getParcelableArrayList<Song>("songs")

        actionAddPlaylist.setOnClickListener {
            CreatePlaylistDialog.create(songs!!).show(activity!!.supportFragmentManager, "ADD_TO_PLAYLIST")
            dismiss()
        }

        bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))
        val playlists = PlaylistLoader.getAllPlaylists(activity!!).blockingFirst()
        val playlistAdapter = AddToPlaylist(activity!!, playlists, R.layout.item_playlist, songs!!, dialog)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = playlistAdapter
        }
    }

    companion object {

        fun create(song: Song): AddToPlaylistDialog {
            val list = ArrayList<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: ArrayList<Song>): AddToPlaylistDialog {
            val dialog = AddToPlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}