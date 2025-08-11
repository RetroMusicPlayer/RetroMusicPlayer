package code.name.monkey.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class CascadingPageTransformer : ViewPager.PageTransformer {

    private var mScaleOffset = 40

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> { // [-Infinity,-1)
                    alpha = 0f
                }
                position <= 0 -> {
                    alpha = 1f
                    rotation = 45 * position
                    translationX = width / 3 * position
                }
                else -> {
                    alpha = 1f
                    rotation = 0f
                    val scale = (width - mScaleOffset * position) / width.toFloat()

                    scaleX = scale
                    scaleY = scale

                    translationX = -width * position
                    translationY = mScaleOffset * 0.8f * position
                }
            }
        }
    }
}