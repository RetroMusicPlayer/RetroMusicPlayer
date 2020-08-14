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
import android.util.DisplayMetrics
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.EXTRA_PLAYLIST
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.extensions.findActivityNavController
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.ProfileBannerGlideRequest
import code.name.monkey.retromusic.glide.UserProfileGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist
import code.name.monkey.retromusic.model.smartplaylist.TopTracksPlaylist
import code.name.monkey.retromusic.repository.Repository
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.abs_playlists.*
import kotlinx.android.synthetic.main.fragment_banner_home.*
import kotlinx.android.synthetic.main.home_content.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment :
    AbsMainActivityFragment(if (PreferenceUtil.isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home) {

    private val repository by inject<Repository>()
    private val libraryViewModel: LibraryViewModel by sharedViewModel()

    private val displayMetrics: DisplayMetrics
        get() {
            val display = mainActivity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            return metrics
        }

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
                R.id.playlistDetailsFragment,
                bundleOf(EXTRA_PLAYLIST to LastAddedPlaylist())
            )
        }

        topPlayed.setOnClickListener {
            findActivityNavController(R.id.fragment_container).navigate(
                R.id.playlistDetailsFragment,
                bundleOf(EXTRA_PLAYLIST to TopTracksPlaylist())
            )
        }

        actionShuffle.setOnClickListener {
            lifecycleScope.launch {
                MusicPlayerRemote.openAndShuffleQueue(
                    repository.allSongs(),
                    true
                )
            }
        }

        history.setOnClickListener {
            requireActivity().findNavController(R.id.fragment_container).navigate(
                R.id.playlistDetailsFragment,
                bundleOf(EXTRA_PLAYLIST to HistoryPlaylist())
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

        libraryViewModel.homeLiveData.observe(viewLifecycleOwner, Observer {
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

    companion object {

        const val TAG: String = "BannerHomeFragment"

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}