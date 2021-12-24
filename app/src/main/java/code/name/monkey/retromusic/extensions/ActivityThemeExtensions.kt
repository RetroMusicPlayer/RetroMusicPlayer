package code.name.monkey.retromusic.extensions

import android.app.ActivityManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import androidx.fragment.app.FragmentActivity
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil

fun AppCompatActivity.toggleScreenOn() {
    if (PreferenceUtil.isScreenOnEnabled) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

fun AppCompatActivity.setImmersiveFullscreen() {
    if (PreferenceUtil.isFullScreenMode) {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
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
    WindowCompat.setDecorFitsSystemWindows(window, false)
    if (VersionUtils.hasOreo()) {
        if (VersionUtils.hasQ()) {
            window.isNavigationBarContrastEnforced = false
        }
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    } else {
        setNavigationBarColorPreOreo(surfaceColor())
        if (VersionUtils.hasMarshmallow()) {
            setStatusBarColor(Color.TRANSPARENT)
        } else {
            setStatusBarColor(surfaceColor())
        }
    }
}

fun FragmentActivity.setTaskDescriptionColor(color: Int) {
    var colorFinal = color
    // Task description requires fully opaque color
    colorFinal = ColorUtil.stripAlpha(colorFinal)
    // Sets color of entry in the system recents page
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
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

fun AppCompatActivity.setLightNavigationAuto() {
    ATH.setLightNavigationBarAuto(this, surfaceColor())
}

fun AppCompatActivity.setLightStatusBar(enabled: Boolean) {
    ATH.setLightStatusBar(this, enabled)
}

fun AppCompatActivity.setLightStatusBarAuto(bgColor: Int) {
    setLightStatusBar(ColorUtil.isColorLight(bgColor))
}

fun AppCompatActivity.setLightNavigationBar(enabled: Boolean) {
    ATH.setLightNavigationBar(this, enabled)
}

fun AppCompatActivity.setLightNavigationBarAuto(bgColor: Int) {
    setLightNavigationBar(ColorUtil.isColorLight(bgColor))
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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