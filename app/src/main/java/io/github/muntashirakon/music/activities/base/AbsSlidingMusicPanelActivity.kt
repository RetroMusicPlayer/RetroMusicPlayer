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
package io.github.muntashirakon.music.activities.base

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.PathInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import code.name.monkey.appthemehelper.util.VersionUtils
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.databinding.SlidingMusicPanelLayoutBinding
import io.github.muntashirakon.music.extensions.*
import io.github.muntashirakon.music.fragments.LibraryViewModel
import io.github.muntashirakon.music.fragments.NowPlayingScreen
import io.github.muntashirakon.music.fragments.NowPlayingScreen.*
import io.github.muntashirakon.music.fragments.base.AbsPlayerFragment
import io.github.muntashirakon.music.fragments.other.MiniPlayerFragment
import io.github.muntashirakon.music.fragments.player.adaptive.AdaptiveFragment
import io.github.muntashirakon.music.fragments.player.blur.BlurPlayerFragment
import io.github.muntashirakon.music.fragments.player.card.CardFragment
import io.github.muntashirakon.music.fragments.player.cardblur.CardBlurFragment
import io.github.muntashirakon.music.fragments.player.circle.CirclePlayerFragment
import io.github.muntashirakon.music.fragments.player.classic.ClassicPlayerFragment
import io.github.muntashirakon.music.fragments.player.color.ColorFragment
import io.github.muntashirakon.music.fragments.player.fit.FitFragment
import io.github.muntashirakon.music.fragments.player.flat.FlatPlayerFragment
import io.github.muntashirakon.music.fragments.player.full.FullPlayerFragment
import io.github.muntashirakon.music.fragments.player.gradient.GradientPlayerFragment
import io.github.muntashirakon.music.fragments.player.material.MaterialFragment
import io.github.muntashirakon.music.fragments.player.md3.MD3PlayerFragment
import io.github.muntashirakon.music.fragments.player.normal.PlayerFragment
import io.github.muntashirakon.music.fragments.player.peek.PeekPlayerFragment
import io.github.muntashirakon.music.fragments.player.plain.PlainPlayerFragment
import io.github.muntashirakon.music.fragments.player.simple.SimplePlayerFragment
import io.github.muntashirakon.music.fragments.player.tiny.TinyPlayerFragment
import io.github.muntashirakon.music.fragments.queue.PlayingQueueFragment
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.model.CategoryInfo
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.ViewUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import org.koin.androidx.viewmodel.ext.android.viewModel


abstract class AbsSlidingMusicPanelActivity : AbsMusicServiceActivity() {
    companion object {
        val TAG: String = AbsSlidingMusicPanelActivity::class.java.simpleName
    }

    var fromNotification = false
    private var windowInsets: WindowInsetsCompat? = null
    protected val libraryViewModel by viewModel<LibraryViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private var playerFragment: AbsPlayerFragment? = null
    private var miniPlayerFragment: MiniPlayerFragment? = null
    private var nowPlayingScreen: NowPlayingScreen? = null
    private var taskColor: Int = 0
    private var paletteColor: Int = Color.WHITE
    private var navigationBarColor = 0
    protected abstract fun createContentView(): SlidingMusicPanelLayoutBinding
    private val panelState: Int
        get() = bottomSheetBehavior.state
    private lateinit var binding: SlidingMusicPanelLayoutBinding
    private var isInOneTabMode = false

    private var navigationBarColorAnimator: ValueAnimator? = null
    private val argbEvaluator: ArgbEvaluator = ArgbEvaluator()

