package code.name.monkey.retromusic.activities.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.RetroBottomSheetBehavior
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.whichFragment
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.MiniPlayerFragment
import code.name.monkey.retromusic.fragments.NowPlayingScreen
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.CategoryInfo
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.views.BottomNavigationBarTinted
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.sliding_music_panel_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class AbsSlidingMusicPanelActivity() : AbsMusicServiceActivity() {
    companion object {
        val TAG: String = AbsSlidingMusicPanelActivity::class.java.simpleName
    }

    private val libraryViewModel by viewModel<LibraryViewModel>()
    private lateinit var behavior: RetroBottomSheetBehavior<FrameLayout>
    private var miniPlayerFragment: MiniPlayerFragment? = null
    private var cps: NowPlayingScreen? = null
    private var navigationBarColor: Int = 0
    private var taskColor: Int = 0
    private var lightStatusBar: Boolean = false
    private var lightNavigationBar: Boolean = false
    private var paletteColor: Int = Color.WHITE
    protected abstract fun createContentView(): View
    private val panelState: Int
        get() = behavior.state

    private val bottomSheetCallbackList = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            setMiniPlayerAlphaProgress(slideOffset)
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    onPanelExpanded()
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    onPanelCollapsed()
                }
                else -> {

                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView())
        chooseFragmentForTheme()
        setupSlidingUpPanel()
        addMusicServiceEventListener(libraryViewModel)

        setupBottomSheet()

        libraryViewModel.paletteColorLiveData.observe(this, Observer {
            this.paletteColor = it
            onPaletteColorChanged()
        })
    }

    fun getBottomSheetBehavior() = behavior

    private fun setupBottomSheet() {
        behavior = BottomSheetBehavior.from(slidingPanel) as RetroBottomSheetBehavior
        behavior.addBottomSheetCallback(bottomSheetCallbackList)

        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            setMiniPlayerAlphaProgress(1f)
        }
    }

    override fun onResume() {
        super.onResume()
        if (cps != PreferenceUtil.nowPlayingScreen) {
            postRecreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        behavior.removeBottomSheetCallback(bottomSheetCallbackList)
    }

    protected fun wrapSlidingMusicPanel(@LayoutRes resId: Int): View {
        val slidingMusicPanelLayout =
            layoutInflater.inflate(R.layout.sliding_music_panel_layout, null)
        val contentContainer: ViewGroup =
            slidingMusicPanelLayout.findViewById(R.id.mainContentFrame)
        layoutInflater.inflate(resId, contentContainer)
        return slidingMusicPanelLayout
    }

    fun collapsePanel() {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        setMiniPlayerAlphaProgress(0f)
    }

    fun expandPanel() {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setMiniPlayerAlphaProgress(1f)
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (miniPlayerFragment?.view == null) return
        val alpha = 1 - progress
        miniPlayerFragment?.view?.alpha = alpha
        miniPlayerFragment?.view?.visibility = if (alpha == 0f) View.GONE else View.VISIBLE
        bottomNavigationView.translationY = progress * 500
        bottomNavigationView.alpha = alpha
    }

    open fun onPanelCollapsed() {
        // restore values
        super.setLightStatusbar(lightStatusBar)
        super.setTaskDescriptionColor(taskColor)
        super.setNavigationbarColor(navigationBarColor)
        super.setLightNavigationBar(lightNavigationBar)
    }

    open fun onPanelExpanded() {
        onPaletteColorChanged()
    }

    private fun setupSlidingUpPanel() {
        slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                when (panelState) {
                    BottomSheetBehavior.STATE_EXPANDED -> onPanelExpanded()
                    BottomSheetBehavior.STATE_COLLAPSED -> onPanelCollapsed()
                    else -> {
                        //playerFragment!!.onHide()
                    }
                }
            }
        })
    }

    fun getBottomNavigationView(): BottomNavigationBarTinted {
        return bottomNavigationView
    }

    fun hideBottomBarVisibility(visible: Boolean) {
        bottomNavigationView.isVisible = visible
        hideBottomBar(MusicPlayerRemote.playingQueue.isEmpty())
    }

    private fun hideBottomBar(hide: Boolean) {
        val heightOfBar = bottomNavigationView.height
        val isBottomBarVisible = bottomNavigationView.isVisible

        if (hide) {
            behavior.isHideable = true
            behavior.peekHeight = 0
            collapsePanel()
            ViewCompat.setElevation(bottomNavigationView, 10f)
        } else {
            ViewCompat.setElevation(bottomNavigationView, 10f)
            ViewCompat.setElevation(slidingPanel, 10f)
            behavior.isHideable = false
            behavior.peekHeight = (if (isBottomBarVisible) heightOfBar * 2 else heightOfBar) - 24
        }
    }

    private fun chooseFragmentForTheme() {
        cps = PreferenceUtil.nowPlayingScreen
        miniPlayerFragment = whichFragment<MiniPlayerFragment>(R.id.miniPlayerFragment)
        miniPlayerFragment?.view?.setOnClickListener { expandPanel() }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
            slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    hideBottomBar(false)
                }
            })
        } // don't call hideBottomBar(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        hideBottomBar(MusicPlayerRemote.playingQueue.isEmpty())
    }

    override fun onBackPressed() {
        if (!handleBackPress()) super.onBackPressed()
    }

    open fun handleBackPress(): Boolean {
        if (panelState == BottomSheetBehavior.STATE_EXPANDED) {
            collapsePanel()
            return true
        }
        return false
    }

    private fun onPaletteColorChanged() {
        if (panelState == BottomSheetBehavior.STATE_EXPANDED) {
            super.setTaskDescriptionColor(paletteColor)
            val isColorLight = ColorUtil.isColorLight(paletteColor)
            if (PreferenceUtil.isAdaptiveColor && (cps == Normal || cps == Flat)) {
                super.setLightNavigationBar(true)
                super.setLightStatusbar(isColorLight)
            } else if (cps == Card || cps == Blur || cps == BlurCard) {
                super.setLightStatusbar(false)
                super.setLightNavigationBar(true)
                super.setNavigationbarColor(Color.BLACK)
            } else if (cps == Color || cps == Tiny || cps == Gradient) {
                super.setNavigationbarColor(paletteColor)
                super.setLightNavigationBar(isColorLight)
                super.setLightStatusbar(isColorLight)
            } else if (cps == Full) {
                super.setNavigationbarColor(paletteColor)
                super.setLightNavigationBar(isColorLight)
                super.setLightStatusbar(false)
            } else if (cps == Classic) {
                super.setLightStatusbar(false)
            } else if (cps == Fit) {
                super.setLightStatusbar(false)
            } else {
                super.setLightStatusbar(
                    ColorUtil.isColorLight(
                        ATHUtil.resolveColor(
                            this,
                            android.R.attr.windowBackground
                        )
                    )
                )
                super.setLightNavigationBar(true)
            }
        }
    }

    override fun setLightStatusbar(enabled: Boolean) {
        lightStatusBar = enabled
        if (panelState == BottomSheetBehavior.STATE_COLLAPSED) {
            super.setLightStatusbar(enabled)
        }
    }

    override fun setLightNavigationBar(enabled: Boolean) {
        lightNavigationBar = enabled
        if (panelState == BottomSheetBehavior.STATE_COLLAPSED) {
            super.setLightNavigationBar(enabled)
        }
    }

    override fun setNavigationbarColor(color: Int) {
        navigationBarColor = color
        if (panelState == BottomSheetBehavior.STATE_COLLAPSED) {
            super.setNavigationbarColor(color)
        }
    }

    override fun setTaskDescriptionColor(color: Int) {
        taskColor = color
        if (panelState == BottomSheetBehavior.STATE_COLLAPSED) {
            super.setTaskDescriptionColor(color)
        }
    }

    fun hideBottomNavigation() {
        behavior.isHideable = true
        behavior.peekHeight = 0
        hideBottomBarVisibility(false)
    }

    fun updateTabs() {
        bottomNavigationView.menu.clear()
        val currentTabs: List<CategoryInfo> = PreferenceUtil.libraryCategory
        for (tab in currentTabs) {
            if (tab.visible) {
                val menu = tab.category
                bottomNavigationView.menu.add(0, menu.id, 0, menu.stringRes).setIcon(menu.icon)
            }
        }
        if (bottomNavigationView.menu.size() == 1) {
            bottomNavigationView.hide()
        }
    }
}