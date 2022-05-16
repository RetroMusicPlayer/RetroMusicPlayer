package code.name.monkey.retromusic.extensions

import android.app.ActivityManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.*
import androidx.fragment.app.FragmentActivity
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil

fun AppCompatActivity.maybeSetScreenOn() {
    if (PreferenceUtil.isScreenOnEnabled) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

fun AppCompatActivity.keepScreenOn(keepScreenOn: Boolean) {
    if (keepScreenOn) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

fun AppCompatActivity.setEdgeToEdgeOrImmersive() {
    if (PreferenceUtil.isFullScreenMode) {
        setImmersiveFullscreen()
    } else {
        setDrawBehindSystemBars()
    }
}

fun AppCompatActivity.setImmersiveFullscreen() {
    if (PreferenceUtil.isFullScreenMode) {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
        if (VersionUtils.hasP()) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            if (insets.displayCutout != null) {
                insets
            } else {
                // Consume insets if display doesn't have a Cutout
                WindowInsetsCompat.CONSUMED
            }
        }
    }
}

fun AppCompatActivity.exitFullscreen() {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.systemBars())
    }
}

fun AppCompatActivity.hideStatusBar() {
    hideStatusBar(PreferenceUtil.isFullScreenMode)
}

private fun AppCompatActivity.hideStatusBar(fullscreen: Boolean) {
    val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
    if (statusBar != null) {
        statusBar.isGone = fullscreen
    }
}

fun AppCompatActivity.setDrawBehindSystemBars() {
    if (VersionUtils.hasOreo()) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        if (VersionUtils.hasQ()) {
            window.isNavigationBarContrastEnforced = false
        }
    } else {
        setNavigationBarColorPreOreo(surfaceColor())
        if (VersionUtils.hasMarshmallow()) {
            setStatusBarColor(Color.TRANSPARENT)
        } else {
            setStatusBarColor(Color.BLACK)
        }
    }
}

fun FragmentActivity.setTaskDescriptionColor(color: Int) {
    var colorFinal = color
    // Task description requires fully opaque color
    colorFinal = ColorUtil.stripAlpha(colorFinal)
    // Sets color of entry in the system recents page
    if (VersionUtils.hasP()) {
        setTaskDescription(
            ActivityManager.TaskDescription(
                title as String?,
                -1,
                colorFinal
            )
        )
    } else {
        setTaskDescription(ActivityManager.TaskDescription(title as String?))
    }
}

fun AppCompatActivity.setTaskDescriptionColorAuto() {
    setTaskDescriptionColor(surfaceColor())
}

@Suppress("Deprecation")
fun AppCompatActivity.setLightStatusBar(enabled: Boolean) {
    if (VersionUtils.hasMarshmallow()) {
        val decorView = window.decorView
        val systemUiVisibility = decorView.systemUiVisibility
        if (enabled) {
            decorView.systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility =
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

fun AppCompatActivity.setLightStatusBarAuto() {
    setLightStatusBar(surfaceColor().isColorLight)
}

fun AppCompatActivity.setLightStatusBarAuto(bgColor: Int) {
    setLightStatusBar(bgColor.isColorLight)
}

@Suppress("Deprecation")
fun AppCompatActivity.setLightNavigationBar(enabled: Boolean) {
    if (VersionUtils.hasOreo()) {
        val decorView = window.decorView
        var systemUiVisibility = decorView.systemUiVisibility
        systemUiVisibility = if (enabled) {
            systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
        decorView.systemUiVisibility = systemUiVisibility
    }
}

fun AppCompatActivity.setLightNavigationBarAuto() {
    setLightNavigationBar(surfaceColor().isColorLight)
}

fun AppCompatActivity.setLightNavigationBarAuto(bgColor: Int) {
    setLightNavigationBar(bgColor.isColorLight)
}


/**
 * This will set the color of the view with the id "status_bar" on KitKat and Lollipop. On
 * Lollipop if no such view is found it will set the statusbar color using the native method.
 *
 * @param color the new statusbar color (will be shifted down on Lollipop and above)
 */
fun AppCompatActivity.setStatusBarColor(color: Int) {
    val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
    if (statusBar != null) {
        when {
            VersionUtils.hasMarshmallow() -> statusBar.setBackgroundColor(color)
            else -> statusBar.setBackgroundColor(
                ColorUtil.darkenColor(
                    color
                )
            )
        }
    } else {
        when {
            VersionUtils.hasMarshmallow() -> window.statusBarColor = color
            else -> window.statusBarColor = ColorUtil.darkenColor(color)
        }
    }
    setLightStatusBarAuto(surfaceColor())
}

fun AppCompatActivity.setStatusBarColorAuto() {
    // we don't want to use statusbar color because we are doing the color darkening on our own to support KitKat
    setStatusBarColor(surfaceColor())
    setLightStatusBarAuto(surfaceColor())
}

fun AppCompatActivity.setNavigationBarColor(color: Int) {
    if (VersionUtils.hasOreo()) {
        window.navigationBarColor = color
    } else {
        window.navigationBarColor = ColorUtil.darkenColor(color)
    }
    setLightNavigationBarAuto(color)
}

fun AppCompatActivity.setNavigationBarColorPreOreo(color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        window.navigationBarColor = ColorUtil.darkenColor(color)
    }
}

fun AppCompatActivity.setStatusBarColorPreMarshmallow(color: Int) {
    val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
    if (statusBar != null) {
        statusBar.setBackgroundColor(
            ColorUtil.darkenColor(
                color
            )
        )
    } else {
        window.statusBarColor = ColorUtil.darkenColor(color)
    }
}

fun AppCompatActivity.hideSoftKeyboard() {
    val currentFocus: View? = currentFocus
    if (currentFocus != null) {
        val inputMethodManager =
            getSystemService<InputMethodManager>()
        inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}