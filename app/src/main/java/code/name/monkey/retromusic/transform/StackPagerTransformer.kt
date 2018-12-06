package code.name.monkey.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class StackPagerTransformer : ViewPager.PageTransformer {


    override fun transformPage(view: View, position: Float) {
        if (position <= 0f) {
            view.translationX = 0f
            view.scaleX = 1f
            view.scaleY = 1f

        } else if (position <= 1f) {

            val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
            view.pivotY = 0.5f * view.height
            view.translationX = view.width * -position
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }
    }

    companion object {
        private val MIN_SCALE = 0.75f
    }
}
