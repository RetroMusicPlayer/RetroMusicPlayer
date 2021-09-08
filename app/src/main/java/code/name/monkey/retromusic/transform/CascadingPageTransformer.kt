/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class CascadingPageTransformer : ViewPager.PageTransformer {

    private var mScaleOffset = 40

    override fun transformPage(page: View, position: Float) {
        if (position <= 0.0f) {//被滑动的那页  position 是-下标~ 0
            page.translationX = 0f
            //旋转角度  45° * -0.1 = -4.5°
            page.rotation = 45 * position
            //X轴偏移 li:  300/3 * -0.1 = -10
            page.translationX = page.width / 3 * position
        } else {
            //缩放比例
            val scale = (page.width - mScaleOffset * position) / page.width.toFloat()

            page.scaleX = scale
            page.scaleY = scale

            page.translationX = -page.width * position
            page.translationY = mScaleOffset * 0.8f * position
        }
    }
}