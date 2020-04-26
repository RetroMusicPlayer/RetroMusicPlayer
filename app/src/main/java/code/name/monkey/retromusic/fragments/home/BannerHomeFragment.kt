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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.abs_playlists.*
import kotlinx.android.synthetic.main.fragment_banner_home.*
import kotlinx.android.synthetic.main.home_content.*
import java.io.File
import java.util.*

class BannerHomeFragment : AbsMainActivityFragment(), MainActivityFragmentCallbacks {

    private lateinit var homeAdapter: HomeAdapter
    lateinit var homeModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            if (PreferenceUtil.getInstance(requireContext()).isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home,
            viewGroup,
            false
        )
    }

    private fun loadImageFromStorage() {
        Glide.with(requireContext())
            .load(
                File(
                    PreferenceUtil.getInstance(requireContext()).profileImage,
                    Constants.USER_PROFILE
                )
            )
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(
                TintHelper.createTintedDrawable(
                    requireContext(),
                    R.drawable.ic_account_white_24dp,
                    ThemeStore.accentColor(requireContext())
                )
            )
            .error(
                TintHelper.createTintedDrawable(
                    requireContext(),
                    R.drawable.ic_account_white_24dp,
                    ThemeStore.accentColor(requireContext())
                )
            )
            .into(userImage)
    }

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
            NavigationUtil.goToPlaylistNew(requireActivity(), LastAddedPlaylist(requireActivity()))
        }

        topPlayed.setOnClickListener {
            NavigationUtil.goToPlaylistNew(
                requireActivity(),
                MyTopTracksPlaylist(requireActivity())
            )
        }

        actionShuffle.setOnClickListener {
            MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(requireActivity()), true)
        }

        history.setOnClickListener {
            NavigationUtil.goToPlaylistNew(requireActivity(), HistoryPlaylist(requireActivity()))
        }

        userImage.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                mainActivity,
                userImage,
                getString(R.string.transition_user_image)
            )
            NavigationUtil.goToUserInfo(requireActivity(), options)
        }
        titleWelcome?.text =
            String.format("%s", PreferenceUtil.getInstance(requireContext()).userName)

        homeAdapter = HomeAdapter(mainActivity, displayMetrics)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }
        homeModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeModel.sections.observe(viewLifecycleOwner, androidx.lifecycle.Observer { sections ->
            homeAdapter.swapData(sections)
        })
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        getTimeOfTheDay()
    }

    private fun getTimeOfTheDay() {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        var images = arrayOf<String>()
        when (timeOfDay) {
            in 0..5 -> images = resources.getStringArray(R.array.night)
            in 6..11 -> images = resources.getStringArray(R.array.morning)
            in 12..15 -> images = resources.getStringArray(R.array.after_noon)
            in 16..19 -> images = resources.getStringArray(R.array.evening)
            in 20..23 -> images = resources.getStringArray(R.array.night)
        }
        val day = images[Random().nextInt(images.size)]
        loadTimeImage(day)
    }

    private fun loadTimeImage(day: String) {
        bannerImage?.let {
            val request = Glide.with(requireContext())
            if (PreferenceUtil.getInstance(requireContext()).bannerImage.isEmpty()) {
                request.load(day)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.material_design_default)
                    .error(R.drawable.material_design_default)
                    .into(it)
            } else {
                request.load(
                    File(
                        PreferenceUtil.getInstance(requireContext()).bannerImage,
                        USER_BANNER
                    )
                )
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.material_design_default)
                    .error(R.drawable.material_design_default)
                    .into(it)
            }
        }
        loadImageFromStorage()
    }

    companion object {

        const val TAG: String = "BannerHomeFragment"

        @JvmStatic
        fun newInstance(): BannerHomeFragment {
            return BannerHomeFragment()
        }
    }
}