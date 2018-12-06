package code.name.monkey.retromusic.transform

import android.annotation.SuppressLint
import android.view.View
import androidx.viewpager.widget.ViewPager

/*******************************************************************
 * * * * *   * * * *   *     *       Created by OCN.Yang
 * *     *   *         * *   *       Time:2017/12/7 19:32.
 * *     *   *         *   * *       Email address:ocnyang@gmail.com
 * * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 */

class CascadingPageTransformer : ViewPager.PageTransformer {
    /**
     * 偏移量
     */
    private var mScaleOffset = 40


    /**
     * @param mScaleOffset 缩放偏移量 单位 px
     */
    fun setScaleOffset(mScaleOffset: Int) {
        this.mScaleOffset = mScaleOffset
    }

    @SuppressLint("NewApi")
    override fun transformPage(page: View, position: Float) {
        if (position <= 0.0f) {//被滑动的那页  position 是-下标~ 0
            page.translationX = 0f
            //旋转角度  45° * -0.1 = -4.5°
            page.rotation = 45 * position
            //X轴偏移 li:  300/3 * -0.1 = -10
            page.translationX = page.width / 3 * position
        } else if (position <= 1f) {
            val scale = (page.width - mScaleOffset * position) / page.width.toFloat()

            page.scaleX = scale
            page.scaleY = scale

            page.translationX = -page.width * position
            page.translationY = mScaleOffset * 0.8f * position
        } else {
            //缩放比例
            val scale = (page.width - mScaleOffset * position) / page.width.toFloat()

            page.scaleX = scale
            page.scaleY = scale

            page.translationX = -page.width * position
            page.translationY = mScaleOffset * 0.7f * position
        }
    }
} 