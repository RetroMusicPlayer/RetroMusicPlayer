package code.name.monkey.retromusic.ui.activities.base

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.ui.fragments.MiniPlayerFragment
import code.name.monkey.retromusic.ui.fragments.NowPlayingScreen
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.adaptive.AdaptiveFragment
import code.name.monkey.retromusic.ui.fragments.player.blur.BlurPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.card.CardFragment
import code.name.monkey.retromusic.ui.fragments.player.cardblur.CardBlurFragment
import code.name.monkey.retromusic.ui.fragments.player.color.ColorFragment
import code.name.monkey.retromusic.ui.fragments.player.fit.FitFragment
import code.name.monkey.retromusic.ui.fragments.player.flat.FlatPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.full.FullPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.material.MaterialFragment
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.plain.PlainPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.simple.SimplePlayerFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.views.BottomNavigationBarTinted
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import kotlinx.android.synthetic.main.sliding_music_panel_layout.*

abstract class AbsSlidingMusicPanelActivity protected constructor() : AbsMusicServiceActivity(), SlidingUpPanelLayout.PanelSlideListener, AbsPlayerFragment.Callbacks {


    private var miniPlayerFragment: MiniPlayerFragment? = null
    private var playerFragment: AbsPlayerFragment? = null
    private var currentNowPlayingScreen: NowPlayingScreen? = null
    private var navigationbarColor: Int = 0
    private var taskColor: Int = 0
    private var lightStatusbar: Boolean = false
    private var lightNavigationBar: Boolean = false
    private var navigationBarColorAnimator: ValueAnimator? = null
    private val argbEvaluator = ArgbEvaluator()

    val panelState: SlidingUpPanelLayout.PanelState?
        get() = slidingLayout.panelState

    private val isOneOfTheseThemes: Boolean
        get() = (currentNowPlayingScreen == NowPlayingScreen.ADAPTIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView())


