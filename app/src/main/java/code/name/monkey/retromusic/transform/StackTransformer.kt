package code.name.monkey.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class StackTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (position >= 0) {
            page.scaleX = (0.9f - 0.05f * position)
            page.scaleY = 0.9f
            page.translationX = -page.width * position
            page.translationY = -30 * position
        }
    }
}