    private val bottomSheetCallbackList = object : BottomSheetCallback() {

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            setMiniPlayerAlphaProgress(slideOffset)
            navigationBarColorAnimator?.cancel()
            setNavigationBarColorPreOreo(
                argbEvaluator.evaluate(
                    slideOffset,
                    surfaceColor(),
                    navigationBarColor
                ) as Int
            )
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                STATE_EXPANDED -> {
                    onPanelExpanded()
                    if (PreferenceUtil.lyricsScreenOn && PreferenceUtil.showLyrics) {
                        keepScreenOn(true)
                    }
                }
                STATE_COLLAPSED -> {
                    onPanelCollapsed()
                    if ((PreferenceUtil.lyricsScreenOn && PreferenceUtil.showLyrics) || !PreferenceUtil.isScreenOnEnabled) {
                        keepScreenOn(false)
                    }
                }
                STATE_SETTLING, STATE_DRAGGING -> {
                    if (fromNotification) {
                        binding.bottomNavigationView.bringToFront()
                        fromNotification = false
                    }
                }
                STATE_HIDDEN -> {
                    MusicPlayerRemote.clearQueue()
                }
                else -> {
                    println("Do a flip")
                }
            }
        }
    }

    fun getBottomSheetBehavior() = bottomSheetBehavior

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createContentView()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { _, insets ->
            windowInsets = insets
            insets
        }
        chooseFragmentForTheme()
        setupSlidingUpPanel()
        setupBottomSheet()
        updateColor()
        if (!PreferenceUtil.materialYou) {
            binding.slidingPanel.backgroundTintList = ColorStateList.valueOf(darkAccentColor())
            bottomNavigationView.backgroundTintList = ColorStateList.valueOf(darkAccentColor())
        }

        navigationBarColor = surfaceColor()
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = from(binding.slidingPanel)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallbackList)
        bottomSheetBehavior.isHideable = PreferenceUtil.swipeDownToDismiss
        setMiniPlayerAlphaProgress(0F)
    }

    override fun onResume() {
        super.onResume()
        if (nowPlayingScreen != PreferenceUtil.nowPlayingScreen) {
            postRecreate()
        }
        if (bottomSheetBehavior.state == STATE_EXPANDED) {
            setMiniPlayerAlphaProgress(1f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallbackList)
    }

    protected fun wrapSlidingMusicPanel(): SlidingMusicPanelLayoutBinding {
        return SlidingMusicPanelLayoutBinding.inflate(layoutInflater)
    }

    fun collapsePanel() {
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

    fun expandPanel() {
        bottomSheetBehavior.state = STATE_EXPANDED
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (progress < 0) return
        val alpha = 1 - progress
        miniPlayerFragment?.view?.alpha = 1 - (progress / 0.2F)
        miniPlayerFragment?.view?.isGone = alpha == 0f
        binding.bottomNavigationView.translationY = progress * 500
        binding.bottomNavigationView.alpha = alpha
        binding.playerFragmentContainer.alpha = (progress - 0.2F) / 0.2F
    }

    private fun animateNavigationBarColor(color: Int) {
        if (VersionUtils.hasOreo()) return
        navigationBarColorAnimator?.cancel()
        navigationBarColorAnimator = ValueAnimator
            .ofArgb(window.navigationBarColor, color).apply {
                duration = ViewUtil.RETRO_MUSIC_ANIM_TIME.toLong()
                interpolator = PathInterpolator(0.4f, 0f, 1f, 1f)
                addUpdateListener { animation: ValueAnimator ->
                    setNavigationBarColorPreOreo(
                        animation.animatedValue as Int
                    )
                }
                start()
            }
    }

    open fun onPanelCollapsed() {
        setMiniPlayerAlphaProgress(0F)
        // restore values
        animateNavigationBarColor(surfaceColor())
        setLightStatusBarAuto()
        setLightNavigationBarAuto()
        setTaskDescriptionColor(taskColor)
        playerFragment?.onHide()
    }

    open fun onPanelExpanded() {
        setMiniPlayerAlphaProgress(1F)
        onPaletteColorChanged()
        playerFragment?.onShow()
    }

    private fun setupSlidingUpPanel() {
        binding.slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (nowPlayingScreen != Peek) {
                    binding.slidingPanel.updateLayoutParams<ViewGroup.LayoutParams> {
                        height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                }
                when (panelState) {
                    STATE_EXPANDED -> onPanelExpanded()
                    STATE_COLLAPSED -> onPanelCollapsed()
                    else -> {
                        // playerFragment!!.onHide()
                    }
                }
            }
        })
    }

    val bottomNavigationView get() = binding.bottomNavigationView

    val slidingPanel get() = binding.slidingPanel

    override fun onServiceConnected() {
        super.onServiceConnected()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
            binding.slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    hideBottomSheet(false)
                }
            })
        } // don't call hideBottomSheet(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        // Mini player should be hidden in Playing Queue
        // it may pop up if hideBottomSheet is called
        if (currentFragment(R.id.fragment_container) !is PlayingQueueFragment) {
            hideBottomSheet(MusicPlayerRemote.playingQueue.isEmpty())
        }
    }

    override fun onBackPressed() {
        if (!handleBackPress()) super.onBackPressed()
    }

    private fun handleBackPress(): Boolean {
        if (bottomSheetBehavior.peekHeight != 0 && playerFragment!!.onBackPressed()) return true
        if (panelState == STATE_EXPANDED) {
            collapsePanel()
            return true
        }
        return false
    }

    private fun onPaletteColorChanged() {
        if (panelState == STATE_EXPANDED) {
            navigationBarColor = surfaceColor()
            setTaskDescColor(paletteColor)
            val isColorLight = paletteColor.isColorLight
            if (PreferenceUtil.isAdaptiveColor && (nowPlayingScreen == Normal || nowPlayingScreen == Flat || nowPlayingScreen == Material)) {
                setLightNavigationBar(true)
                setLightStatusBar(isColorLight)
            } else if (nowPlayingScreen == Card || nowPlayingScreen == Blur || nowPlayingScreen == BlurCard) {
                animateNavigationBarColor(Color.BLACK)
                navigationBarColor = Color.BLACK
                setLightStatusBar(false)
                setLightNavigationBar(true)
            } else if (nowPlayingScreen == Color || nowPlayingScreen == Tiny || nowPlayingScreen == Gradient) {
                animateNavigationBarColor(paletteColor)
                navigationBarColor = paletteColor
                setLightNavigationBar(isColorLight)
                setLightStatusBar(isColorLight)
            } else if (nowPlayingScreen == Full) {
                animateNavigationBarColor(paletteColor)
                navigationBarColor = paletteColor
                setLightNavigationBar(isColorLight)
                setLightStatusBar(false)
            } else if (nowPlayingScreen == Classic) {
                setLightStatusBar(false)
            } else if (nowPlayingScreen == Fit) {
                setLightStatusBar(false)
            }
        }
    }

    private fun setTaskDescColor(color: Int) {
        taskColor = color
        if (panelState == STATE_COLLAPSED) {
            setTaskDescriptionColor(color)
        }
    }

    fun updateTabs() {
        binding.bottomNavigationView.menu.clear()
        val currentTabs: List<CategoryInfo> = PreferenceUtil.libraryCategory
        for (tab in currentTabs) {
            if (tab.visible) {
                val menu = tab.category
                binding.bottomNavigationView.menu.add(0, menu.id, 0, menu.stringRes)
                    .setIcon(menu.icon)
            }
        }
        if (binding.bottomNavigationView.menu.size() == 1) {
            isInOneTabMode = true
            binding.bottomNavigationView.hide()
        }
    }

    private fun updateColor() {
        libraryViewModel.paletteColor.observe(this) { color ->
            this.paletteColor = color
            onPaletteColorChanged()
        }
    }

    fun setBottomNavVisibility(
        visible: Boolean,
        animate: Boolean = false,
        hideBottomSheet: Boolean = MusicPlayerRemote.playingQueue.isEmpty(),
    ) {
        if (!ViewCompat.isLaidOut(bottomNavigationView)) {
            return
        }
        if (isInOneTabMode) {
            hideBottomSheet(
                hide = hideBottomSheet,
                animate = animate,
                isBottomNavVisible = false
            )
            return
        }
        val mAnimate = animate && bottomSheetBehavior.state == STATE_COLLAPSED
        if (mAnimate) {
            if (visible) {
                binding.bottomNavigationView.bringToFront()
                binding.bottomNavigationView.show()
            } else {
                binding.bottomNavigationView.hide()
            }
        } else {
            binding.bottomNavigationView.isVisible = false
            if (visible && bottomSheetBehavior.state != STATE_EXPANDED) {
                binding.bottomNavigationView.bringToFront()
            }
        }
        hideBottomSheet(
            hide = hideBottomSheet,
            animate = animate,
            isBottomNavVisible = visible
        )
    }

    fun hideBottomSheet(
        hide: Boolean,
        animate: Boolean = false,
        isBottomNavVisible: Boolean = bottomNavigationView.isVisible,
    ) {
        val heightOfBar =
            windowInsets.safeGetBottomInsets() +
                    if (MusicPlayerRemote.isCasting) dip(R.dimen.cast_mini_player_height) else dip(R.dimen.mini_player_height)
        val heightOfBarWithTabs = heightOfBar + dip(R.dimen.bottom_nav_height)
        if (hide) {
            bottomSheetBehavior.peekHeight = -windowInsets.safeGetBottomInsets()
            bottomSheetBehavior.state = STATE_COLLAPSED
            libraryViewModel.setFabMargin(
                this,
                if (isBottomNavVisible) dip(R.dimen.bottom_nav_height) else 0
            )
        } else {
            if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
                binding.slidingPanel.elevation = 0F
                binding.bottomNavigationView.elevation = 5F
                if (isBottomNavVisible) {
                    println("List")
                    if (animate) {
                        bottomSheetBehavior.peekHeightAnimate(heightOfBarWithTabs)
                    } else {
                        bottomSheetBehavior.peekHeight = heightOfBarWithTabs
                    }
                    libraryViewModel.setFabMargin(this, dip(R.dimen.mini_player_height_expanded))
                } else {
                    println("Details")
                    if (animate) {
                        bottomSheetBehavior.peekHeightAnimate(heightOfBar).doOnEnd {
                            binding.slidingPanel.bringToFront()
                        }
                    } else {
                        bottomSheetBehavior.peekHeight = heightOfBar
                        binding.slidingPanel.bringToFront()
                    }
                    libraryViewModel.setFabMargin(this, dip(R.dimen.mini_player_height))
                }
            }
        }
    }

    fun setAllowDragging(allowDragging: Boolean) {
        bottomSheetBehavior.isDraggable = allowDragging
        hideBottomSheet(false)
    }

    private fun chooseFragmentForTheme() {
        nowPlayingScreen = PreferenceUtil.nowPlayingScreen

        val fragment: Fragment = when (nowPlayingScreen) {
            Blur -> BlurPlayerFragment()
            Adaptive -> AdaptiveFragment()
            Normal -> PlayerFragment()
            Card -> CardFragment()
            BlurCard -> CardBlurFragment()
            Fit -> FitFragment()
            Flat -> FlatPlayerFragment()
            Full -> FullPlayerFragment()
            Plain -> PlainPlayerFragment()
            Simple -> SimplePlayerFragment()
            Material -> MaterialFragment()
            Color -> ColorFragment()
            Gradient -> GradientPlayerFragment()
            Tiny -> TinyPlayerFragment()
            Peek -> PeekPlayerFragment()
            Circle -> CirclePlayerFragment()
            Classic -> ClassicPlayerFragment()
            MD3 -> MD3PlayerFragment()
            else -> PlayerFragment()
        } // must implement AbsPlayerFragment
        supportFragmentManager.commit {
            replace(R.id.playerFragmentContainer, fragment)
        }
        supportFragmentManager.executePendingTransactions()
        playerFragment = whichFragment<AbsPlayerFragment>(R.id.playerFragmentContainer)
        miniPlayerFragment = whichFragment<MiniPlayerFragment>(R.id.miniPlayerFragment)
        miniPlayerFragment?.view?.setOnClickListener { expandPanel() }
    }
}
