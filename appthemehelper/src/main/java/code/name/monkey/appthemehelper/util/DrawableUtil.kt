package code.name.monkey.appthemehelper.util

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import androidx.annotation.ColorInt


object DrawableUtil {

    fun createTransitionDrawable(@ColorInt startColor: Int, @ColorInt endColor: Int): TransitionDrawable {
        return createTransitionDrawable(ColorDrawable(startColor), ColorDrawable(endColor))
    }

    fun createTransitionDrawable(start: Drawable, end: Drawable): TransitionDrawable {
        val drawables = arrayOfNulls<Drawable>(2)

        drawables[0] = start
        drawables[1] = end

        return TransitionDrawable(drawables)
    }
}
