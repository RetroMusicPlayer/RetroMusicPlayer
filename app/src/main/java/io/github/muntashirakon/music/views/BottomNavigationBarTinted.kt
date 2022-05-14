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

package io.github.muntashirakon.music.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.NavigationViewUtil
import io.github.muntashirakon.music.util.PreferenceUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.chrisbanes.insetter.applyInsetter

class BottomNavigationBarTinted @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        // If we are in Immersive mode we have to just set empty OnApplyWindowInsetsListener as
        // bottom, start, and end padding is always applied (with the help of OnApplyWindowInsetsListener) to
        // BottomNavigationView to dodge the system navigation bar (so we basically clear that listener).
        if (PreferenceUtil.isFullScreenMode) {
            setOnApplyWindowInsetsListener { _, insets ->
                insets
            }
        } else {
            applyInsetter {
                type(navigationBars = true) {
                    padding(vertical = true)
                    margin(horizontal = true)
                }
            }
        }

        labelVisibilityMode = PreferenceUtil.tabTitleMode

        if (!PreferenceUtil.materialYou) {
            val iconColor = ATHUtil.resolveColor(context, android.R.attr.colorControlNormal)
            val accentColor = ThemeStore.accentColor(context)
            NavigationViewUtil.setItemIconColors(
                this,
                ColorUtil.withAlpha(iconColor, 0.5f),
                accentColor
            )
            NavigationViewUtil.setItemTextColors(
                this,
                ColorUtil.withAlpha(iconColor, 0.5f),
                accentColor
            )
            itemRippleColor = ColorStateList.valueOf(accentColor.addAlpha(0.08F))
            itemActiveIndicatorColor = ColorStateList.valueOf(accentColor.addAlpha(0.12F))
        }
    }
}

fun Int.addAlpha(alpha: Float): Int {
    return ColorUtil.withAlpha(this, alpha)
}
