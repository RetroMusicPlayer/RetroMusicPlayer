/*
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.fragments.player.ring

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentRingPlayerBinding
import code.name.monkey.retromusic.extensions.drawAboveSystemBars
import code.name.monkey.retromusic.fragments.MusicSeekSkipTouchListener
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.glide.BlurTransformation
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroGlideExtension.simpleSongCoverOptions
import code.name.monkey.retromusic.glide.crossfadeListener
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tankery.lib.circularseekbar.CircularSeekBar

/**
 * "Ring cover" player: a round cover inside a white progress ring that can be dragged to seek,
 * a blurred cover as background, and tapping the cover swaps it for the synced lyrics.
 */
class RingPlayerFragment : AbsPlayerFragment(R.layout.fragment_ring_player),
    MusicProgressViewUpdateHelper.Callback, CircularSeekBar.OnCircularSeekBarChangeListener {

    private var _binding: FragmentRingPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private var lastRequest: RequestBuilder<Drawable>? = null
    private var isSeeking = false
    private var showingLyrics = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRingPlayerBinding.bind(view)

        binding.playerToolbar.apply {
            inflateMenu(R.menu.menu_player)
            setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            setOnMenuItemClickListener(this@RingPlayerFragment)
            ToolbarContentTintHelper.colorizeToolbar(this, Color.WHITE, requireActivity())
            drawAboveSystemBars()
        }

        binding.ringTitle.isSelected = true
        binding.progressRing.setOnSeekBarChangeListener(this)

        binding.playPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
        binding.nextButton.setOnTouchListener(MusicSeekSkipTouchListener(requireActivity(), true))
        binding.previousButton.setOnTouchListener(
            MusicSeekSkipTouchListener(requireActivity(), false)
        )
        binding.shuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
        binding.repeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
        binding.favoriteButton.setOnClickListener { toggleFavorite(MusicPlayerRemote.currentSong) }
        binding.queueButton.setOnClickListener {
            requireActivity().findNavController(R.id.fragment_container).navigate(
                R.id.playing_queue_fragment, null, navOptions { launchSingleTop = true }
            )
            mainActivity.collapsePanel()
        }
        binding.shareButton.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    MusicUtil.createShareSongFileIntent(
                        requireContext(), MusicPlayerRemote.currentSong
                    ), null
                )
            )
        }
        binding.equalizerButton.setOnClickListener { NavigationUtil.openEqualizer(requireActivity()) }
        binding.ringTitle.setOnClickListener { code.name.monkey.retromusic.fragments.base.goToAlbum(requireActivity()) }
        binding.ringArtist.setOnClickListener { code.name.monkey.retromusic.fragments.base.goToArtist(requireActivity()) }

        // Tapping the cover or the chip swaps the cover for the lyrics and back.
        binding.ringCover.setOnClickListener { setLyricsVisible(!showingLyrics) }
        binding.lyricsChip.setOnClickListener { setLyricsVisible(!showingLyrics) }
        binding.ringLyrics.apply {
            setCurrentColor(Color.WHITE)
            setNormalColor(Color.argb(140, 255, 255, 255))
            setTimeTextColor(Color.WHITE)
            setTimelineColor(Color.WHITE)
            setTimelineTextColor(Color.WHITE)
            setDraggable(true) { time ->
                MusicPlayerRemote.seekTo(time.toInt())
                MusicPlayerRemote.resumePlaying()
                true
            }
            setOnClickListener { setLyricsVisible(false) }
        }
    }

    private fun setLyricsVisible(visible: Boolean) {
        showingLyrics = visible
        binding.ringLyrics.isVisible = visible
        binding.ringCover.isVisible = !visible
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.ringTitle.text = song.title
        binding.ringArtist.text = song.artistName
        binding.playingFrom.text = song.albumName

        Glide.with(this)
            .load(RetroGlideExtension.getSongModel(song))
            .simpleSongCoverOptions(song)
            .error(R.drawable.default_audio_art)
            .into(binding.ringCover)

        Glide.with(this)
            .load(RetroGlideExtension.getSongModel(song))
            .simpleSongCoverOptions(song)
            .transform(
                BlurTransformation.Builder(requireContext())
                    .blurRadius(PreferenceUtil.blurAmount.toFloat()).build()
            )
            .thumbnail(lastRequest)
            .error(Glide.with(this).load(ColorDrawable(Color.DKGRAY)).fitCenter())
            .also {
                lastRequest = it.clone()
                it.crossfadeListener().into(binding.ringBackground)
            }
        updateFavoriteHeart()
        updateArtistCounter()
        updateLyrics()
    }

    /** "6/155": position of the playing song among the songs of its artist. */
    private fun updateArtistCounter() {
        val song = MusicPlayerRemote.currentSong
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val songs = libraryViewModel.artistById(song.artistId).songs
            val position = songs.indexOfFirst { it.id == song.id } + 1
            withContext(Dispatchers.Main) {
                _binding?.artistCounter?.text =
                    if (songs.isEmpty() || position == 0) "" else "$position/${songs.size}"
            }
        }
    }

    private fun updateFavoriteHeart() {
        val id = MusicPlayerRemote.currentSong.id
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val isFavorite = libraryViewModel.isSongFavorite(id)
            withContext(Dispatchers.Main) {
                _binding?.favoriteButton?.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
            }
        }
    }

    private fun updateLyrics() {
        val song = MusicPlayerRemote.currentSong
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val lrcFile = LyricUtil.getSyncedLyricsFile(song)
            val embedded = if (lrcFile == null) LyricUtil.getEmbeddedSyncedLyrics(song.data) else null
            withContext(Dispatchers.Main) {
                val view = _binding?.ringLyrics ?: return@withContext
                when {
                    lrcFile != null -> view.loadLrc(lrcFile)
                    embedded != null -> view.loadLrc(embedded)
                    else -> {
                        view.reset()
                        view.setLabel(getString(R.string.ring_no_lyrics))
                    }
                }
            }
        }
    }

    private fun updatePlayPause() {
        binding.playPauseButton.setImageResource(
            if (MusicPlayerRemote.isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
        )
    }

    private fun updateShuffle() {
        binding.shuffleButton.setColorFilter(
            if (MusicPlayerRemote.shuffleMode == MusicService.SHUFFLE_MODE_SHUFFLE) Color.WHITE
            else Color.argb(90, 255, 255, 255), PorterDuff.Mode.SRC_IN
        )
    }

    private fun updateRepeat() {
        val mode = MusicPlayerRemote.repeatMode
        binding.repeatButton.setImageResource(
            if (mode == MusicService.REPEAT_MODE_THIS) R.drawable.ic_repeat_one else R.drawable.ic_repeat
        )
        binding.repeatButton.setColorFilter(
            if (mode == MusicService.REPEAT_MODE_NONE) Color.argb(90, 255, 255, 255)
            else Color.WHITE, PorterDuff.Mode.SRC_IN
        )
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
        updateSong()
        updatePlayPause()
        updateShuffle()
        updateRepeat()
    }

    override fun onPause() {
        super.onPause()
        lastRequest = null
        progressViewUpdateHelper.stop()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
        updatePlayPause()
        updateShuffle()
        updateRepeat()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        if (_binding != null) updateSong()
    }

    override fun onPlayStateChanged() {
        super.onPlayStateChanged()
        if (_binding != null) updatePlayPause()
    }

    override fun onShuffleModeChanged() {
        super.onShuffleModeChanged()
        if (_binding != null) updateShuffle()
    }

    override fun onRepeatModeChanged() {
        super.onRepeatModeChanged()
        if (_binding != null) updateRepeat()
    }

    override fun onFavoriteStateChanged() {
        super.onFavoriteStateChanged()
        if (_binding != null) updateFavoriteHeart()
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        val b = _binding ?: return
        if (total > 0) b.progressRing.max = total.toFloat()
        if (!isSeeking) b.progressRing.progress = progress.toFloat().coerceIn(0f, b.progressRing.max)
        b.ringLyrics.updateTime(progress.toLong())
    }

    override fun onProgressChanged(
        circularSeekBar: CircularSeekBar?, progress: Float, fromUser: Boolean
    ) = Unit

    override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {
        isSeeking = true
        progressViewUpdateHelper.stop()
    }

    override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
        isSeeking = false
        seekBar?.let { MusicPlayerRemote.seekTo(it.progress.toInt()) }
        progressViewUpdateHelper.start()
    }

    override fun playerToolbar(): Toolbar = binding.playerToolbar

    override fun onShow() = Unit

    override fun onHide() = Unit

    override fun toolbarIconColor(): Int = Color.WHITE

    override val paletteColor: Int
        get() = Color.BLACK

    override fun onColorChanged(color: MediaNotificationProcessor) = Unit

    override fun onFavoriteToggled() = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
