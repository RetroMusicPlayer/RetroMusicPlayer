package code.name.monkey.appthemehelper.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.core.content.res.use

/**
 * @author Aidan Follestad (afollestad)
 */
object ATHUtil {

    fun isWindowBackgroundDark(context: Context): Boolean {
        return !ColorUtil.isColorLight(resolveColor(context, android.R.attr.windowBackground))
    }

    @JvmOverloads
    fun resolveColor(context: Context, @AttrRes attr: Int, fallback: Int = 0): Int {
        context.theme.obtainStyledAttributes(intArrayOf(attr)).use {
            return it.getColor(0, fallback)
        }
    }
}