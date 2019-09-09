package code.name.monkey.retromusic.activities.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.MiniPlayerFragment
import code.name.monkey.retromusic.fragments.NowPlayingScreen
import code.name.monkey.retromusic.fragments.NowPlayingScreen.*
import code.name.monkey.retromusic.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.fragments.player.adaptive.AdaptiveFragment
import code.name.monkey.retromusic.fragments.player.blur.BlurPlayerFragment
import code.name.monkey.retromusic.fragments.player.card.CardFragment
import code.name.monkey.retromusic.fragments.player.cardblur.CardBlurFragment
import code.name.monkey.retromusic.fragments.player.classic.ClassicPlayerFragment
import code.name.monkey.retromusic.fragments.player.color.ColorFragment
import code.name.monkey.retromusic.fragments.player.fit.FitFragment
import code.name.monkey.retromusic.fragments.player.flat.FlatPlayerFragment
import code.name.monkey.retromusic.fragments.player.full.FullPlayerFragment
import code.name.monkey.retromusic.fragments.player.material.MaterialFragment
import code.name.monkey.retromusic.fragments.player.normal.PlayerFragment
import code.name.monkey.retromusic.fragments.player.plain.PlainPlayerFragment
import code.name.monkey.retromusic.fragments.player.simple.SimplePlayerFragment
import code.name.monkey.retromusic.fragments.player.tiny.TinyPlayerFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.views.BottomNavigationBarTinted
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.sliding_music_panel_layout.*

abstract class AbsSlidingMusicPanelActivity : AbsMusicServiceActivity(), SlidingUpPanelLayout.PanelSlideListener, AbsPlayerFragment.Callbacks {
    companion object {
        val TAG: String = AbsSlidingMusicPanelActivity::class.java.simpleName
    }

    private var miniPlayerFragment: MiniPlayerFragment? = null
    private var playerFragment: AbsPlayerFragment? = null
    private var currentNowPlayingScreen: NowPlayingScreen? = null
    private var navigationBarColor: Int = 0
    private var taskColor: Int = 0
    private var lightStatusBar: Boolean = false
    private var lightNavigationBar: Boolean = false
    private var navigationBarColorAnimator: ValueAnimator? = null

    protected abstract fun createContentView(): View

