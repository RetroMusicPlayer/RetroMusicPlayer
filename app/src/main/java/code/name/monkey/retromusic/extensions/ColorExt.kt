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

package code.name.monkey.retromusic.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R

fun Int.ripAlpha(): Int {
    return ColorUtil.stripAlpha(this)
}

fun Any.surfaceColor(context: Context): Int {
    return ATHUtil.resolveColor(context, R.attr.colorSurface, Color.WHITE)
}

fun Toolbar.backgroundTintList() {
    val surfaceColor = ATHUtil.resolveColor(context, R.attr.colorSurface, Color.BLACK)
    val colorStateList = ColorStateList.valueOf(surfaceColor)
    backgroundTintList = colorStateList
}

fun textColorSecondary(context: Context): Int {
    return ATHUtil.resolveColor(context, android.R.attr.textColorSecondary)
}

fun colorControlNormal(context: Context): Int {
    return ATHUtil.resolveColor(context, android.R.attr.colorControlNormal)
}

fun textColorPrimary(context: Context): Int {
    return ATHUtil.resolveColor(context, android.R.attr.textColorPrimary)
}