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
import kotlin.math.abs

class HingeTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {

        page.translationX = -position * page.width
        page.pivotX = 0f
        page.pivotY = 0f


        when {
            position < -1 -> {    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
                // The Page is off-screen but it may still interfere with
                // click events of current page if
                // it's visibility is not set to Gone
                page.visibility = View.GONE
            }
            position <= 0 -> {    // [-1,0]
                page.rotation = 90 * abs(position)
                page.alpha = 1 - abs(position)
                page.visibility = View.VISIBLE
            }
            position <= 1 -> {    // (0,1]
                page.rotation = 0f
                page.alpha = 1f
                page.visibility = View.VISIBLE
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
                page.visibility = View.GONE
            }
        }
    }
}