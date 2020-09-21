/*
 * Copyright (c) 2020 Hemanth Savarala.
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

package code.name.monkey.retromusic.fragments.home

import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.HISTORY_PLAYLIST
import code.name.monkey.retromusic.LAST_ADDED_PLAYLIST
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.TOP_PLAYED_PLAYLIST
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.extensions.findActivityNavController
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.ProfileBannerGlideRequest
import code.name.monkey.retromusic.glide.UserProfileGlideRequest
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.google.android.material.transition.platform.MaterialFadeThrough
import kotlinx.android.synthetic.main.abs_playlists.*
import kotlinx.android.synthetic.main.fragment_banner_home.*
import kotlinx.android.synthetic.main.home_content.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment :
    AbsMainActivityFragment(if (PreferenceUtil.isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    private val libraryViewModel: LibraryViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColorAuto(view)
        bannerImage?.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                mainActivity,
                userImage,
                getString(R.string.transition_user_image)
            )
            NavigationUtil.goToUserInfo(requireActivity(), options)
        }

        lastAdded.setOnClickListener {
            findActivityNavController(R.id.fragment_container).navigate(
                R.id.detailListFragment,
                bundleOf("type" to LAST_ADDED_PLAYLIST)
            )
        }

        topPlayed.setOnClickListener {
            findActivityNavController(R.id.fragment_container).navigate(
                R.id.detailListFragment,
                bundleOf("type" to TOP_PLAYED_PLAYLIST)
            )
        }

        actionShuffle.setOnClickListener {
            libraryViewModel.shuffleSongs()
        }

        history.setOnClickListener {
            findActivityNavController(R.id.fragment_container).navigate(
                R.id.detailListFragment,
                bundleOf("type" to HISTORY_PLAYLIST)
            )
        }

        userImage.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                mainActivity,
                userImage,
                getString(R.string.transition_user_image)
            )
            NavigationUtil.goToUserInfo(requireActivity(), options)
        }
        titleWelcome?.text = String.format("%s", PreferenceUtil.userName)

        val homeAdapter = HomeAdapter(mainActivity)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }

        libraryViewModel.getHome().observe(viewLifecycleOwner, Observer {
            homeAdapter.swapData(it)
        })

        loadProfile()
    }

    private fun loadProfile() {
        bannerImage?.let {
            ProfileBannerGlideRequest.Builder.from(
                Glide.with(requireContext()),
                ProfileBannerGlideRequest.getBannerModel()
            ).build().into(it)
        }
        UserProfileGlideRequest.Builder.from(
            Glide.with(requireActivity()),
            UserProfileGlideRequest.getUserModel()
        ).build().into(userImage)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.removeItem(R.id.action_sort_order)
        menu.findItem(R.id.action_settings).setShowAsAction(SHOW_AS_ACTION_IF_ROOM)
    }

    companion object {

        const val TAG: String = "BannerHomeFragment"

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}