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
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.R.menu
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.db.toSongs
import code.name.monkey.retromusic.dialogs.RemoveSongFromPlaylistDialog
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.model.PlaylistSong
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.ViewUtil
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags

class OrderablePlaylistSongAdapter(
    private val playlist: PlaylistEntity,
    activity: FragmentActivity,
    dataSet: ArrayList<Song>,
    itemLayoutRes: Int,
    ICabHolder: ICabHolder?,
    private val onMoveItemListener: OnMoveItemListener?
) : SongAdapter(
    activity,
    dataSet,
    itemLayoutRes,
    ICabHolder
), DraggableItemAdapter<OrderablePlaylistSongAdapter.ViewHolder> {

    init {
        setMultiSelectMenuRes(menu.menu_playlists_songs_selection)
    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        var positionFinal = position
        positionFinal--

        var long: Long = 0
        if (positionFinal < 0) {
            long = -2
        } else {
            if (dataSet[positionFinal] is PlaylistSong) {
                long = (dataSet[positionFinal] as PlaylistSong).idInPlayList.toLong()
            }
        }
        return long
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: List<Song>) {
        when (menuItem.itemId) {
            R.id.action_remove_from_playlist -> {
                RemoveSongFromPlaylistDialog.create(selection.toSongs(playlist.playListId))
                    .show(activity.supportFragmentManager, "REMOVE_FROM_PLAYLIST")
                return
            }
        }
        super.onMultipleItemAction(menuItem, selection)
    }

    override fun onCheckCanStartDrag(holder: ViewHolder, position: Int, x: Int, y: Int): Boolean {
        return onMoveItemListener != null && position > 0 && (ViewUtil.hitTest(
            holder.dragView!!, x, y
        ) || ViewUtil.hitTest(holder.image!!, x, y))
    }

    override fun onGetItemDraggableRange(holder: ViewHolder, position: Int): ItemDraggableRange {
        return ItemDraggableRange(1, dataSet.size)
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        if (onMoveItemListener != null && fromPosition != toPosition) {
            onMoveItemListener.onMoveItem(fromPosition - 1, toPosition - 1)
        }
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return dropPosition > 0
    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        notifyDataSetChanged()
    }

    interface OnMoveItemListener {
        fun onMoveItem(fromPosition: Int, toPosition: Int)
    }

    inner class ViewHolder(itemView: View) : SongAdapter.ViewHolder(itemView),
        DraggableItemViewHolder {
        @DraggableItemStateFlags
        private var mDragStateFlags: Int = 0

        override var songMenuRes: Int
            get() = R.menu.menu_item_playlist_song
            set(value) {
                super.songMenuRes = value
            }

        init {
            if (dragView != null) {
                if (onMoveItemListener != null) {
                    dragView?.visibility = View.VISIBLE
                } else {
                    dragView?.visibility = View.GONE
                }
            }
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

        @DraggableItemStateFlags
        override fun getDragStateFlags(): Int {
            return mDragStateFlags
        }

        override fun setDragStateFlags(@DraggableItemStateFlags flags: Int) {
            mDragStateFlags = flags
        }
    }
}
