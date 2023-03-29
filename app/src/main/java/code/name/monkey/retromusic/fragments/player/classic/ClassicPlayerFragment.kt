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
package code.name.monkey.retromusic.fragments.player.classic

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.PlayingQueueAdapter
import code.name.monkey.retromusic.databinding.FragmentClassicPlayerBinding
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.fragments.MusicSeekSkipTouchListener
import code.name.monkey.retromusic.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.base.goToAlbum
import code.name.monkey.retromusic.fragments.base.goToArtist
import code.name.monkey.retromusic.fragments.other.VolumeFragment
import code.name.monkey.retromusic.fragments.player.PlayerAlbumCoverFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils

class ClassicPlayerFragment : AbsPlayerFragment(R.layout.fragment_classic_player),
    View.OnLayoutChangeListener,
    MusicProgressViewUpdateHelper.Callback {

    private var _binding: FragmentClassicPlayerBinding? = null
    private val binding get() = _binding!!

    private var lastColor: Int = 0
    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private var volumeFragment: VolumeFragment? = null
    private lateinit var shapeDrawable: MaterialShapeDrawable
    private lateinit var wrappedAdapter: RecyclerView.Adapter<*>
    private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null
    private var recyclerViewSwipeManager: RecyclerViewSwipeManager? = null
    private var recyclerViewTouchActionGuardManager: RecyclerViewTouchActionGuardManager? = null
    private var playingQueueAdapter: PlayingQueueAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val bottomSheetCallbackList = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            mainActivity.getBottomSheetBehavior().isDraggable = false
            binding.playerQueueSheet.setContentPadding(
                binding.playerQueueSheet.contentPaddingLeft,
                (slideOffset * binding.statusBar.height).toInt(),
                binding.playerQueueSheet.contentPaddingRight,
                binding.playerQueueSheet.contentPaddingBottom
            )

            shapeDrawable.interpolation = 1 - slideOffset
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED,
                BottomSheetBehavior.STATE_DRAGGING -> {
                    mainActivity.getBottomSheetBehavior().isDraggable = false
                }

                BottomSheetBehavior.STATE_COLLAPSED -> {
                    resetToCurrentPosition()
                    mainActivity.getBottomSheetBehavior().isDraggable = true
                }

                else -> {
                    mainActivity.getBottomSheetBehavior().isDraggable = true
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClassicPlayerBinding.bind(view)
        setupPanel()
        setUpMusicControllers()
        setUpPlayerToolbar()
        hideVolumeIfAvailable()
        setupRecyclerView()

        val coverFragment: PlayerAlbumCoverFragment = whichFragment(R.id.playerAlbumCoverFragment)
        coverFragment.setCallbacks(this)

        getQueuePanel().addBottomSheetCallback(bottomSheetCallbackList)

        shapeDrawable = MaterialShapeDrawable(
            ShapeAppearanceModel.builder(
                requireContext(),
                R.style.ClassicThemeOverLay,
                0
            ).build()
        )
        shapeDrawable.fillColor =
            ColorStateList.valueOf(surfaceColor())
        binding.playerQueueSheet.background = shapeDrawable

        binding.playerQueueSheet.setOnTouchListener { _, _ ->
            mainActivity.getBottomSheetBehavior().isDraggable = false
            getQueuePanel().isDraggable = true
            return@setOnTouchListener false
        }

        ToolbarContentTintHelper.colorizeToolbar(
            binding.playerToolbar,
            Color.WHITE,
            requireActivity()
        )
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (getQueuePanel().state == BottomSheetBehavior.STATE_EXPANDED) {
                    getQueuePanel().state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }

    private fun hideVolumeIfAvailable() {
        if (PreferenceUtil.isVolumeVisibilityMode) {
            childFragmentManager.commit {
                replace(R.id.volumeFragmentContainer, VolumeFragment.newInstance())
            }
            childFragmentManager.executePendingTransactions()
            volumeFragment =
                whichFragment(R.id.volumeFragmentContainer) as VolumeFragment?
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getQueuePanel().removeBottomSheetCallback(bottomSheetCallbackList)
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager?.release()
            recyclerViewDragDropManager = null
        }

        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager?.release()
            recyclerViewSwipeManager = null
        }

        WrapperAdapterUtils.releaseAll(wrappedAdapter)
        _binding = null
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.title.text = song.title
        binding.text.text = song.artistName

        if (PreferenceUtil.isSongInfo) {
            binding.playerControlsContainer.songInfo.text = getSongInfo(song)
            binding.playerControlsContainer.songInfo.show()
        } else {
            binding.playerControlsContainer.songInfo.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        recyclerViewDragDropManager?.cancelDrag()
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
        updatePlayPauseDrawableState()
        updateQueue()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onRepeatModeChanged() {
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        updateShuffleState()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
        updateQueuePosition()
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        updateQueue()
    }

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        libraryViewModel.updateColor(color.backgroundColor)

        lastPlaybackControlsColor = color.primaryTextColor
        lastDisabledPlaybackControlsColor = ColorUtil.withAlpha(color.primaryTextColor, 0.3f)

        binding.playerContainer.setBackgroundColor(color.backgroundColor)
        binding.playerControlsContainer.songInfo.setTextColor(color.primaryTextColor)
        binding.playerQueueSubHeader.setTextColor(color.primaryTextColor)

        binding.playerControlsContainer.songCurrentProgress.setTextColor(lastPlaybackControlsColor)
        binding.playerControlsContainer.songTotalTime.setTextColor(lastPlaybackControlsColor)

        ViewUtil.setProgressDrawable(
            binding.playerControlsContainer.progressSlider,
            color.primaryTextColor,
            true
        )
        volumeFragment?.setTintableColor(color.primaryTextColor)

        TintHelper.setTintAuto(
            binding.playerControlsContainer.playPauseButton,
            color.primaryTextColor,
            true
        )
        TintHelper.setTintAuto(
            binding.playerControlsContainer.playPauseButton,
            color.backgroundColor,
            false
        )

        updateRepeatState()
        updateShuffleState()
        updatePrevNextColor()

        ToolbarContentTintHelper.colorizeToolbar(
            binding.playerToolbar,
            Color.WHITE,
            requireActivity()
        )
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.playerControlsContainer.progressSlider.max = total

        val animator = ObjectAnimator.ofInt(
            binding.playerControlsContainer.progressSlider,
            "progress",
            progress
        )
        animator.duration = AbsPlayerControlsFragment.SLIDER_ANIMATION_TIME
        animator.interpolator = LinearInterpolator()
        animator.start()

        binding.playerControlsContainer.songTotalTime.text =
            MusicUtil.getReadableDurationString(total.toLong())
        binding.playerControlsContainer.songCurrentProgress.text =
            MusicUtil.getReadableDurationString(progress.toLong())
    }

    private fun updateQueuePosition() {
        playingQueueAdapter?.setCurrent(MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun updateQueue() {
        playingQueueAdapter?.swapDataSet(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun resetToCurrentPosition() {
        binding.recyclerView.stopScroll()
        linearLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    private fun getQueuePanel(): BottomSheetBehavior<MaterialCardView> {
        return from(binding.playerQueueSheet)
    }

    private fun setupPanel() {
        if (!binding.playerContainer.isLaidOut || binding.playerContainer.isLayoutRequested) {
            binding.playerContainer.addOnLayoutChangeListener(this)
            return
        }
        val height = binding.playerContainer.height
        val width = binding.playerContainer.width
        val finalHeight = height - width
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.inflateMenu(R.menu.menu_player)
        binding.playerToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.playerToolbar.setOnMenuItemClickListener(this)

        ToolbarContentTintHelper.colorizeToolbar(
            binding.playerToolbar,
            Color.WHITE,
            requireActivity()
        )
    }

    private fun setupRecyclerView() {
        playingQueueAdapter = PlayingQueueAdapter(
            requireActivity() as AppCompatActivity,
            MusicPlayerRemote.playingQueue.toMutableList(),
            MusicPlayerRemote.position,
            R.layout.item_queue
        )
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerViewTouchActionGuardManager = RecyclerViewTouchActionGuardManager()
        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewSwipeManager = RecyclerViewSwipeManager()

        val animator = DraggableItemAnimator()
        animator.supportsChangeAnimations = false
        wrappedAdapter =
            recyclerViewDragDropManager?.createWrappedAdapter(playingQueueAdapter!!) as RecyclerView.Adapter<*>
        wrappedAdapter =
            recyclerViewSwipeManager?.createWrappedAdapter(wrappedAdapter) as RecyclerView.Adapter<*>
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = wrappedAdapter
        binding.recyclerView.itemAnimator = animator
        recyclerViewTouchActionGuardManager?.attachRecyclerView(binding.recyclerView)
        recyclerViewDragDropManager?.attachRecyclerView(binding.recyclerView)
        recyclerViewSwipeManager?.attachRecyclerView(binding.recyclerView)

        linearLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    private fun setUpProgressSlider() {
        binding.playerControlsContainer.progressSlider.setOnSeekBarChangeListener(object :
            SimpleOnSeekbarChangeListener() {
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

    private fun setUpPlayPauseFab() {
        binding.playerControlsContainer.playPauseButton.setOnClickListener(
            PlayPauseButtonOnClickHandler()
        )
    }

    private fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            binding.playerControlsContainer.playPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            binding.playerControlsContainer.playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPrevNext() {
        updatePrevNextColor()
        binding.playerControlsContainer.nextButton.setOnTouchListener(
            MusicSeekSkipTouchListener(
                requireActivity(),
                true
            )
        )
        binding.playerControlsContainer.previousButton.setOnTouchListener(
            MusicSeekSkipTouchListener(
                requireActivity(),
                false
            )
        )
    }

    private fun updatePrevNextColor() {
        binding.playerControlsContainer.nextButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.playerControlsContainer.previousButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setUpShuffleButton() {
        binding.playerControlsContainer.shuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE ->
                binding.playerControlsContainer.shuffleButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )

            else -> binding.playerControlsContainer.shuffleButton.setColorFilter(
                lastDisabledPlaybackControlsColor,
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun setUpRepeatButton() {
        binding.playerControlsContainer.repeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                binding.playerControlsContainer.repeatButton.setImageResource(R.drawable.ic_repeat)
                binding.playerControlsContainer.repeatButton.setColorFilter(
                    lastDisabledPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }

            MusicService.REPEAT_MODE_ALL -> {
                binding.playerControlsContainer.repeatButton.setImageResource(R.drawable.ic_repeat)
                binding.playerControlsContainer.repeatButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }

            MusicService.REPEAT_MODE_THIS -> {
                binding.playerControlsContainer.repeatButton.setImageResource(R.drawable.ic_repeat_one)
                binding.playerControlsContainer.repeatButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        val height = binding.playerContainer.height
        val width = binding.playerContainer.width
        val finalHeight = height - (binding.playerControlsContainer.root.height + width)
        val panel = getQueuePanel()
        panel.peekHeight = finalHeight
    }
}
