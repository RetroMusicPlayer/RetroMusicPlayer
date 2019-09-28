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
import android.util.AttributeSet
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.NavigationViewUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBarTinted @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        labelVisibilityMode = PreferenceUtil.getInstance(context).tabTitleMode
        selectedItemId = PreferenceUtil.getInstance(context).lastPage

        val iconColor = ATHUtil.resolveColor(context, R.attr.iconColor)
        val accentColor = ThemeStore.accentColor(context)
        NavigationViewUtil.setItemIconColors(this, ColorUtil.withAlpha(iconColor, 0.5f), accentColor)
        NavigationViewUtil.setItemTextColors(this, ColorUtil.withAlpha(iconColor, 0.5f), accentColor)

        setOnApplyWindowInsetsListener(null)
    }
}