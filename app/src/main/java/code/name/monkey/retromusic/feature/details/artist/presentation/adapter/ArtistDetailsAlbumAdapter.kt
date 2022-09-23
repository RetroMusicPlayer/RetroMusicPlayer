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

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.feature.details.artist.presentation.entity.ArtistDetailsAlbumUi
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.HorizontalAdapterHelper
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.interfaces.IAlbumClickListener
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import me.zhanghai.android.fastscroll.PopupTextProvider

class ArtistDetailsAlbumAdapter(
    override val activity: FragmentActivity,
    private var dataSet: List<ArtistDetailsAlbumUi>,
    val listener: IAlbumClickListener?
) : AbsMultiSelectAdapter<ArtistDetailsAlbumAdapter.ViewHolder, ArtistDetailsAlbumUi>(
    activity,
    R.menu.menu_media_selection
), PopupTextProvider {

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return HorizontalAdapterHelper.getItemViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun swapDataSet(dataSet: List<ArtistDetailsAlbumUi>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(HorizontalAdapterHelper.LAYOUT_RES, parent, false)
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        HorizontalAdapterHelper.applyMarginToLayoutParams(activity, params, viewType)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = dataSet[position]
        val isChecked = isChecked(album)
        holder.itemView.isActivated = isChecked
        holder.title?.text = album.title
        holder.text?.text = MusicUtil.getYearString(album.year)
        // Check if imageContainer exists so we can have a smooth transition without
        // CardView clipping, if it doesn't exist in current layout set transition name to image instead.
        if (holder.imageContainer != null) {
            holder.imageContainer?.transitionName = album.id
        } else {
            holder.image?.transitionName = album.id
        }
        loadAlbumCover(album, holder)
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.hashCode().toLong()
    }

    override fun getIdentifier(position: Int): ArtistDetailsAlbumUi? {
        return dataSet[position]
    }

    override fun getName(model: ArtistDetailsAlbumUi): String {
        return model.title
    }

    override fun onMultipleItemAction(
        menuItem: MenuItem,
        selection: List<ArtistDetailsAlbumUi>
    ) {
//        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.itemId)
    }

    override fun getPopupText(position: Int): String {
        return getSectionName(position)
    }

    private fun setColors(color: MediaNotificationProcessor, holder: ViewHolder) {
        // holder.title?.setTextColor(ATHUtil.resolveColor(activity, android.R.attr.textColorPrimary))
        // holder.text?.setTextColor(ATHUtil.resolveColor(activity, android.R.attr.textColorSecondary))
    }

    private fun loadAlbumCover(album: ArtistDetailsAlbumUi, holder: ViewHolder) {
        val image = holder.image ?: return
        GlideApp
            .with(activity)
            .asBitmapPalette()
            .load(album.coverArtUrl)
            .into(object : RetroMusicColoredTarget(image) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    setColors(colors, holder)
                }
            })
    }

    private fun getSectionName(position: Int): String {
        var sectionName: String? = null
        when (PreferenceUtil.albumSortOrder) {
            SortOrder.AlbumSortOrder.ALBUM_A_Z,
            SortOrder.AlbumSortOrder.ALBUM_Z_A -> sectionName = dataSet[position].title
            SortOrder.AlbumSortOrder.ALBUM_ARTIST -> sectionName = dataSet[position].artist
            SortOrder.AlbumSortOrder.ALBUM_YEAR -> return MusicUtil.getYearString(dataSet[position].year)
        }
        return MusicUtil.getSectionName(sectionName)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            menu?.isVisible = false
        }

        override fun onClick(v: View?) {
            super.onClick(v)
            if (isInQuickSelectMode) {
                toggleChecked(layoutPosition)
            } else {
                image?.let {
                    listener?.onAlbumClick(dataSet[layoutPosition].id, imageContainer ?: it)
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            return toggleChecked(layoutPosition)
        }
    }

    companion object {
        val TAG: String = AlbumAdapter::class.java.simpleName
    }
}
