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
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.EXTRA_ARTIST_NAME
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.MainActivity
import code.name.monkey.retromusic.databinding.FragmentFullBinding
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.CoverLyricsFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroGlideExtension.artistImageOptions
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.ArtistSeparator
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FullPlayerFragment : AbsPlayerFragment(R.layout.fragment_full) {
    private var _binding: FragmentFullBinding? = null
    private val binding get() = _binding!!

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor
    private lateinit var controlsFragment: FullPlaybackControlsFragment

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFullBinding.bind(view)

        setUpSubFragments()
        setUpPlayerToolbar()
        setupArtistClick()
        binding.nextSong.isSelected = true
        binding.playbackControlsFragment.drawAboveSystemBars()
    }

    private fun setupArtistClick() {
        binding.artistImage.setOnClickListener {
            val activity = requireActivity() as? MainActivity ?: return@setOnClickListener
            val song = MusicPlayerRemote.currentSong
            val artistNames = ArtistSeparator.split(song.artistName)

            if (artistNames.size > 1) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.go_to_artist))
                    .setItems(artistNames.toTypedArray()) { dialog, which ->
                        navigateToArtist(activity, artistNames[which])
                        dialog.dismiss()
                    }
                    .show()
            } else if (artistNames.isNotEmpty()) {
                navigateToArtist(activity, artistNames[0])
            }
        }
    }

    private fun navigateToArtist(activity: MainActivity, artistName: String) {
        activity.currentFragment(R.id.fragment_container)?.exitTransition = null
        activity.setBottomNavVisibility(false)
        if (activity.getBottomSheetBehavior().state == BottomSheetBehavior.STATE_EXPANDED) {
            activity.collapsePanel()
        }
        activity.findNavController(R.id.fragment_container).navigate(
            R.id.artistDetailsFragment,
            bundleOf(EXTRA_ARTIST_NAME to artistName)
        )
    }

    private fun setUpSubFragments() {
        controlsFragment = whichFragment(R.id.playbackControlsFragment)
        val coverFragment: PlayerAlbumCoverFragment = whichFragment(R.id.playerAlbumCoverFragment)
        coverFragment.setCallbacks(this)
        coverFragment.removeSlideEffect()
    }

    override fun onShow() {}

    override fun onHide() {}

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        binding.mask.backgroundTintList = ColorStateList.valueOf(color.backgroundColor)
        controlsFragment.setColor(color)
        libraryViewModel.updateColor(color.backgroundColor)
        ToolbarContentTintHelper.colorizeToolbar(binding.playerToolbar, Color.WHITE, activity)
        binding.coverLyrics.getFragment<CoverLyricsFragment>().setColors(color)
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
        _binding = null
    }

    private fun updateArtistImage() {
        if (!isAdded) return
        val song = MusicPlayerRemote.currentSong
        val mainArtistName = ArtistSeparator.split(song.artistName).firstOrNull()
        if (mainArtistName != null) {
            libraryViewModel.artistByName(mainArtistName).observe(viewLifecycleOwner) { artist ->
                if (artist != null && artist.id != -1L) {
                    if (!isAdded) return@observe
                    Glide.with(this)
                        .load(RetroGlideExtension.getArtistModel(artist))
                        .artistImageOptions(artist)
                        .into(binding.artistImage)
                }
            }
        }
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) updateLabel()
    }

    private fun updateLabel() {
        if (!isAdded) return
        if ((MusicPlayerRemote.playingQueue.size - 1) == (MusicPlayerRemote.position)) {
            binding.nextSongLabel.setText(R.string.last_song)
            binding.nextSong.hide()
        } else {
            val title = MusicPlayerRemote.playingQueue[MusicPlayerRemote.position + 1].title
            binding.nextSongLabel.setText(R.string.next_song)
            binding.nextSong.apply {
                text = title
                show()
            }
        }
    }
}