        chooseFragmentForTheme()
        setupSlidingUpPanel()
    }

    fun setBottomBarVisibility(gone: Int) {
        bottomNavigationView.visibility = gone
        hideBottomBar(false)
    }

    protected abstract fun createContentView(): View

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

    fun hideBottomBar(hide: Boolean) {
        val heightOfBar = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val heightOfBarWithTabs = resources.getDimensionPixelSize(R.dimen.mini_player_height_expanded)

        if (hide) {
            slidingLayout.panelHeight = 0
            collapsePanel()
        } else {
            if (!MusicPlayerRemote.playingQueue.isEmpty()) {
                slidingLayout.panelHeight = if (bottomNavigationView.visibility == View.VISIBLE) heightOfBarWithTabs else heightOfBar
            }
        }
    }

    protected fun wrapSlidingMusicPanel(@LayoutRes resId: Int): View {
        @SuppressLint("InflateParams")
        val slidingMusicPanelLayout = layoutInflater.inflate(R.layout.sliding_music_panel_layout, null)
        val contentContainer = slidingMusicPanelLayout.findViewById<ViewGroup>(R.id.mainContentFrame)
        layoutInflater.inflate(resId, contentContainer)
        return slidingMusicPanelLayout
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

    fun toggleBottomNavigationView(toggle: Boolean) {
        bottomNavigationView.visibility = if (toggle) View.GONE else View.VISIBLE
    }

    fun getBottomNavigationView(): BottomNavigationBarTinted {
        return bottomNavigationView
    }

    private fun setupSlidingUpPanel() {
        slidingLayout.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        slidingLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        if (panelState == PanelState.EXPANDED) {
                            onPanelSlide(slidingLayout, 1f)
                            onPanelExpanded()
                        } else if (panelState == PanelState.COLLAPSED) {
                            onPanelCollapsed()
                        } else {
                            playerFragment!!.onHide()
                        }
                    }
                })

        slidingLayout.addPanelSlideListener(this)

    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        bottomNavigationView.translationY = slideOffset * 400
        setMiniPlayerAlphaProgress(slideOffset)
    }

    override fun onPanelStateChanged(panel: View, previousState: PanelState, newState: PanelState) {
        when (newState) {
            SlidingUpPanelLayout.PanelState.COLLAPSED -> onPanelCollapsed()
            SlidingUpPanelLayout.PanelState.EXPANDED -> onPanelExpanded()
            SlidingUpPanelLayout.PanelState.ANCHORED -> collapsePanel() // this fixes a bug where the panel would get stuck for some reason
            else -> {
            }
        }
    }

    open fun onPanelCollapsed() {
        // restore values
        super.setLightStatusbar(lightStatusbar)
        super.setTaskDescriptionColor(taskColor)
        super.setNavigationbarColor(navigationbarColor)
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

    private fun setMiniPlayerAlphaProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float) {
        if (miniPlayerFragment!!.view == null) return
        val alpha = 1 - progress
        miniPlayerFragment!!.view!!.alpha = alpha
        // necessary to make the views below clickable
        miniPlayerFragment!!.view!!.visibility = if (alpha == 0f) View.GONE else View.VISIBLE
    }

    private fun chooseFragmentForTheme() {
        currentNowPlayingScreen = PreferenceUtil.getInstance().nowPlayingScreen

        val fragment: Fragment = when (currentNowPlayingScreen) {
            NowPlayingScreen.BLUR -> BlurPlayerFragment()
            NowPlayingScreen.ADAPTIVE -> AdaptiveFragment()
            NowPlayingScreen.NORMAL -> PlayerFragment()
            NowPlayingScreen.CARD -> CardFragment()
            NowPlayingScreen.BLUR_CARD -> CardBlurFragment()
            NowPlayingScreen.FIT -> FitFragment()
            NowPlayingScreen.FLAT -> FlatPlayerFragment()
            NowPlayingScreen.FULL -> FullPlayerFragment()
            NowPlayingScreen.PLAIN -> PlainPlayerFragment()
            NowPlayingScreen.SIMPLE -> SimplePlayerFragment()
            NowPlayingScreen.MATERIAL -> MaterialFragment()
            NowPlayingScreen.COLOR -> ColorFragment()
            else -> PlayerFragment()
        } // must implement AbsPlayerFragment
        supportFragmentManager.beginTransaction().replace(R.id.playerFragmentContainer, fragment).commit()
        supportFragmentManager.executePendingTransactions()

        playerFragment = supportFragmentManager.findFragmentById(R.id.playerFragmentContainer) as AbsPlayerFragment
        miniPlayerFragment = supportFragmentManager.findFragmentById(R.id.miniPlayerFragment) as MiniPlayerFragment
        miniPlayerFragment!!.view!!.setOnClickListener { expandPanel() }

    }

    override fun onResume() {
        super.onResume()
        if (currentNowPlayingScreen != PreferenceUtil.getInstance().nowPlayingScreen) {
            postRecreate()
        }
    }

    private fun collapsePanel() {
        slidingLayout.panelState = PanelState.COLLAPSED
    }

    private fun expandPanel() {
        slidingLayout.panelState = PanelState.EXPANDED
    }


    override fun onPaletteColorChanged() {
        if (panelState == PanelState.EXPANDED) {
            val paletteColor = playerFragment!!.paletteColor
            super.setTaskDescriptionColor(paletteColor)

            val isColorLight = ColorUtil.isColorLight(paletteColor)
            if (PreferenceUtil.getInstance().adaptiveColor &&
                    (currentNowPlayingScreen == NowPlayingScreen.NORMAL || currentNowPlayingScreen == NowPlayingScreen.FLAT)) {
                super.setLightNavigationBar(true)
                super.setLightStatusbar(isColorLight)
            } else if (currentNowPlayingScreen == NowPlayingScreen.FULL || currentNowPlayingScreen == NowPlayingScreen.CARD ||
                    currentNowPlayingScreen == NowPlayingScreen.FIT ||
                    currentNowPlayingScreen == NowPlayingScreen.BLUR || currentNowPlayingScreen == NowPlayingScreen.BLUR_CARD) {
                super.setLightStatusbar(false)
                super.setLightNavigationBar(true)
            } else if (currentNowPlayingScreen == NowPlayingScreen.COLOR) {
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
        lightStatusbar = enabled
        if (panelState == PanelState.COLLAPSED) {
            super.setLightStatusbar(enabled)
        }
    }

    override fun setLightNavigationBar(enabled: Boolean) {
        lightNavigationBar = enabled
        if (panelState == PanelState.COLLAPSED) {
            super.setLightNavigationBar(enabled)
        }
    }

    override fun setNavigationbarColor(color: Int) {
        navigationbarColor = color
        if (panelState == PanelState.COLLAPSED) {
            if (navigationBarColorAnimator != null) navigationBarColorAnimator!!.cancel()
            super.setNavigationbarColor(color)
        }
    }

    override fun setTaskDescriptionColor(color: Int) {
        taskColor = color
        if (panelState == PanelState.COLLAPSED) {
            super.setTaskDescriptionColor(color)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (navigationBarColorAnimator != null) navigationBarColorAnimator!!.cancel() // just in case
    }

    companion object {

        val TAG: String = AbsSlidingMusicPanelActivity::class.java.simpleName
    }
}