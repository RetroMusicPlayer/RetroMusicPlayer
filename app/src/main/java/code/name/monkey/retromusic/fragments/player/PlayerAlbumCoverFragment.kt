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

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.SHOW_LYRICS
import code.name.monkey.retromusic.adapter.album.AlbumCoverPagerAdapter
import code.name.monkey.retromusic.adapter.album.AlbumCoverPagerAdapter.AlbumCoverFragment
import code.name.monkey.retromusic.databinding.FragmentPlayerAlbumCoverBinding
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.model.lyrics.AbsSynchronizedLyrics
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.transform.CarousalPagerTransformer
import code.name.monkey.retromusic.transform.ParallaxPagerTransformer
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.io.FileNotFoundException

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

    private val lyricsLayout: FrameLayout get() = binding.playerLyrics
    private val lyricsLine1: TextView get() = binding.playerLyricsLine1
    private val lyricsLine2: TextView get() = binding.playerLyricsLine2

    var lyrics: Lyrics? = null

    fun removeSlideEffect() {
        val transformer = ParallaxPagerTransformer(R.id.player_image)
        transformer.setSpeed(0.3f)
    }

    private fun updateLyrics() {
        lyrics = null
        lifecycleScope.launch(Dispatchers.IO) {
            val song = MusicPlayerRemote.currentSong
            val lyrics = try {
                var lrcFile: File? = null
                if (LyricUtil.isLrcOriginalFileExist(song.data)) {
                    lrcFile = LyricUtil.getLocalLyricOriginalFile(song.data)
                } else if (LyricUtil.isLrcFileExist(song.title, song.artistName)) {
                    lrcFile = LyricUtil.getLocalLyricFile(song.title, song.artistName)
                }
                val data: String = LyricUtil.getStringFromLrc(lrcFile)
                if (!TextUtils.isEmpty(data)) {
                    Lyrics.parse(song, data)
                } else {
                    // Get Embedded Lyrics and check if they are Synchronized
                    val embeddedLyrics = try{
                        AudioFileIO.read(File(song.data)).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
                    } catch(e: Exception){
                        null
                    }
                    if (AbsSynchronizedLyrics.isSynchronized(embeddedLyrics)) {
                        Lyrics.parse(song, embeddedLyrics)
                    } else {
                        null
                    }
                }
            } catch (err: FileNotFoundException) {
                null
            } catch (e: CannotReadException){
                null
            }
            withContext(Dispatchers.Main) {
                this@PlayerAlbumCoverFragment.lyrics = lyrics
            }
        }
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        if (_binding == null) return

        if (!isLyricsLayoutVisible()) {
            hideLyricsLayout()
            return
        }

        if (lyrics !is AbsSynchronizedLyrics) return
        val synchronizedLyrics = lyrics as AbsSynchronizedLyrics

        lyricsLayout.visibility = View.VISIBLE
        lyricsLayout.alpha = 1f

        val oldLine = lyricsLine2.text.toString()
        val line = synchronizedLyrics.getLine(progress)

        if (oldLine != line || oldLine.isEmpty()) {
            lyricsLine1.text = oldLine
            lyricsLine2.text = line

            lyricsLine1.visibility = View.VISIBLE
            lyricsLine2.visibility = View.VISIBLE

            lyricsLine2.measure(
                View.MeasureSpec.makeMeasureSpec(
                    lyricsLine2.measuredWidth,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.UNSPECIFIED
            )
            val h: Float = lyricsLine2.measuredHeight.toFloat()

            lyricsLine1.alpha = 1f
            lyricsLine1.translationY = 0f
            lyricsLine1.animate().alpha(0f).translationY(-h).duration =
                AbsPlayerFragment.VISIBILITY_ANIM_DURATION

            lyricsLine2.alpha = 0f
            lyricsLine2.translationY = h
            lyricsLine2.animate().alpha(1f).translationY(0f).duration =
                AbsPlayerFragment.VISIBILITY_ANIM_DURATION
        }
    }

    private fun isLyricsLayoutVisible(): Boolean {
        return lyrics != null && lyrics!!.isSynchronized && lyrics!!.isValid
    }

    private fun hideLyricsLayout() {
        lyricsLayout.animate().alpha(0f).setDuration(AbsPlayerFragment.VISIBILITY_ANIM_DURATION)
            .withEndAction {
                if (_binding == null) return@withEndAction
                lyricsLayout.visibility = View.GONE
                lyricsLine1.text = null
                lyricsLine2.text = null
            }
    }

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
            progressViewUpdateHelper?.start()
        }
        // Go to lyrics activity when clicked lyrics
        binding.playerLyricsLine2.setOnClickListener {
            NavigationUtil.goToLyrics(requireActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        val nps = PreferenceUtil.nowPlayingScreen
        // Don't show lyrics container for below conditions
        if (nps == Circle || nps == Peak || nps == Tiny || !PreferenceUtil.showLyrics) {
            lyricsLayout.isVisible = false
            progressViewUpdateHelper?.stop()
        } else {
            lyricsLayout.isVisible = true
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
                progressViewUpdateHelper?.start()
                lyricsLayout.animate().alpha(1f).duration =
                    AbsPlayerFragment.VISIBILITY_ANIM_DURATION
                binding.playerLyrics.isVisible = true
            } else {
                progressViewUpdateHelper?.stop()
                lyricsLayout.animate().alpha(0f)
                    .setDuration(AbsPlayerFragment.VISIBILITY_ANIM_DURATION)
                    .withEndAction {
                        if (_binding != null) {
                            binding.playerLyrics.isVisible = false
                            lyricsLine1.text = null
                            lyricsLine2.text = null
                        }
                    }
            }
        }
    }

    private fun updatePlayingQueue() {
        binding.viewPager.apply {
            adapter = AlbumCoverPagerAdapter(childFragmentManager, MusicPlayerRemote.playingQueue)
            adapter!!.notifyDataSetChanged()
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
