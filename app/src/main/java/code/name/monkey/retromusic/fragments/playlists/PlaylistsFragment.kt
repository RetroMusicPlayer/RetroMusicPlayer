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
package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.MenuCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.EXTRA_PLAYLIST
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.playlist.PlaylistAdapter
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.helper.SortOrder.PlaylistSortOrder
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.interfaces.IPlaylistClickListener
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import com.afollestad.materialcab.MaterialCab
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.transition.MaterialSharedAxis

class PlaylistsFragment :
    AbsRecyclerViewCustomGridSizeFragment<PlaylistAdapter, GridLayoutManager>(),
    IPlaylistClickListener, ICabHolder {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getPlaylists().observe(viewLifecycleOwner, {
            if (it.isNotEmpty())
                adapter?.swapDataSet(it)
            else
                adapter?.swapDataSet(listOf())
        })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!handleBackPress()) {
                remove()
                requireActivity().onBackPressed()
            }
        }
    }

    override val titleRes: Int
        get() = R.string.playlists
    override val emptyMessage: Int
        get() = R.string.no_playlists

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireContext(), getGridSize())
    }

    override fun createAdapter(): PlaylistAdapter {
        return PlaylistAdapter(
            requireActivity(),
            ArrayList(),
            itemLayoutRes(),
            this,
            this
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.add(0, R.id.action_add_to_playlist, 0, R.string.new_playlist_title)
        menu.add(0, R.id.action_import_playlist, 0, R.string.import_playlist)
        menu.findItem(R.id.action_settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        setUpSortOrderMenu(menu.findItem(R.id.action_sort_order).subMenu)
        MenuCompat.setGroupDividerEnabled(menu, true)
        //Setting up cast button
        CastButtonFactory.setUpMediaRouteButton(requireContext(), menu, R.id.action_cast)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (handleSortOrderMenuItem(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUpSortOrderMenu(subMenu: SubMenu) {
        val order: String? = getSortOrder()
        subMenu.clear()
        createId(
            subMenu,
            R.id.action_song_sort_order_asc,
            R.string.sort_order_a_z,
            order == PlaylistSortOrder.PLAYLIST_A_Z
        )
        createId(
            subMenu,
            R.id.action_song_sort_order_desc,
            R.string.sort_order_z_a,
            order == PlaylistSortOrder.PLAYLIST_Z_A
        )
        createId(
            subMenu,
            R.id.action_playlist_sort_order,
            R.string.sort_order_num_songs,
            order == PlaylistSortOrder.PLAYLIST_SONG_COUNT
        )
        createId(
            subMenu,
            R.id.action_playlist_sort_order_desc,
            R.string.sort_order_num_songs_desc,
            order == PlaylistSortOrder.PLAYLIST_SONG_COUNT_DESC
        )
        subMenu.setGroupCheckable(0, true, true)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        val sortOrder: String = when (item.itemId) {
            R.id.action_song_sort_order_asc -> PlaylistSortOrder.PLAYLIST_A_Z
            R.id.action_song_sort_order_desc -> PlaylistSortOrder.PLAYLIST_Z_A
            R.id.action_playlist_sort_order -> PlaylistSortOrder.PLAYLIST_SONG_COUNT
            R.id.action_playlist_sort_order_desc -> PlaylistSortOrder.PLAYLIST_SONG_COUNT_DESC
            else -> PreferenceUtil.playlistSortOrder
        }
        if (sortOrder != PreferenceUtil.playlistSortOrder) {
            item.isChecked = true
            setAndSaveSortOrder(sortOrder)
            return true
        }
        return false
    }

    private fun createId(menu: SubMenu, id: Int, title: Int, checked: Boolean) {
        menu.add(0, id, 0, title).isChecked = checked
    }


    override fun setGridSize(gridSize: Int) {
        TODO("Not yet implemented")
    }

    override fun setSortOrder(sortOrder: String) {
        libraryViewModel.forceReload(ReloadType.Playlists)
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.playlistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.playlistSortOrder = sortOrder
    }

    override fun loadGridSize(): Int {
        return 2
    }

    override fun saveGridSize(gridColumns: Int) {
        //Add grid save
    }

    override fun loadGridSizeLand(): Int {
        return 4
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        //Add land grid save
    }

    override fun loadLayoutRes(): Int {
        return R.layout.item_grid
    }

    override fun saveLayoutRes(layoutRes: Int) {
        //Save layout
    }

    override fun onPlaylistClick(playlistWithSongs: PlaylistWithSongs, view: View) {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(requireView())
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        findNavController().navigate(
            R.id.playlistDetailsFragment,
            bundleOf(EXTRA_PLAYLIST to playlistWithSongs),
            null,
            null
        )
    }

    private fun handleBackPress(): Boolean {
        cab?.let {
            if (it.isActive) {
                it.finish()
                return true
            }
        }
        return false
    }

    private var cab: MaterialCab? = null

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        cab?.let {
            println("Cab")
            if (it.isActive) {
                it.finish()
            }
        }
        cab = MaterialCab(mainActivity, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close)
            .setBackgroundColor(RetroColorUtil.shiftBackgroundColorForLightText(surfaceColor()))
            .start(callback)
        return cab as MaterialCab
    }
}