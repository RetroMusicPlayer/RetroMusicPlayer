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
package code.name.monkey.retromusic.adapter.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.util.MusicUtil

class LegacyPlaylistAdapter(
    private val activity: FragmentActivity,
    private var list: List<Playlist>,
    private val layoutRes: Int,
    private val playlistClickListener: PlaylistClickListener
) :
    RecyclerView.Adapter<LegacyPlaylistAdapter.ViewHolder>() {

    fun swapData(list: List<Playlist>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist: Playlist = list[position]
        holder.title?.text = playlist.name
        holder.text?.text = MusicUtil.getPlaylistInfoString(activity, playlist.getSongs())
        holder.itemView.setOnClickListener {
            playlistClickListener.onPlaylistClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}
