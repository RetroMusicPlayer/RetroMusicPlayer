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
package code.name.monkey.retromusic.fragments.player

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.SHOW_LYRICS
import code.name.monkey.retromusic.adapter.album.AlbumCoverPagerAdapter
import code.name.monkey.retromusic.adapter.album.AlbumCoverPagerAdapter.AlbumCoverFragment
import code.name.monkey.retromusic.databinding.FragmentPlayerAlbumCoverBinding
import code.name.monkey.retromusic.extensions.isColorLight
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.base.goToLyrics
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.lyrics.CoverLrcView
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.transform.CarousalPagerTransformer
import code.name.monkey.retromusic.transform.ParallaxPagerTransformer
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor

class PlayerAlbumCoverFragment : AbsMusicServiceFragment(R.layout.fragment_player_album_cover),
    ViewPager.OnPageChangeListener, MusicProgressViewUpdateHelper.Callback,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var _binding: FragmentPlayerAlbumCoverBinding? = null
    private val binding get() = _binding!!
    private var callbacks: Callbacks? = null
    private var currentPosition: Int = 0
    val viewPager get() = binding.viewPager

    private val colorReceiver = object : AlbumCoverFragment.ColorReceiver {
        override fun onColorReady(color: MediaNotificationProcessor, request: Int) {
            if (currentPosition == request) {
                notifyColorChange(color)
            }
        }
    }
    private var progressViewUpdateHelper: MusicProgressViewUpdateHelper? = null

    private val lrcView: CoverLrcView get() = binding.lyricsView

    var lyrics: Lyrics? = null

    fun removeSlideEffect() {
        val transformer = ParallaxPagerTransformer(R.id.player_image)
        transformer.setSpeed(0.3f)
    }

    private fun updateLyrics() {
        binding.lyricsView.setLabel("Empty")
        val song = MusicPlayerRemote.currentSong
        when {
            LyricUtil.isLrcOriginalFileExist(song.data) -> {
                LyricUtil.getLocalLyricOriginalFile(song.data)
                    ?.let { binding.lyricsView.loadLrc(it) }
            }
            LyricUtil.isLrcFileExist(song.title, song.artistName) -> {
                LyricUtil.getLocalLyricFile(song.title, song.artistName)
                    ?.let { binding.lyricsView.loadLrc(it) }
            }
            else -> {
                binding.lyricsView.reset()
            }
        }
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.lyricsView.updateTime(progress.toLong())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlayerAlbumCoverBinding.bind(view)
        binding.viewPager.addOnPageChangeListener(this)
        val nps = PreferenceUtil.nowPlayingScreen

        val metrics = resources.displayMetrics
        val ratio = metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat()

        if (nps == Full || nps == Classic || nps == Fit || nps == Gradient) {
            binding.viewPager.offscreenPageLimit = 2
        } else if (PreferenceUtil.isCarouselEffect) {
            binding.viewPager.clipToPadding = false
            val padding =
                if (ratio >= 1.777f) {
                    40
                } else {
                    100
                }
            binding.viewPager.setPadding(padding, 0, padding, 0)
            binding.viewPager.pageMargin = 0
            binding.viewPager.setPageTransformer(false, CarousalPagerTransformer(requireContext()))
        } else {
            binding.viewPager.offscreenPageLimit = 2
            binding.viewPager.setPageTransformer(
                true,
                PreferenceUtil.albumCoverTransform
            )
        }
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
        // Don't show lyrics container for below conditions
        if (!(nps == Circle || nps == Peak || nps == Tiny || !PreferenceUtil.showLyrics)) {
            lrcView.isVisible = false
            viewPager.isInvisible = false
            progressViewUpdateHelper?.stop()
        } else {
            lrcView.isVisible = true
            viewPager.isInvisible = true
            progressViewUpdateHelper?.start()
        }
        lrcView.apply {
            setDraggable(true, object : CoverLrcView.OnPlayClickListener {
                override fun onPlayClick(time: Long): Boolean {
                    MusicPlayerRemote.seekTo(time.toInt())
                    MusicPlayerRemote.resumePlaying()
                    return true
                }
            })
        }
        // Go to lyrics activity when clicked lyrics
        lrcView.setOnClickListener {
            goToLyrics(requireActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        val nps = PreferenceUtil.nowPlayingScreen
        // Don't show lyrics container for below conditions
        if (nps == Circle || nps == Peak || nps == Tiny || !PreferenceUtil.showLyrics) {
            lrcView.isVisible = false
            viewPager.isInvisible = false
            progressViewUpdateHelper?.stop()
        } else {
            lrcView.isVisible = true
            viewPager.isInvisible = true
            progressViewUpdateHelper?.start()
        }
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
        binding.viewPager.removeOnPageChangeListener(this)
        progressViewUpdateHelper?.stop()
        _binding = null
    }

    override fun onServiceConnected() {
        updatePlayingQueue()
        updateLyrics()
    }

    override fun onPlayingMetaChanged() {
        binding.viewPager.currentItem = MusicPlayerRemote.position
        updateLyrics()
    }

    override fun onQueueChanged() {
        updatePlayingQueue()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == SHOW_LYRICS) {
            if (sharedPreferences.getBoolean(key, false)) {
                val nps = PreferenceUtil.nowPlayingScreen
                // Don't show lyrics container for below conditions
                if (!(nps == Circle || nps == Peak || nps == Tiny || !PreferenceUtil.showLyrics)) {
                    lrcView.isVisible = true
                    viewPager.isInvisible = true
                    progressViewUpdateHelper?.start()
                    lrcView.animate().alpha(1f).duration =
                        AbsPlayerFragment.VISIBILITY_ANIM_DURATION
                } else {
                    lrcView.isVisible = false
                    viewPager.isInvisible = false
                    progressViewUpdateHelper?.stop()
                }
            } else {
                lrcView.isVisible = false
                viewPager.isInvisible = false
                progressViewUpdateHelper?.stop()
            }
        }
    }

    private fun setLRCViewColors(backgroundColor: Int) {
        val primaryColor = MaterialValueHelper.getPrimaryTextColor(
            requireContext(),
            backgroundColor.isColorLight
        )
        val secondaryColor = MaterialValueHelper.getSecondaryDisabledTextColor(
            requireContext(),
            backgroundColor.isColorLight
        )
        lrcView.apply {
            setCurrentColor(primaryColor)
            setTimeTextColor(primaryColor)
            setTimelineColor(primaryColor)
            setNormalColor(secondaryColor)
            setTimelineTextColor(primaryColor)
        }
    }

    private fun updatePlayingQueue() {
        binding.viewPager.apply {
            adapter = AlbumCoverPagerAdapter(childFragmentManager, MusicPlayerRemote.playingQueue)
            adapter?.notifyDataSetChanged()
            currentItem = MusicPlayerRemote.position
            onPageSelected(MusicPlayerRemote.position)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        currentPosition = position
        if (binding.viewPager.adapter != null) {
            (binding.viewPager.adapter as AlbumCoverPagerAdapter).receiveColor(
                colorReceiver,
                position
            )
        }
        if (position != MusicPlayerRemote.position) {
            MusicPlayerRemote.playSongAt(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }


    private fun notifyColorChange(color: MediaNotificationProcessor) {
        callbacks?.onColorChanged(color)
        setLRCViewColors(
            when (PreferenceUtil.nowPlayingScreen) {
                Flat, Normal -> if (PreferenceUtil.isAdaptiveColor) {
                    color.backgroundColor
                } else {
                    surfaceColor()
                }
                Color, Gradient, Full ->color.backgroundColor
                Blur -> Color.BLACK
                else -> surfaceColor()
            })
    }

    fun setCallbacks(listener: Callbacks) {
        callbacks = listener
    }

    interface Callbacks {

        fun onColorChanged(color: MediaNotificationProcessor)

        fun onFavoriteToggled()
    }

    companion object {
        val TAG: String = PlayerAlbumCoverFragment::class.java.simpleName
    }
}
