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