    val panelState: SlidingUpPanelLayout.PanelState?
        get() = slidingLayout.panelState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView())


        chooseFragmentForTheme()
        setupSlidingUpPanel()

        updateTabs()
    }

    override fun onResume() {
        super.onResume()
        if (currentNowPlayingScreen != PreferenceUtil.getInstance(this).nowPlayingScreen) {
            postRecreate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (navigationBarColorAnimator != null) navigationBarColorAnimator!!.cancel() // just in case
    }


    protected fun wrapSlidingMusicPanel(@LayoutRes resId: Int): View {
        @SuppressLint("InflateParams")
        val slidingMusicPanelLayout = layoutInflater.inflate(R.layout.sliding_music_panel_layout, null)
        val contentContainer = slidingMusicPanelLayout.findViewById<ViewGroup>(R.id.mainContentFrame)
        layoutInflater.inflate(resId, contentContainer)
        return slidingMusicPanelLayout
    }

    fun setAntiDragView(antiDragView: View) {
        slidingLayout.setAntiDragView(antiDragView)
    }

    private fun collapsePanel() {
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    fun expandPanel() {
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (miniPlayerFragment!!.view == null) return
        val alpha = 1 - progress
        miniPlayerFragment!!.view!!.alpha = alpha
        // necessary to make the views below clickable
        miniPlayerFragment!!.view!!.visibility = if (alpha == 0f) View.GONE else View.VISIBLE

        bottomNavigationView.translationY = progress * 500
        bottomNavigationView.alpha = alpha
    }

    open fun onPanelCollapsed() {
        // restore values
        super.setLightStatusbar(lightStatusBar)
        super.setTaskDescriptionColor(taskColor)
        super.setNavigationbarColor(navigationBarColor)
        super.setLightNavigationBar(lightNavigationBar)


        playerFragment!!.setMenuVisibility(false)
        playerFragment!!.userVisibleHint = false
        playerFragment!!.onHide()
    }

    open fun onPanelExpanded() {
        val playerFragmentColor = playerFragment!!.paletteColor
        super.setTaskDescriptionColor(playerFragmentColor)

        playerFragment!!.setMenuVisibility(true)
        playerFragment!!.userVisibleHint = true
        playerFragment!!.onShow()
        onPaletteColorChanged()
    }

    private fun setupSlidingUpPanel() {
        slidingLayout.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        slidingLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            onPanelSlide(slidingLayout, 1f)
                            onPanelExpanded()
                        } else if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            onPanelCollapsed()
                        } else {
                            playerFragment!!.onHide()
                        }
                    }
                })

        slidingLayout.addPanelSlideListener(this)

    }

    fun toggleBottomNavigationView(toggle: Boolean) {
        bottomNavigationView.visibility = if (toggle) View.GONE else View.VISIBLE
    }

    fun getBottomNavigationView(): BottomNavigationBarTinted {
        return bottomNavigationView
    }

    fun hideBottomBar(hide: Boolean) {
        val heightOfBar = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val heightOfBarWithTabs = resources.getDimensionPixelSize(R.dimen.mini_player_height_expanded)

        if (hide) {
            slidingLayout.panelHeight = 0
            collapsePanel()
        } else {
            if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
                slidingLayout.panelHeight = if (bottomNavigationView.visibility == View.VISIBLE) heightOfBarWithTabs else heightOfBar
            }
        }
    }

    fun setBottomBarVisibility(gone: Int) {
        bottomNavigationView.visibility = gone
        hideBottomBar(false)
    }

    private fun chooseFragmentForTheme() {
        currentNowPlayingScreen = PreferenceUtil.getInstance(this).nowPlayingScreen

        val fragment: Fragment = when (currentNowPlayingScreen) {
            BLUR -> BlurPlayerFragment()
            ADAPTIVE -> AdaptiveFragment()
            NORMAL -> PlayerFragment()
            CARD -> CardFragment()
            BLUR_CARD -> CardBlurFragment()
            FIT -> FitFragment()
            FLAT -> FlatPlayerFragment()
            FULL -> FullPlayerFragment()
            PLAIN -> PlainPlayerFragment()
            SIMPLE -> SimplePlayerFragment()
            MATERIAL -> MaterialFragment()
            COLOR -> ColorFragment()
            TINY -> TinyPlayerFragment()
            CLASSIC -> ClassicPlayerFragment()
            else -> PlayerFragment()
        } // must implement AbsPlayerFragment
        supportFragmentManager.beginTransaction().replace(R.id.playerFragmentContainer, fragment).commit()
        supportFragmentManager.executePendingTransactions()

        playerFragment = supportFragmentManager.findFragmentById(R.id.playerFragmentContainer) as AbsPlayerFragment
        miniPlayerFragment = supportFragmentManager.findFragmentById(R.id.miniPlayerFragment) as MiniPlayerFragment
        miniPlayerFragment!!.view!!.setOnClickListener { expandPanel() }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        if (!MusicPlayerRemote.playingQueue.isEmpty()) {
            slidingLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    slidingLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
        if (!handleBackPress())
            super.onBackPressed()
    }

    open fun handleBackPress(): Boolean {
        if (slidingLayout.panelHeight != 0 && playerFragment!!.onBackPressed())
            return true
        if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            collapsePanel()
            return true
        }
        return false
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        setMiniPlayerAlphaProgress(slideOffset)
    }

    override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
        when (newState) {
            SlidingUpPanelLayout.PanelState.COLLAPSED -> onPanelCollapsed()
            SlidingUpPanelLayout.PanelState.EXPANDED -> onPanelExpanded()
            SlidingUpPanelLayout.PanelState.ANCHORED -> collapsePanel() // this fixes a bug where the panel would get stuck for some reason
            else -> {
            }
        }
    }

    override fun onPaletteColorChanged() {
        if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            val paletteColor = playerFragment!!.paletteColor
            super.setTaskDescriptionColor(paletteColor)

            val isColorLight = ColorUtil.isColorLight(paletteColor)


            if (PreferenceUtil.getInstance(this).adaptiveColor &&
                    (currentNowPlayingScreen == NORMAL || currentNowPlayingScreen == FLAT)) {
                super.setLightNavigationBar(true)
                super.setLightStatusbar(isColorLight)
            } else if (currentNowPlayingScreen == FULL || currentNowPlayingScreen == CARD ||
                    currentNowPlayingScreen == FIT || currentNowPlayingScreen == CLASSIC ||
                    currentNowPlayingScreen == BLUR || currentNowPlayingScreen == BLUR_CARD) {
                super.setLightStatusbar(false)
                super.setLightNavigationBar(true)
            } else if (currentNowPlayingScreen == COLOR || currentNowPlayingScreen == TINY) {
                super.setNavigationbarColor(paletteColor)
                super.setLightNavigationBar(isColorLight)
                super.setLightStatusbar(isColorLight)
            } else {
                super.setLightStatusbar(ColorUtil.isColorLight(ThemeStore.primaryColor(this)))
                super.setLightNavigationBar(true)
            }
        }
    }

    override fun setLightStatusbar(enabled: Boolean) {
        lightStatusBar = enabled
        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setLightStatusbar(enabled)
        }
    }

    override fun setLightNavigationBar(enabled: Boolean) {
        lightNavigationBar = enabled
        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setLightNavigationBar(enabled)
        }
    }

    override fun setNavigationbarColor(color: Int) {
        navigationBarColor = color
        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (navigationBarColorAnimator != null) navigationBarColorAnimator!!.cancel()
            super.setNavigationbarColor(color)
        }
    }

    override fun setTaskDescriptionColor(color: Int) {
        taskColor = color
        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.setTaskDescriptionColor(color)
        }
    }


    private fun updateTabs() {
        bottomNavigationView.menu.clear()
        val currentTabs = PreferenceUtil.getInstance(this).libraryCategoryInfos
        for (tab in currentTabs) {
            if (tab.visible) {
                val menu = tab.category;
                bottomNavigationView.menu.add(0, menu.id, 0, menu.stringRes)
                        .setIcon(menu.icon)
            }
        }
    }
}