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

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.LYRICS_TYPE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.SHOW_LYRICS
import code.name.monkey.retromusic.adapter.album.AlbumCoverPagerAdapter
import code.name.monkey.retromusic.adapter.album.AlbumCoverPagerAdapter.AlbumCoverFragment
import code.name.monkey.retromusic.databinding.FragmentPlayerAlbumCoverBinding
import code.name.monkey.retromusic.extensions.isColorLight
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment
import code.name.monkey.retromusic.fragments.base.goToLyrics
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.lyrics.CoverLrcView
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.transform.CarousalPagerTransformer
import code.name.monkey.retromusic.transform.ParallaxPagerTransformer
import code.name.monkey.retromusic.util.CoverLyricsType
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        lifecycleScope.launchWhenStarted {
            viewPager.setPageTransformer(false, transformer)
        }
    }

    private fun updateLyrics() {
        val song = MusicPlayerRemote.currentSong
        lifecycleScope.launch(Dispatchers.IO) {
            val lrcFile = LyricUtil.getSyncedLyricsFile(song)
            if (lrcFile != null) {
                binding.lyricsView.loadLrc(lrcFile)
            } else {
                val embeddedLyrics = LyricUtil.getEmbeddedSyncedLyrics(song.data)
                if (embeddedLyrics != null) {
                    binding.lyricsView.loadLrc(embeddedLyrics)
                } else {
                    withContext(Dispatchers.Main) {
                        binding.lyricsView.reset()
                        binding.lyricsView.setLabel(context?.getString(R.string.no_lyrics_found))
                    }
                }
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
        setupViewPager()
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
        maybeInitLyrics()
        lrcView.apply {
            setDraggable(true) { time ->
                MusicPlayerRemote.seekTo(time.toInt())
                MusicPlayerRemote.resumePlaying()
                true
            }
            setOnClickListener {
                goToLyrics(requireActivity())
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPager.addOnPageChangeListener(this)
        val nps = PreferenceUtil.nowPlayingScreen

        if (nps == Full || nps == Classic || nps == Fit || nps == Gradient) {
            binding.viewPager.offscreenPageLimit = 2
        } else if (PreferenceUtil.isCarouselEffect) {
            val metrics = resources.displayMetrics
            val ratio = metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat()
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
    }

    override fun onResume() {
        super.onResume()
        maybeInitLyrics()
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
        if (viewPager.currentItem != MusicPlayerRemote.position) {
            viewPager.setCurrentItem(MusicPlayerRemote.position, true)
        }
        updateLyrics()
    }

    override fun onQueueChanged() {
        updatePlayingQueue()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            SHOW_LYRICS -> {
                if (PreferenceUtil.showLyrics) {
                    maybeInitLyrics()
                } else {
                    showLyrics(false)
                    progressViewUpdateHelper?.stop()
                }
            }
            LYRICS_TYPE -> {
                maybeInitLyrics()
            }
        }
    }

    private fun setLRCViewColors(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        lrcView.apply {
            setCurrentColor(primaryColor)
            setTimeTextColor(primaryColor)
            setTimelineColor(primaryColor)
            setNormalColor(secondaryColor)
            setTimelineTextColor(primaryColor)
        }
    }

    private fun showLyrics(visible: Boolean) {
        binding.coverLyrics.isVisible = false
        binding.lyricsView.isVisible = false
        binding.viewPager.isVisible = true
        val lyrics: View = if (PreferenceUtil.lyricsType == CoverLyricsType.REPLACE_COVER) {
            ObjectAnimator.ofFloat(viewPager, View.ALPHA, if (visible) 0F else 1F).start()
            lrcView
        } else {
            ObjectAnimator.ofFloat(viewPager, View.ALPHA, 1F).start()
            binding.coverLyrics
        }
        ObjectAnimator.ofFloat(lyrics, View.ALPHA, if (visible) 1F else 0F).apply {
            doOnEnd {
                lyrics.isVisible = visible
            }
            start()
        }
    }

    private fun maybeInitLyrics() {
        val nps = PreferenceUtil.nowPlayingScreen
        // Don't show lyrics container for below conditions
        if (lyricViewNpsList.contains(nps) && PreferenceUtil.showLyrics) {
            showLyrics(true)
            if (PreferenceUtil.lyricsType == CoverLyricsType.REPLACE_COVER) {
                progressViewUpdateHelper?.start()
            }
        } else {
            showLyrics(false)
            progressViewUpdateHelper?.stop()
        }
    }

    private fun updatePlayingQueue() {
        binding.viewPager.apply {
            adapter = AlbumCoverPagerAdapter(parentFragmentManager, MusicPlayerRemote.playingQueue)
            setCurrentItem(MusicPlayerRemote.position, true)
            onPageSelected(MusicPlayerRemote.position)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

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
        val primaryColor = MaterialValueHelper.getPrimaryTextColor(
            requireContext(),
            surfaceColor().isColorLight
        )
        val secondaryColor = MaterialValueHelper.getSecondaryDisabledTextColor(
            requireContext(),
            surfaceColor().isColorLight
        )

        when (PreferenceUtil.nowPlayingScreen) {
            Flat, Normal, Material -> if (PreferenceUtil.isAdaptiveColor) {
                setLRCViewColors(color.primaryTextColor, color.secondaryTextColor)
            } else {
                setLRCViewColors(primaryColor, secondaryColor)
            }
            Color, Classic -> setLRCViewColors(color.primaryTextColor, color.secondaryTextColor)
            Blur -> setLRCViewColors(Color.WHITE, ColorUtil.withAlpha(Color.WHITE, 0.5f))
            else -> setLRCViewColors(primaryColor, secondaryColor)
        }
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

    private val lyricViewNpsList =
        listOf(Blur, Classic, Color, Flat, Material, MD3, Normal, Plain, Simple)
}
