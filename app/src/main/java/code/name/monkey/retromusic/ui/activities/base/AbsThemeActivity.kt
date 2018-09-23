package code.name.monkey.retromusic.ui.activities.base

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager

import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.ATHActivity
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialDialogsUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil

abstract class AbsThemeActivity : ATHActivity(), Runnable {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(PreferenceUtil.getInstance(this).generalTheme)
        hideStatusBar()
        super.onCreate(savedInstanceState)
        MaterialDialogsUtil.updateMaterialDialogsThemeSingleton(this)

        changeBackgroundShape()
        setImmersiveFullscreen()
        registerSystemUiVisibility()
        toggleScreenOn()
    }

    private fun toggleScreenOn() {
        if (PreferenceUtil.getInstance(this).isScreenOnEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideStatusBar()
            handler.removeCallbacks(this)
            handler.postDelayed(this, 300)
        } else {
            handler.removeCallbacks(this)
        }
    }

    fun hideStatusBar() {
        hideStatusBar(PreferenceUtil.getInstance(this).fullScreenMode)
    }

    private fun hideStatusBar(fullscreen: Boolean) {
        val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
        if (statusBar != null) {
            statusBar.visibility = if (fullscreen) View.GONE else View.VISIBLE
        }
    }


    private fun changeBackgroundShape() {
        var background: Drawable? = if (PreferenceUtil.getInstance(this).isRoundCorners)
            ContextCompat.getDrawable(this, R.drawable.round_window)
        else
            ContextCompat.getDrawable(this, R.drawable.square_window)
        background = TintHelper.createTintedDrawable(background, ThemeStore.primaryColor(this))
        window.setBackgroundDrawable(background)
    }

    protected fun setDrawUnderStatusBar(drawUnderStatusbar: Boolean) {
        if (VersionUtils.hasLollipop()) {
            RetroUtil.setAllowDrawUnderStatusBar(window)
        } else if (VersionUtils.hasKitKat()) {
            RetroUtil.setStatusBarTranslucent(window)
        }
    }

    /**
     * This will set the color of the view with the id "status_bar" on KitKat and Lollipop. On
     * Lollipop if no such view is found it will set the statusbar color using the native method.
     *
     * @param color the new statusbar color (will be shifted down on Lollipop and above)
     */
    fun setStatusbarColor(color: Int) {
        if (VersionUtils.hasKitKat()) {
            val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
            if (statusBar != null) {
                if (VersionUtils.hasLollipop()) {
                    statusBar.setBackgroundColor(ColorUtil.darkenColor(color))
                    setLightStatusbarAuto(color)
                } else {
                    statusBar.setBackgroundColor(color)
                }
            } else if (Build.VERSION.SDK_INT >= 21) {
                window.statusBarColor = ColorUtil.darkenColor(color)
                setLightStatusbarAuto(color)
            }
        }
    }

    fun setStatusbarColorAuto() {
        // we don't want to use statusbar color because we are doing the color darkening on our own to support KitKat
        setStatusbarColor(ThemeStore.primaryColor(this))
    }

    open fun setTaskDescriptionColor(@ColorInt color: Int) {
        ATH.setTaskDescriptionColor(this, color)
    }

    fun setTaskDescriptionColorAuto() {
        setTaskDescriptionColor(ThemeStore.primaryColor(this))
    }

    open fun setNavigationbarColor(color: Int) {
        if (ThemeStore.coloredNavigationBar(this)) {
            ATH.setNavigationbarColor(this, color)
        } else {
            ATH.setNavigationbarColor(this, Color.BLACK)
        }
    }

    fun setNavigationbarColorAuto() {
        setNavigationbarColor(ThemeStore.navigationBarColor(this))
    }

    open fun setLightStatusbar(enabled: Boolean) {
        ATH.setLightStatusbar(this, enabled)
    }

    fun setLightStatusbarAuto(bgColor: Int) {
        setLightStatusbar(ColorUtil.isColorLight(bgColor))
    }

    open fun setLightNavigationBar(enabled: Boolean) {
        if (!ATHUtil.isWindowBackgroundDark(this) && ThemeStore.coloredNavigationBar(this)) {
            ATH.setLightNavigationbar(this, enabled)
        }
    }

    private fun registerSystemUiVisibility() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                setImmersiveFullscreen()
            }
        }
    }

    private fun unregisterSystemUiVisibility() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener(null)
    }

    fun setImmersiveFullscreen() {
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (PreferenceUtil.getInstance(this).fullScreenMode) {
            window.decorView.systemUiVisibility = flags
        }
    }

    fun exitFullscreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun run() {
        setImmersiveFullscreen()
    }

    override fun onStop() {
        handler.removeCallbacks(this)
        super.onStop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterSystemUiVisibility()
        exitFullscreen()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            handler.removeCallbacks(this)
            handler.postDelayed(this, 500)
        }
        return super.onKeyDown(keyCode, event)

    }
}