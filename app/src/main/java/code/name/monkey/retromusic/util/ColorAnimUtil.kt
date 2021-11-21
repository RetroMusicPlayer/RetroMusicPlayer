package code.name.monkey.retromusic.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator

class ColorAnimUtil {
    companion object {
        fun createColorAnimator(
            fromColor: Int,
            toColor: Int,
            mDuration: Long = 300
        ): ValueAnimator {
            return ValueAnimator.ofInt(fromColor, toColor).apply {
                setEvaluator(ArgbEvaluator())
                duration = mDuration
            }
        }
    }
}