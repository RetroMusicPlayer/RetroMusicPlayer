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
package code.name.monkey.retromusic.fragments.player.full

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.EXTRA_ARTIST_ID
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.extensions.whichFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.glide.ArtistGlideRequest
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.AbsSynchronizedLyrics
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_full.*

class FullPlayerFragment : AbsPlayerFragment(R.layout.fragment_full),
    MusicProgressViewUpdateHelper.Callback {
    private lateinit var lyricsLayout: FrameLayout
    private lateinit var lyricsLine1: TextView
    private lateinit var lyricsLine2: TextView

    private var lyrics: Lyrics? = null
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        if (!isLyricsLayoutBound()) return

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
            lyricsLine1.animate().alpha(0f).translationY(-h).duration = VISIBILITY_ANIM_DURATION

            lyricsLine2.alpha = 0f
            lyricsLine2.translationY = h
            lyricsLine2.animate().alpha(1f).translationY(0f).duration = VISIBILITY_ANIM_DURATION
        }
    }

    private fun isLyricsLayoutVisible(): Boolean {
        return lyrics != null && lyrics!!.isSynchronized && lyrics!!.isValid
    }

    private fun isLyricsLayoutBound(): Boolean {
        return lyricsLayout != null && lyricsLine1 != null && lyricsLine2 != null
    }

    private fun hideLyricsLayout() {
        lyricsLayout.animate().alpha(0f).setDuration(VISIBILITY_ANIM_DURATION)
            .withEndAction(Runnable {
                if (!isLyricsLayoutBound()) return@Runnable
                lyricsLayout.visibility = View.GONE
                lyricsLine1.text = null
                lyricsLine2.text = null
            })
    }

    override fun setLyrics(l: Lyrics?) {
        lyrics = l

        if (!isLyricsLayoutBound()) return

        if (!isLyricsLayoutVisible()) {
            hideLyricsLayout()
            return
        }

        lyricsLine1.text = null
        lyricsLine2.text = null

        lyricsLayout.visibility = View.VISIBLE
        lyricsLayout.animate().alpha(1f).duration = VISIBILITY_ANIM_DURATION
    }

    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor
    private lateinit var controlsFragment: FullPlaybackControlsFragment

    private fun setUpPlayerToolbar() {
        playerToolbar.apply {
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lyricsLayout = view.findViewById(R.id.playerLyrics)
        lyricsLine1 = view.findViewById(R.id.player_lyrics_line1)
        lyricsLine2 = view.findViewById(R.id.player_lyrics_line2)

        setUpSubFragments()
        setUpPlayerToolbar()
        setupArtist()
        nextSong.isSelected = true

        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
        progressViewUpdateHelper.start()
    }

    private fun setupArtist() {
        artistImage.setOnClickListener {
            mainActivity.collapsePanel()
            findNavController().navigate(
                R.id.artistDetailsFragment,
                bundleOf(EXTRA_ARTIST_ID to MusicPlayerRemote.currentSong.artistId),
            )
        }
    }

    private fun setUpSubFragments() {
        controlsFragment = whichFragment(R.id.playbackControlsFragment)
        val coverFragment: PlayerAlbumCoverFragment = whichFragment(R.id.playerAlbumCoverFragment)
        coverFragment.setCallbacks(this)
        coverFragment.removeSlideEffect()
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        mask.backgroundTintList = ColorStateList.valueOf(color.backgroundColor)
        controlsFragment.setColor(color)
        libraryViewModel.updateColor(color.backgroundColor)
        ToolbarContentTintHelper.colorizeToolbar(playerToolbar, Color.WHITE, activity)
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
        controlsFragment.onFavoriteToggled()
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateArtistImage()
        updateLabel()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateArtistImage()
        updateLabel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressViewUpdateHelper.stop()
    }

    private fun updateArtistImage() {
        libraryViewModel.artist(MusicPlayerRemote.currentSong.artistId)
            .observe(viewLifecycleOwner, { artist ->
                ArtistGlideRequest.Builder.from(Glide.with(requireContext()), artist)
                    .generatePalette(requireContext())
                    .build()
                    .into(object : RetroMusicColoredTarget(artistImage) {
                        override fun onColorReady(colors: MediaNotificationProcessor) {
                        }
                    })
            })
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) updateLabel()
    }

    private fun updateLabel() {
        (MusicPlayerRemote.playingQueue.size - 1).apply {
            if (this == (MusicPlayerRemote.position)) {
                nextSongLabel.setText(R.string.last_song)
                nextSong.hide()
            } else {
                val title = MusicPlayerRemote.playingQueue[MusicPlayerRemote.position + 1].title
                nextSongLabel.setText(R.string.next_song)
                nextSong.apply {
                    text = title
                    show()
                }
            }
        }
    }
}
