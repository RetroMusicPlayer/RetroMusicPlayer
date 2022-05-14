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