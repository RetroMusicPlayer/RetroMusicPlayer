package code.name.monkey.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class VerticalStackTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            if (position >= 0) {
                scaleX = (0.9f - 0.05f * position)
                scaleY = 0.9f
                translationX = -width * position
                translationY = -30 * position
            }
        }

    }
}
