package code.name.monkey.appthemehelper.util

import android.content.Context
import androidx.annotation.AttrRes

/**
 * @author Aidan Follestad (afollestad)
 */
object ATHUtil {

    fun isWindowBackgroundDark(context: Context): Boolean {
        return !ColorUtil.isColorLight(resolveColor(context, android.R.attr.windowBackground))
    }

    @JvmOverloads
    fun resolveColor(context: Context?, @AttrRes attr: Int, fallback: Int = 0): Int {
        val a = context!!.theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColor(0, fallback)
        } finally {
            a.recycle()
        }
    }

    fun inClassPath(clsName: String): Class<*> {
        try {
            return Class.forName(clsName)
        } catch (t: Throwable) {
            throw IllegalStateException(String.format("%s is not in your class path! You must include the associated library.", clsName))
        }

    }
}