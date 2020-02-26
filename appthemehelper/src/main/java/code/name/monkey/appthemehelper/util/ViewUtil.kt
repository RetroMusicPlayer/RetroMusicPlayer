package code.name.monkey.appthemehelper.util

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver

object ViewUtil {
    fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    fun setBackgroundCompat(view: View, drawable: Drawable?) {
        view.background = drawable
    }
}
