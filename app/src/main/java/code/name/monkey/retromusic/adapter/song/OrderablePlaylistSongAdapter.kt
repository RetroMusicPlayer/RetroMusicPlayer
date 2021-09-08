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

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.db.toSongsEntity
import code.name.monkey.retromusic.dialogs.RemoveSongFromPlaylistDialog
import code.name.monkey.retromusic.extensions.applyColor
import code.name.monkey.retromusic.extensions.applyOutlineColor
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.model.Song
import com.google.android.material.button.MaterialButton
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderablePlaylistSongAdapter(
    private val playlist: PlaylistEntity,
    activity: FragmentActivity,
    dataSet: MutableList<Song>,
    itemLayoutRes: Int,
    ICabHolder: ICabHolder?,
) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, ICabHolder),
    DraggableItemAdapter<OrderablePlaylistSongAdapter.ViewHolder> {

    val libraryViewModel: LibraryViewModel by activity.viewModel()
    val tempDataSet = dataSet

    init {
        this.setHasStableIds(true)
        this.setMultiSelectMenuRes(R.menu.menu_playlists_songs_selection)
    }

    override fun getItemId(position: Int): Long {
        // requires static value, it means need to keep the same value
        // even if the item position has been changed.
        return if (position != 0) {
            dataSet[position - 1].id
        } else {
            -1
        }


    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) OFFSET_ITEM else SONG
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        if (holder.itemViewType == OFFSET_ITEM) {
            val color = ThemeStore.accentColor(activity)
            val viewHolder = holder as ViewHolder
            viewHolder.playAction?.let {
                it.setOnClickListener {
                    MusicPlayerRemote.openQueue(dataSet, 0, true)
                }
                it.applyOutlineColor(color)
            }
            viewHolder.shuffleAction?.let {
                it.setOnClickListener {
                    MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                }
                it.applyColor(color)
            }
        } else {
            super.onBindViewHolder(holder, position - 1)
        }
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: List<Song>) {
        when (menuItem.itemId) {
            R.id.action_remove_from_playlist -> RemoveSongFromPlaylistDialog.create(
                selection.toSongsEntity(
                    playlist
                )
            )
                .show(activity.supportFragmentManager, "REMOVE_FROM_PLAYLIST")
            else -> super.onMultipleItemAction(menuItem, selection)
        }
    }

    inner class ViewHolder(itemView: View) : AbsOffsetSongAdapter.ViewHolder(itemView) {
        val playAction: MaterialButton? = itemView.findViewById(R.id.playAction)
        val shuffleAction: MaterialButton? = itemView.findViewById(R.id.shuffleAction)

        override var songMenuRes: Int
            get() = R.menu.menu_item_playlist_song
            set(value) {
                super.songMenuRes = value
            }

        override fun onSongMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_remove_from_playlist -> {
                    RemoveSongFromPlaylistDialog.create(song.toSongEntity(playlist.playListId))
                        .show(activity.supportFragmentManager, "REMOVE_FROM_PLAYLIST")
                    return true
                }
            }
            return super.onSongMenuItemClick(item)
        }

        init {
            dragView?.visibility = View.VISIBLE
        }

        override fun onClick(v: View?) {
            if (itemViewType == OFFSET_ITEM) {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                return
            }
            super.onClick(v)
        }
    }

    override fun onCheckCanStartDrag(holder: ViewHolder, position: Int, x: Int, y: Int): Boolean {
        if (dataSet.size == 0 or 1) {
            return false
        }
        val dragHandle = holder.dragView ?: return false

        val handleWidth = dragHandle.width
        val handleHeight = dragHandle.height
        val handleLeft = dragHandle.left
        val handleTop = dragHandle.top

        return (x >= handleLeft && x < handleLeft + handleWidth &&
                y >= handleTop && y < handleTop + handleHeight) && position != 0
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        dataSet.add(toPosition - 1, dataSet.removeAt(fromPosition - 1))
    }


    override fun onGetItemDraggableRange(holder: ViewHolder, position: Int): ItemDraggableRange {
        return ItemDraggableRange(1, itemCount - 1)
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return true
    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        notifyDataSetChanged()
    }

    fun saveSongs(playlistEntity: PlaylistEntity) {
        activity.lifecycleScope.launch(Dispatchers.IO) {
            libraryViewModel.insertSongs(dataSet.toSongsEntity(playlistEntity))
        }
    }
}
