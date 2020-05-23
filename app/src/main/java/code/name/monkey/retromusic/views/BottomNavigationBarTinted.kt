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

package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.NavigationViewUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtilKT
import code.name.monkey.retromusic.util.RippleUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBarTinted @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        labelVisibilityMode = PreferenceUtilKT.tabTitleMode
        selectedItemId = PreferenceUtilKT.lastPage

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
        itemBackground = RippleDrawable(
            RippleUtils.convertToRippleDrawableColor(
                ColorStateList.valueOf(
                    ThemeStore.accentColor(context).addAlpha()
                )
            ),
            ContextCompat.getDrawable(context, R.drawable.bottom_navigation_item_background),
            ContextCompat.getDrawable(context, R.drawable.bottom_navigation_item_background_mask)
        )
        setOnApplyWindowInsetsListener(null)
        //itemRippleColor = ColorStateList.valueOf(accentColor)
        background = ColorDrawable(ATHUtil.resolveColor(context, R.attr.colorSurface))
    }
}

fun Int.addAlpha(): Int {
    return ColorUtil.withAlpha(this, 0.12f)
}
