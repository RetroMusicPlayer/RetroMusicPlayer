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
