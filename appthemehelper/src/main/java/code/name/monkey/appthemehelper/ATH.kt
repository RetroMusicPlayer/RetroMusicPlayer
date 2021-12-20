package code.name.monkey.appthemehelper

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsControllerCompat
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
object ATH {

    fun didThemeValuesChange(context: Context, since: Long): Boolean {
        return ThemeStore.isConfigured(context) && ThemeStore.prefs(context).getLong(
            ThemeStorePrefKeys.VALUES_CHANGED,
            -1
        ) > since
    }

    fun setLightStatusBar(activity: Activity, enabled: Boolean) {
        activity.window.apply {
            WindowInsetsControllerCompat(
                this,
                decorView
            ).isAppearanceLightStatusBars = enabled
        }
    }

    fun setLightNavigationBar(activity: Activity, enabled: Boolean) {
        activity.window.apply {
            WindowInsetsControllerCompat(
                this,
                decorView
            ).isAppearanceLightNavigationBars = enabled
        }
    }

    fun setLightNavigationBarAuto(activity: Activity, bgColor: Int) {
        setLightNavigationBar(activity, ColorUtil.isColorLight(bgColor))
    }

    fun setNavigationBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.window.navigationBarColor = color
        } else {
            activity.window.navigationBarColor = ColorUtil.darkenColor(color)
        }
        setLightNavigationBarAuto(activity, color)
    }

    fun setActivityToolbarColorAuto(activity: Activity, toolbar: Toolbar?) {
        setActivityToolbarColor(activity, toolbar, ThemeStore.primaryColor(activity))
    }

    private fun setActivityToolbarColor(
        activity: Activity, toolbar: Toolbar?,
        color: Int
    ) {
        if (toolbar == null) {
            return
        }
        toolbar.setBackgroundColor(color)
        ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(activity, toolbar, color)
    }

    fun setTaskDescriptionColorAuto(activity: Activity) {
        setTaskDescriptionColor(activity, ThemeStore.primaryColor(activity))
    }

    fun setTaskDescriptionColor(activity: Activity, @ColorInt color: Int) {
        var colorFinal = color
        // Task description requires fully opaque color
        colorFinal = ColorUtil.stripAlpha(colorFinal)
        // Sets color of entry in the system recents page
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.setTaskDescription(
                ActivityManager.TaskDescription(
                    activity.title as String?,
                    -1,
                    colorFinal
                )
            )
        } else {
            activity.setTaskDescription(ActivityManager.TaskDescription(activity.title as String?))
        }
    }

    fun setTint(view: View, @ColorInt color: Int) {
        TintHelper.setTintAuto(view, color, false)
    }

    fun setBackgroundTint(view: View, @ColorInt color: Int) {
        TintHelper.setTintAuto(view, color, true)
    }
}