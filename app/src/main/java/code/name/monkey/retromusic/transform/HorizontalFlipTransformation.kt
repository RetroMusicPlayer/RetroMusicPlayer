package code.name.monkey.retromusic.transform

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class HorizontalFlipTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            page.translationX = -position * page.width
            page.cameraDistance = 20000f

            if (position < 0.5 && position > -0.5) {
                page.isVisible = true
            } else {
                page.isInvisible = true
            }

            when {
                position < -1 -> {     // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 0 -> {    // [-1,0]
                    page.alpha = 1f
                    page.rotationX = 180 * (1 - abs(position) + 1)
                }
                position <= 1 -> {    // (0,1]
                    page.alpha = 1f
                    page.rotationX = -180 * (1 - abs(position) + 1)
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }
    }
}