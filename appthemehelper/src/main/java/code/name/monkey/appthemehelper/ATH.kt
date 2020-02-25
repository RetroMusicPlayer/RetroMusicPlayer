package code.name.monkey.appthemehelper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
object ATH {

    @SuppressLint("CommitPrefEdits")
    fun didThemeValuesChange(context: Context, since: Long): Boolean {
        return ThemeStore.isConfigured(context) && ThemeStore.prefs(context).getLong(
            ThemeStorePrefKeys.VALUES_CHANGED,
            -1
        ) > since
    }

    fun setLightStatusbar(activity: Activity, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
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

    fun setLightNavigationbar(activity: Activity, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val decorView = activity.window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility = if (enabled) {
                systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            decorView.systemUiVisibility = systemUiVisibility
        }
    }

    fun setLightNavigationbarAuto(activity: Activity, bgColor: Int) {
        setLightNavigationbar(activity, ColorUtil.isColorLight(bgColor))
    }

    fun setNavigationbarColorAuto(activity: Activity) {
        setNavigationbarColor(activity, ThemeStore.navigationBarColor(activity))
    }

    fun setNavigationbarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.window.navigationBarColor = color
        } else {
            activity.window.navigationBarColor = ColorUtil.darkenColor(color)
        }
        setLightNavigationbarAuto(activity, color)
    }

    fun setActivityToolbarColorAuto(activity: Activity, toolbar: Toolbar?) {
        setActivityToolbarColor(activity, toolbar, ThemeStore.primaryColor(activity))
    }

    fun setActivityToolbarColor(
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
    }

    fun setTint(view: View, @ColorInt color: Int) {
        TintHelper.setTintAuto(view, color, false)
    }

    fun setBackgroundTint(view: View, @ColorInt color: Int) {
        TintHelper.setTintAuto(view, color, true)
    }
}