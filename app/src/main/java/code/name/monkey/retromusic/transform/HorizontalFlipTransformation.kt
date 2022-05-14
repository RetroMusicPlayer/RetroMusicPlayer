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