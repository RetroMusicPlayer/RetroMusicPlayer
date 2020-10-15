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
package io.github.muntashirakon.music.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.accentColor
import io.github.muntashirakon.music.extensions.applyColor
import io.github.muntashirakon.music.extensions.show
import io.github.muntashirakon.music.extensions.textColorPrimary
import io.github.muntashirakon.music.extensions.textColorSecondary
import io.github.muntashirakon.music.fragments.base.AbsMusicServiceFragment
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.helper.MusicProgressViewUpdateHelper
import io.github.muntashirakon.music.helper.PlayPauseButtonOnClickHandler
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.RetroUtil
import kotlin.math.abs
import kotlinx.android.synthetic.main.fragment_mini_player.*

open class MiniPlayerFragment : AbsMusicServiceFragment(R.layout.fragment_mini_player),
    MusicProgressViewUpdateHelper.Callback, View.OnClickListener {

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.actionNext -> MusicPlayerRemote.playNextSong()
            R.id.actionPrevious -> MusicPlayerRemote.back()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener(FlingPlayBackController(requireContext()))
        setUpMiniPlayer()

        if (RetroUtil.isTablet()) {
            actionNext.show()
            actionPrevious.show()
            actionNext?.show()
            actionPrevious?.show()
        } else {
            actionNext.visibility = if (PreferenceUtil.isExtraControls) View.VISIBLE else View.GONE
            actionPrevious.visibility =
                if (PreferenceUtil.isExtraControls) View.VISIBLE else View.GONE
        }
        actionNext.setOnClickListener(this)
        actionPrevious.setOnClickListener(this)
        actionNext?.setOnClickListener(this)
        actionPrevious?.setOnClickListener(this)
    }

    private fun setUpMiniPlayer() {
        setUpPlayPauseButton()
        progressBar.accentColor()
    }

    private fun setUpPlayPauseButton() {
        miniPlayerPlayPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    private fun updateSongTitle() {
        val builder = SpannableStringBuilder()

        val song = MusicPlayerRemote.currentSong
        val title = SpannableString(song.title)
        title.setSpan(ForegroundColorSpan(textColorPrimary()), 0, title.length, 0)

        val text = SpannableString(song.artistName)
        text.setSpan(ForegroundColorSpan(textColorSecondary()), 0, text.length, 0)

        builder.append(title).append(" • ").append(text)

        miniPlayerTitle.isSelected = true
        miniPlayerTitle.text = builder
    }

    override fun onServiceConnected() {
        updateSongTitle()
        updatePlayPauseDrawableState()
    }

    override fun onPlayingMetaChanged() {
        updateSongTitle()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        progressBar.max = total
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progress)
        animator.duration = 1000
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    protected fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            miniPlayerPlayPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            miniPlayerPlayPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    fun updateProgressBar(paletteColor: Int) {
        progressBar.applyColor(paletteColor)
    }

    class FlingPlayBackController(context: Context) : View.OnTouchListener {

        private var flingPlayBackController: GestureDetector

        init {
            flingPlayBackController = GestureDetector(context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onFling(
                        e1: MotionEvent,
                        e2: MotionEvent,
                        velocityX: Float,
                        velocityY: Float
                    ): Boolean {
                        if (abs(velocityX) > abs(velocityY)) {
                            if (velocityX < 0) {
                                MusicPlayerRemote.playNextSong()
                                return true
                            } else if (velocityX > 0) {
                                MusicPlayerRemote.playPreviousSong()
                                return true
                            }
                        }
                        return false
                    }
                })
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return flingPlayBackController.onTouchEvent(event)
        }
    }
}
