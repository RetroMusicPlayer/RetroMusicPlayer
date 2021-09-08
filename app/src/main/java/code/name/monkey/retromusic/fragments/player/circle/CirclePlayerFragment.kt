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
package code.name.monkey.retromusic.fragments.player.circle

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentCirclePlayerBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.extensions.applyColor
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.base.goToAlbum
import code.name.monkey.retromusic.fragments.base.goToArtist
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import code.name.monkey.retromusic.views.SeekArc
import code.name.monkey.retromusic.views.SeekArc.OnSeekArcChangeListener
import code.name.monkey.retromusic.volume.AudioVolumeObserver
import code.name.monkey.retromusic.volume.OnAudioVolumeChangedListener

/**
 * Created by hemanths on 2020-01-06.
 */

class CirclePlayerFragment : AbsPlayerFragment(R.layout.fragment_circle_player), Callback,
    OnAudioVolumeChangedListener,
    OnSeekArcChangeListener {

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private var audioVolumeObserver: AudioVolumeObserver? = null

    private val audioManager: AudioManager?
        get() = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var _binding: FragmentCirclePlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCirclePlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        binding.title.isSelected = true
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setOnMenuItemClickListener(this@CirclePlayerFragment)
            ToolbarContentTintHelper.colorizeToolbar(
                this,
                ATHUtil.resolveColor(requireContext(), R.attr.colorControlNormal),
                requireActivity()
            )
        }
    }

    private fun setupViews() {
        setUpProgressSlider()
        ViewUtil.setProgressDrawable(
            binding.progressSlider,
            ThemeStore.accentColor(requireContext()),
            false
        )
        binding.volumeSeekBar.progressColor = accentColor()
        binding.volumeSeekBar.arcColor = ColorUtil.withAlpha(accentColor(), 0.25f)
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpPlayerToolbar()
    }

    private fun setUpPrevNext() {
        updatePrevNextColor()
        binding.nextButton.setOnClickListener { MusicPlayerRemote.playNextSong() }
        binding.previousButton.setOnClickListener { MusicPlayerRemote.back() }
    }

    private fun updatePrevNextColor() {
        val accentColor = ThemeStore.accentColor(requireContext())
        binding.nextButton.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN)
        binding.previousButton.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setUpPlayPauseFab() {
        TintHelper.setTintAuto(
            binding.playPauseButton,
            ThemeStore.accentColor(requireContext()),
            false
        )
        binding.playPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
        if (audioVolumeObserver == null) {
            audioVolumeObserver = AudioVolumeObserver(requireActivity())
        }
        audioVolumeObserver?.register(AudioManager.STREAM_MUSIC, this)

        val audioManager = audioManager
        if (audioManager != null) {
            binding.volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            binding.volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
        binding.volumeSeekBar.setOnSeekArcChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean = false

    override fun toolbarIconColor(): Int =
        ATHUtil.resolveColor(requireContext(), android.R.attr.colorControlNormal)

    override val paletteColor: Int
        get() = Color.BLACK

    override fun onColorChanged(color: MediaNotificationProcessor) {
    }

    override fun onFavoriteToggled() {
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
        updatePlayPauseDrawableState()
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.title.text = song.title
        binding.text.text = song.artistName

        if (PreferenceUtil.isSongInfo) {
            binding.songInfo.text = getSongInfo(song)
            binding.songInfo.show()
        } else {
            binding.songInfo.hide()
        }
    }

    private fun updatePlayPauseDrawableState() {
        when {
            MusicPlayerRemote.isPlaying -> binding.playPauseButton.setImageResource(R.drawable.ic_pause)
            else -> binding.playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    override fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int) {
        _binding?.volumeSeekBar?.max = maxVolume
        _binding?.volumeSeekBar?.progress = currentVolume
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (audioVolumeObserver != null) {
            audioVolumeObserver!!.unregister()
        }
        _binding = null
    }

    override fun onProgressChanged(seekArc: SeekArc?, progress: Int, fromUser: Boolean) {
        val audioManager = audioManager
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
    }

    override fun onStartTrackingTouch(seekArc: SeekArc?) {
    }

    override fun onStopTrackingTouch(seekArc: SeekArc?) {
    }

    fun setUpProgressSlider() {
        binding.progressSlider.applyColor(accentColor())
        binding.progressSlider.setOnSeekBarChangeListener(object : SimpleOnSeekbarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress)
                    onUpdateProgressViews(
                        MusicPlayerRemote.songProgressMillis,
                        MusicPlayerRemote.songDurationMillis
                    )
                }
            }
        })
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.progressSlider.max = total

        val animator = ObjectAnimator.ofInt(binding.progressSlider, "progress", progress)
        animator.duration = AbsPlayerControlsFragment.SLIDER_ANIMATION_TIME
        animator.interpolator = LinearInterpolator()
        animator.start()

        binding.songTotalTime.text = MusicUtil.getReadableDurationString(total.toLong())
        binding.songCurrentProgress.text = MusicUtil.getReadableDurationString(progress.toLong())
    }
}
