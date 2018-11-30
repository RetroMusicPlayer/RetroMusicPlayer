package code.name.monkey.retromusic.ui.activities.base

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.ui.fragments.MiniPlayerFragment
import code.name.monkey.retromusic.ui.fragments.NowPlayingScreen
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment
import code.name.monkey.retromusic.ui.fragments.player.adaptive.AdaptiveFragment
import code.name.monkey.retromusic.ui.fragments.player.blur.BlurPlayerFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.views.BottomNavigationBarTinted
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState

abstract class AbsSlidingMusicPanelActivity protected constructor() : AbsMusicServiceActivity(), SlidingUpPanelLayout.PanelSlideListener, AbsPlayerFragment.Callbacks {


    lateinit var slidingUpPanelLayout: SlidingUpPanelLayout
    private lateinit var bottomNavigationView: BottomNavigationBarTinted

    private var miniPlayerFragment: MiniPlayerFragment? = null
    var playerFragment: AbsPlayerFragment? = null
    private var currentNowPlayingScreen: NowPlayingScreen? = null

    private var navigationbarColor: Int = 0
    private var taskColor: Int = 0
    private var lightStatusbar: Boolean = false
    private var lightNavigationBar: Boolean = false
    private var navigationBarColorAnimator: ValueAnimator? = null
    private val argbEvaluator = ArgbEvaluator()

    val panelState: SlidingUpPanelLayout.PanelState?
        get() = slidingUpPanelLayout.panelState

    private val isOneOfTheseThemes: Boolean
        get() = (currentNowPlayingScreen == NowPlayingScreen.ADAPTIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView())
        ButterKnife.bind(this)

        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        choosFragmentForTheme()
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
            slidingUpPanelLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    slidingUpPanelLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
            slidingUpPanelLayout.panelHeight = 0
            collapsePanel()
        } else {
            if (!MusicPlayerRemote.playingQueue.isEmpty()) {
                slidingUpPanelLayout.panelHeight = if (bottomNavigationView.visibility == View.VISIBLE) heightOfBarWithTabs else heightOfBar
            }
        }
    }

    protected fun wrapSlidingMusicPanel(@LayoutRes resId: Int): View {
        @SuppressLint("InflateParams")
        val slidingMusicPanelLayout = layoutInflater.inflate(R.layout.sliding_music_panel_layout, null)
        val contentContainer = slidingMusicPanelLayout.findViewById<ViewGroup>(R.id.content_container)
        layoutInflater.inflate(resId, contentContainer)
        return slidingMusicPanelLayout
    }

    override fun onBackPressed() {
        if (!handleBackPress())
            super.onBackPressed()
    }

    open fun handleBackPress(): Boolean {
        if (slidingUpPanelLayout.panelHeight != 0 && playerFragment!!.onBackPressed())
            return true
        if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            collapsePanel()
            return true
        }
        return false
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        castSession ?: return
        //MusicPlayerRemote.setZeroVolume();
        //CastHelper.startCasting(castSession, MusicPlayerRemote.getCurrentSong());
    }

    fun toggleBottomNavigationView(toggle: Boolean) {
        bottomNavigationView.visibility = if (toggle) View.GONE else View.VISIBLE
    }

    fun getBottomNavigationView(): BottomNavigationView? {
        return bottomNavigationView
    }

    private fun setupSlidingUpPanel() {
        slidingUpPanelLayout.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        slidingUpPanelLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        if (panelState == PanelState.EXPANDED) {
                            onPanelSlide(slidingUpPanelLayout, 1f)
                            onPanelExpanded()
                        } else if (panelState == PanelState.COLLAPSED) {
                            onPanelCollapsed()
                        } else {
                            playerFragment!!.onHide()
                        }
                    }
                })

        slidingUpPanelLayout.addPanelSlideListener(this)

    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        bottomNavigationView.translationY = slideOffset * 400
        setMiniPlayerAlphaProgress(slideOffset)
        //if (navigationBarColorAnimator != null) navigationBarColorAnimator.cancel();
        //super.setNavigationbarColor((int) argbEvaluator.evaluate(slideOffset, navigationbarColor, Color.TRANSPARENT));
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

    fun onPanelCollapsed() {
        // restore values
        super.setLightStatusbar(lightStatusbar)
        super.setTaskDescriptionColor(taskColor)
        super.setNavigationbarColor(navigationbarColor)
        super.setLightNavigationBar(lightNavigationBar)


        playerFragment!!.setMenuVisibility(false)
        playerFragment!!.userVisibleHint = false
        playerFragment!!.onHide()
    }

    fun onPanelExpanded() {
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

    private fun choosFragmentForTheme() {
        currentNowPlayingScreen = PreferenceUtil.getInstance().nowPlayingScreen

        val fragment: Fragment // must implement AbsPlayerFragment
        when (currentNowPlayingScreen) {
            NowPlayingScreen.BLUR -> fragment = BlurPlayerFragment()
            NowPlayingScreen.ADAPTIVE -> fragment = AdaptiveFragment()
            else -> fragment = AdaptiveFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.player_fragment_container, fragment).commit()
        supportFragmentManager.executePendingTransactions()

        playerFragment = supportFragmentManager.findFragmentById(R.id.player_fragment_container) as AbsPlayerFragment?
        miniPlayerFragment = supportFragmentManager.findFragmentById(R.id.mini_player_fragment) as MiniPlayerFragment?


        miniPlayerFragment!!.view!!.setOnClickListener { expandPanel() }

    }

    override fun onResume() {
        super.onResume()
        if (currentNowPlayingScreen != PreferenceUtil.getInstance().nowPlayingScreen) {
            postRecreate()
        }
    }

    private fun collapsePanel() {
        slidingUpPanelLayout.panelState = PanelState.COLLAPSED
    }

    private fun expandPanel() {
        slidingUpPanelLayout.panelState = PanelState.EXPANDED
    }


    override fun onPaletteColorChanged() {
        if (panelState == PanelState.EXPANDED) {
            val paletteColor = playerFragment!!.paletteColor
            ColorUtil.isColorLight(paletteColor)
            super.setTaskDescriptionColor(paletteColor)
            if (currentNowPlayingScreen == NowPlayingScreen.BLUR) {
                super.setLightStatusbar(false)
                super.setLightNavigationBar(true)
            } else {
                val isTheme = isOneOfTheseThemes && ColorUtil.isColorLight(ThemeStore.primaryColor(this))
                super.setStatusbarColor(Color.TRANSPARENT)
                super.setLightStatusbar(isTheme)
                super.setLightNavigationBar(isTheme)
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

        val TAG = AbsSlidingMusicPanelActivity::class.java.simpleName
    }
}