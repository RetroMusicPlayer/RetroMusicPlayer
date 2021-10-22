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
package code.name.monkey.retromusic.activities.base

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.RetroBottomSheetBehavior
import code.name.monkey.retromusic.databinding.SlidingMusicPanelLayoutBinding
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.NowPlayingScreen
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.other.MiniPlayerFragment
import code.name.monkey.retromusic.fragments.player.adaptive.AdaptiveFragment
import code.name.monkey.retromusic.fragments.player.blur.BlurPlayerFragment
import code.name.monkey.retromusic.fragments.player.card.CardFragment
import code.name.monkey.retromusic.fragments.player.cardblur.CardBlurFragment
import code.name.monkey.retromusic.fragments.player.circle.CirclePlayerFragment
import code.name.monkey.retromusic.fragments.player.classic.ClassicPlayerFragment
import code.name.monkey.retromusic.fragments.player.color.ColorFragment
import code.name.monkey.retromusic.fragments.player.fit.FitFragment
import code.name.monkey.retromusic.fragments.player.flat.FlatPlayerFragment
import code.name.monkey.retromusic.fragments.player.full.FullPlayerFragment
import code.name.monkey.retromusic.fragments.player.gradient.GradientPlayerFragment
import code.name.monkey.retromusic.fragments.player.material.MaterialFragment
import code.name.monkey.retromusic.fragments.player.normal.PlayerFragment
import code.name.monkey.retromusic.fragments.player.peak.PeakPlayerFragment
import code.name.monkey.retromusic.fragments.player.plain.PlainPlayerFragment
import code.name.monkey.retromusic.fragments.player.simple.SimplePlayerFragment
import code.name.monkey.retromusic.fragments.player.tiny.TinyPlayerFragment
import code.name.monkey.retromusic.fragments.queue.PlayingQueueFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.CategoryInfo
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class AbsSlidingMusicPanelActivity : AbsMusicServiceActivity() {
    companion object {
        val TAG: String = AbsSlidingMusicPanelActivity::class.java.simpleName
        var fromNotification: Boolean = false
    }

    private var windowInsets: WindowInsetsCompat? = null
    private var bottomNavAnimator: Animator? = null
    protected val libraryViewModel by viewModel<LibraryViewModel>()
    private lateinit var bottomSheetBehavior: RetroBottomSheetBehavior<FrameLayout>
    private var playerFragment: AbsPlayerFragment? = null
    private var miniPlayerFragment: MiniPlayerFragment? = null
    private var nowPlayingScreen: NowPlayingScreen? = null
    private var taskColor: Int = 0
    private var lightStatusBar: Boolean = false
    protected abstract fun createContentView(): SlidingMusicPanelLayoutBinding
    private val panelState: Int
        get() = bottomSheetBehavior.state
    private lateinit var binding: SlidingMusicPanelLayoutBinding
    private val bottomSheetCallbackList = object : BottomSheetCallback() {

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            setMiniPlayerAlphaProgress(slideOffset)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                STATE_EXPANDED -> {
                    onPanelExpanded()
                }
                STATE_COLLAPSED -> {
                    onPanelCollapsed()
                    if (fromNotification) {
                        hideBottomBar(MusicPlayerRemote.playingQueue.isEmpty())
                        fromNotification = false
                    }
                }
                STATE_SETTLING, STATE_DRAGGING -> {
                    if (fromNotification) {
                        getBottomNavigationView().isVisible = true
                    }
                }
                else -> {
                    println("Do something")
                }
            }
            println(bottomSheetBehavior.peekHeight)
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
        //binding.fragmentContainer.drawAboveNavBar()
        binding.bottomNavigationView.drawAboveNavBarWithPadding()
        if (RetroUtil.isLandscape()) {
            binding.slidingPanel.drawAboveNavBarWithPadding(true)
        }
        chooseFragmentForTheme()
        setupSlidingUpPanel()
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = from(binding.slidingPanel) as RetroBottomSheetBehavior
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallbackList)
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
        setMiniPlayerAlphaProgress(1.0f)
        bottomSheetBehavior.state = STATE_EXPANDED
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (progress < 0) return
        val alpha = 1 - progress
        miniPlayerFragment?.view?.alpha = 1 - (progress / 0.2F)
        miniPlayerFragment?.view?.visibility = if (alpha == 0f) View.GONE else View.VISIBLE
        binding.bottomNavigationView.translationY = progress * 500
        binding.bottomNavigationView.alpha = alpha
        binding.playerFragmentContainer.alpha = (progress - 0.2F) / 0.2F
    }

    open fun onPanelCollapsed() {
        // restore values
        super.setLightStatusbarAuto(surfaceColor())
        super.setTaskDescriptionColor(taskColor)
    }

    open fun onPanelExpanded() {
        if (nowPlayingScreen == Blur) {
            super.setLightStatusbar(false)
        } else {
            super.setLightStatusbarAuto(surfaceColor())
        }
    }

    private fun setupSlidingUpPanel() {
        binding.slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (nowPlayingScreen != Peak) {
                    val params = binding.slidingPanel.layoutParams as ViewGroup.LayoutParams
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT
                    binding.slidingPanel.layoutParams = params
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

    fun getBottomNavigationView(): BottomNavigationView {
        return binding.bottomNavigationView
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
            binding.slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    hideBottomBar(false)
                }
            })
        } // don't call hideBottomBar(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        // Mini player should be hidden in Playing Queue
        // it may pop up if hideBottomBar is called
        if (currentFragment(R.id.fragment_container) !is PlayingQueueFragment) {
            hideBottomBar(MusicPlayerRemote.playingQueue.isEmpty())
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

    override fun setLightStatusbar(enabled: Boolean) {
        lightStatusBar = enabled
        if (panelState == STATE_COLLAPSED) {
            super.setLightStatusbar(enabled)
        }
    }

    override fun setTaskDescriptionColor(color: Int) {
        taskColor = color
        if (panelState == STATE_COLLAPSED) {
            super.setTaskDescriptionColor(color)
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
            binding.bottomNavigationView.hide()
        }
    }

    fun setBottomBarVisibility(visible: Boolean) {
        binding.bottomNavigationView.isVisible = visible
        hideBottomBar(MusicPlayerRemote.playingQueue.isEmpty())
    }

    fun hideBottomBar(hide: Boolean) {
        val heightOfBar =
            windowInsets.safeGetBottomInsets() +
                    if (MusicPlayerRemote.isCasting) dip(R.dimen.cast_mini_player_height) else dip(R.dimen.mini_player_height)
        val heightOfBarWithTabs = heightOfBar + dip(R.dimen.bottom_nav_height)
        val isVisible = binding.bottomNavigationView.isVisible
        if (hide) {
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.peekHeight = 0
            bottomSheetBehavior.state = STATE_HIDDEN
            libraryViewModel.setFabMargin(if (isVisible) dip(R.dimen.bottom_nav_height) else 0)
            ViewCompat.setElevation(binding.slidingPanel, 0f)
            ViewCompat.setElevation(binding.bottomNavigationView, 10f)
        } else {
            if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
                bottomSheetBehavior.isHideable = false
                if (bottomSheetBehavior.state == STATE_HIDDEN)
                    bottomSheetBehavior.state = STATE_EXPANDED
                ViewCompat.setElevation(binding.slidingPanel, 10f)
                ViewCompat.setElevation(binding.bottomNavigationView, 10f)
                if (isVisible) {
                    println("List")
                    bottomSheetBehavior.peekHeightAnimate(heightOfBarWithTabs)
                    bottomNavAnimator?.end()
                    bottomNavAnimator = binding.bottomNavigationView.translateYAnimate(0F)
                    libraryViewModel.setFabMargin(heightOfBarWithTabs - windowInsets.safeGetBottomInsets())
                } else {
                    println("Details")
                    bottomSheetBehavior.peekHeightAnimate(heightOfBar)
                    bottomNavAnimator?.end()
                    bottomNavAnimator =
                        getBottomNavigationView().translateYAnimate(dip(R.dimen.bottom_nav_height).toFloat())
                    libraryViewModel.setFabMargin(heightOfBar - windowInsets.safeGetBottomInsets())
                }
            }
        }
    }

    fun setAllowDragging(allowDragging: Boolean) {
        bottomSheetBehavior.setAllowDragging(allowDragging)
        hideBottomBar(false)
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
            Peak -> PeakPlayerFragment()
            Circle -> CirclePlayerFragment()
            Classic -> ClassicPlayerFragment()
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
