package code.name.monkey.retromusic.transform

import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class HingeTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            translationX = -position * width
            pivotX = 0f
            pivotY = 0f

            when {
                position < -1 -> {    // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                    // The Page is off-screen but it may still interfere with
                    // click events of current page if
                    // it's visibility is not set to Gone
                    isVisible = false
                }
                position <= 0 -> {    // [-1,0]
                    rotation = 90 * abs(position)
                    alpha = 1 - abs(position)
                    isVisible = true
                }
                position <= 1 -> {    // (0,1]
                    rotation = 0f
                    alpha = 1f
                    isVisible = true
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                    isVisible = false
                }
            }
        }
    }
}