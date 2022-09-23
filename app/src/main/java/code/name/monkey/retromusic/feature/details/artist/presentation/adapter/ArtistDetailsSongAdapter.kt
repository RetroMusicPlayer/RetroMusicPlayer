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
package code.name.monkey.retromusic.feature.details.artist.presentation.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import code.name.monkey.retromusic.EXTRA_ALBUM_ID
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.feature.details.artist.presentation.entity.ArtistDetailsSongUi
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.helper.menu.SongMenuHelper
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import me.zhanghai.android.fastscroll.PopupTextProvider

/**
 * Created by hemanths on 13/08/17.
 */

open class ArtistDetailsSongAdapter(
    override val activity: FragmentActivity,
    var dataSet: MutableList<ArtistDetailsSongUi>,
    protected var itemLayoutRes: Int,
    showSectionName: Boolean = true
) : AbsMultiSelectAdapter<ArtistDetailsSongAdapter.ViewHolder, ArtistDetailsSongUi>(activity, R.menu.menu_media_selection), PopupTextProvider {

    private var showSectionName = true

    init {
        this.showSectionName = showSectionName
        this.setHasStableIds(true)
    }

    fun swapDataSet(dataSet: List<ArtistDetailsSongUi>) {
        this.dataSet = dataSet.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = dataSet[position]
        val isChecked = isChecked(song)
        holder.itemView.isActivated = isChecked
        holder.menu?.isGone = isChecked
        holder.title?.text = song.title
        holder.text?.text = song.artist
        holder.text2?.text = song.artist
        loadAlbumCover(song, holder)
        val landscape = RetroUtil.isLandscape
        if ((PreferenceUtil.songGridSize > 2 && !landscape) || (PreferenceUtil.songGridSizeLand > 5 && landscape)) {
            holder.menu?.isVisible = false
        }

        val fixedTrackNumber = MusicUtil.getFixedTrackNumber(dataSet[position].track)

        holder.imageText?.text = if (fixedTrackNumber > 0) fixedTrackNumber.toString() else "-"
        holder.time?.text = MusicUtil.getReadableDurationString(dataSet[position].duration)
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.hashCode().toLong()
    }

    protected open fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    private fun setColors(color: MediaNotificationProcessor, holder: ViewHolder) {
        if (holder.paletteColorContainer != null) {
            holder.title?.setTextColor(color.primaryTextColor)
            holder.text?.setTextColor(color.secondaryTextColor)
            holder.paletteColorContainer?.setBackgroundColor(color.backgroundColor)
            holder.menu?.imageTintList = ColorStateList.valueOf(color.primaryTextColor)
        }
        holder.mask?.backgroundTintList = ColorStateList.valueOf(color.primaryTextColor)
    }

    protected open fun loadAlbumCover(song: ArtistDetailsSongUi, holder: ViewHolder) {
        val image = holder.image ?: return
        GlideApp
            .with(activity)
            .asBitmapPalette()
            .load(song.coverArtUrl)
            .into(object : RetroMusicColoredTarget(image) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    setColors(colors, holder)
                }
            })
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): ArtistDetailsSongUi? {
        return dataSet[position]
    }

    override fun getName(model: ArtistDetailsSongUi): String {
        return model.title
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: List<ArtistDetailsSongUi>) {
//        SongsMenuHelper.handleMenuClick(activity, selection, menuItem.itemId)
    }

    override fun getPopupText(position: Int): String {
        val sectionName: String? = when (PreferenceUtil.songSortOrder) {
            SortOrder.SongSortOrder.SONG_DEFAULT -> return MusicUtil.getSectionName(dataSet[position].title, true)
            SortOrder.SongSortOrder.SONG_A_Z, SortOrder.SongSortOrder.SONG_Z_A -> dataSet[position].title
            SortOrder.SongSortOrder.SONG_ALBUM -> dataSet[position].album
            SortOrder.SongSortOrder.SONG_ARTIST -> dataSet[position].artist
            SortOrder.SongSortOrder.SONG_YEAR -> return MusicUtil.getYearString(dataSet[position].year)
//            SortOrder.SongSortOrder.COMPOSER -> dataSet[position].composer
//            SortOrder.SongSortOrder.SONG_ALBUM_ARTIST -> dataSet[position].ar
            else -> {
                return ""
            }
        }
        return MusicUtil.getSectionName(sectionName)
    }

    open inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {
        protected open var songMenuRes = SongMenuHelper.MENU_RES
        protected open val song: ArtistDetailsSongUi
            get() = dataSet[layoutPosition]

        init {
//            menu?.setOnClickListener(object : SongMenuHelper.OnClickSongMenu(activity) {
//                override val song: ArtistDetailsSongUi
//                    get() = this@ViewHolder.song
//
//                override val menuRes: Int
//                    get() = songMenuRes
//
//                override fun onMenuItemClick(item: MenuItem): Boolean {
//                    return onSongMenuItemClick(item) || super.onMenuItemClick(item)
//                }
//            })
        }

        protected open fun onSongMenuItemClick(item: MenuItem): Boolean {
            if (image != null && image!!.isVisible) {
                when (item.itemId) {
                    R.id.action_go_to_album -> {
                        activity.findNavController(R.id.fragment_container)
                            .navigate(
                                R.id.albumDetailsFragment,
                                bundleOf(EXTRA_ALBUM_ID to song.albumId)
                            )
                        return true
                    }
                }
            }
            return false
        }

        override fun onClick(v: View?) {
            if (isInQuickSelectMode) {
                toggleChecked(layoutPosition)
            } else {
//                MusicPlayerRemote.openQueue(dataSet, layoutPosition, true)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            println("Long click")
            return toggleChecked(layoutPosition)
        }
    }

    companion object {
        val TAG: String = ArtistDetailsSongAdapter::class.java.simpleName
    }
}
