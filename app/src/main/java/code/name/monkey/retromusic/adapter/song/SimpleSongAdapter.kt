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
package code.name.monkey.retromusic.adapter.song

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil

/**
 * Adapter for displaying songs in a simplified list format with track numbers.
 * 
 * This adapter is primarily used in album detail views where track numbering
 * is essential for user experience. It extends the base SongAdapter with
 * enhanced track number display logic and duration formatting.
 * 
 * Key features:
 * - Intelligent track numbering with metadata and position-based fallbacks
 * - Consistent duration formatting
 * - Optimized for album/playlist contexts
 */
class SimpleSongAdapter(
    context: FragmentActivity,
    songs: ArrayList<Song>,
    layoutRes: Int
) : SongAdapter(context, songs, layoutRes) {

    /**
     * Updates the adapter's dataset with new songs.
     * 
     * @param dataSet New list of songs to display
     */
    override fun swapDataSet(dataSet: List<Song>) {
        this.dataSet = dataSet.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    /**
     * Binds song data to the view holder with enhanced track number display.
     * 
     * This method ensures consistent track numbering across different scenarios:
     * - Uses embedded metadata when available
     * - Falls back to position-based numbering for missing metadata
     * - Handles single-song albums appropriately
     * 
     * @param holder The ViewHolder to bind data to
     * @param position The position of the item in the dataset
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        
        val song = dataSet[position]
        
        // Set track number using centralized logic for consistency
        holder.imageText?.text = MusicUtil.getDisplayTrackNumber(
            song = song,
            position = position,
            totalSongs = dataSet.size
        )
        
        // Format and display song duration
        holder.time?.text = MusicUtil.getReadableDurationString(song.duration)
    }

    override fun getItemCount(): Int = dataSet.size
}
