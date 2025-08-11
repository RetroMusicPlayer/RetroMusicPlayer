package code.name.monkey.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Created by xgc1986 on 2/Apr/2016
 */

class ParallaxPagerTransformer(private val id: Int) : ViewPager.PageTransformer {
    private var speed = 0.2f

    override fun transformPage(page: View, position: Float) {
        val parallaxView = page.findViewById<View>(id)
        page.apply {
            if (parallaxView != null) {
                if (position > -1 && position < 1) {
                    val width = parallaxView.width.toFloat()
                    parallaxView.translationX = -(position * width * speed)
                    scaleX = 1f
                    scaleY = 1f
                }
            }
        }
    }

    fun setSpeed(speed: Float) {
        this.speed = speed
    }
}
