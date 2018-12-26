package code.name.monkey.appthemehelper.util

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import androidx.annotation.ColorInt
import android.view.View
import android.view.ViewTreeObserver


object ViewUtil {

    fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        } else {
            v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    fun setBackgroundCompat(view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.background = drawable
        else
            view.setBackgroundDrawable(drawable)
    }

    fun setBackgroundTransition(view: View, newDrawable: Drawable): TransitionDrawable {
        val transition = DrawableUtil.createTransitionDrawable(view.background, newDrawable)
        setBackgroundCompat(view, transition)
        return transition
    }

    fun setBackgroundColorTransition(view: View, @ColorInt newColor: Int): TransitionDrawable {
        val oldColor = view.background

        val start = oldColor ?: ColorDrawable(view.solidColor)
        val end = ColorDrawable(newColor)

        val transition = DrawableUtil.createTransitionDrawable(start, end)

        setBackgroundCompat(view, transition)

        return transition
    }
}
