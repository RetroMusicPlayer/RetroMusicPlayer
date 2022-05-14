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
package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.glide.AlbumGlideRequest
import code.name.monkey.retromusic.glide.ArtistGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.menu.SongMenuHelper
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist
import code.name.monkey.retromusic.repository.PlaylistSongsLoader
import code.name.monkey.retromusic.util.MusicUtil
import com.bumptech.glide.Glide
import java.util.*

class SearchAdapter(
    private val activity: FragmentActivity,
    private var dataSet: List<Any>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    fun swapDataSet(dataSet: List<Any>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet[position] is Album) return ALBUM
        if (dataSet[position] is Artist) return ARTIST
        if (dataSet[position] is Genre) return GENRE
        if (dataSet[position] is PlaylistEntity) return PLAYLIST
        return if (dataSet[position] is Song) SONG else HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == HEADER) ViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.sub_header,
                parent,
                false
            ), viewType
        )
        else
            ViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.item_list, parent, false),
                viewType
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ALBUM -> {
                holder.imageTextContainer?.isVisible = true
                val album = dataSet[position] as Album
                holder.title?.text = album.title
                holder.text?.text = album.artistName
                AlbumGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
                    .checkIgnoreMediaStore().build().into(holder.image)
            }
            ARTIST -> {
                holder.imageTextContainer?.isVisible = true
                val artist = dataSet[position] as Artist
                holder.title?.text = artist.name
                holder.text?.text = MusicUtil.getArtistInfoString(activity, artist)
                ArtistGlideRequest.Builder.from(Glide.with(activity), artist).build()
                    .into(holder.image)
            }
            SONG -> {
                val song = dataSet[position] as Song
                holder.title?.text = song.title
                holder.text?.text = song.albumName
            }
            GENRE -> {
                val genre = dataSet[position] as Genre
                holder.title?.text = genre.name
                holder.text?.text = String.format(
                    Locale.getDefault(),
                    "%d %s",
                    genre.songCount,
                    if (genre.songCount > 1) activity.getString(R.string.songs) else activity.getString(
                        R.string.song
                    )
                )
            }
            PLAYLIST -> {
                val playlist = dataSet[position] as PlaylistEntity
                holder.title?.text = playlist.playlistName
                //holder.text?.text = MusicUtil.playlistInfoString(activity, playlist.songs)
            }
            else -> {
                holder.title?.text = dataSet[position].toString()
                holder.title?.setTextColor(ThemeStore.accentColor(activity))
            }
        }
    }

    private fun getSongs(playlist: Playlist): List<Song> {
        val songs = mutableListOf<Song>()
        if (playlist is AbsSmartPlaylist) {
            songs.addAll(playlist.getSongs())
        } else {
            songs.addAll(PlaylistSongsLoader.getPlaylistSongList(activity, playlist.id))
        }
        return songs
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View, itemViewType: Int) : MediaEntryViewHolder(itemView) {
        init {
            itemView.setOnLongClickListener(null)
            imageTextContainer?.isInvisible = true
            if (itemViewType == SONG) {
                imageTextContainer?.isGone = true
                menu?.visibility = View.VISIBLE
                menu?.setOnClickListener(object : SongMenuHelper.OnClickSongMenu(activity) {
                    override val song: Song
                        get() = dataSet[layoutPosition] as Song
                })
            } else {
                menu?.visibility = View.GONE
            }

            when (itemViewType) {
                ALBUM -> setImageTransitionName(activity.getString(R.string.transition_album_art))
                ARTIST -> setImageTransitionName(activity.getString(R.string.transition_artist_image))
                else -> {
                    val container = itemView.findViewById<View>(R.id.imageContainer)
                    container?.visibility = View.GONE
                }
            }
        }

        override fun onClick(v: View?) {
            val item = dataSet[layoutPosition]
            when (itemViewType) {
                ALBUM -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.albumDetailsFragment,
                        bundleOf(EXTRA_ALBUM_ID to (item as Album).id)
                    )
                }
                ARTIST -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.artistDetailsFragment,
                        bundleOf(EXTRA_ARTIST_ID to (item as Artist).id)
                    )
                }
                GENRE -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.genreDetailsFragment,
                        bundleOf(EXTRA_GENRE to (item as Genre))
                    )
                }
                PLAYLIST -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.playlistDetailsFragment,
                        bundleOf(EXTRA_PLAYLIST to (item as PlaylistWithSongs))
                    )
                }
                SONG -> {
                    val playList = mutableListOf<Song>()
                    playList.add(item as Song)
                    MusicPlayerRemote.openQueue(playList, 0, true)
                }
            }
        }
    }

    companion object {
        private const val HEADER = 0
        private const val ALBUM = 1
        private const val ARTIST = 2
        private const val SONG = 3
        private const val GENRE = 4
        private const val PLAYLIST = 5
    }
}
