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
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import com.google.android.material.color.MaterialColors


class ColorIconsImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : AppCompatImageView(context, attrs, defStyleAttr) {


    init {
        // Load the styled attributes and set their properties
        context.withStyledAttributes(attrs, R.styleable.ColorIconsImageView, 0, 0) {
            val color = getColor(R.styleable.ColorIconsImageView_iconBackgroundColor, Color.RED)
            setIconBackgroundColor(color)
        }
    }

    fun setIconBackgroundColor(color: Int) {
        background = ContextCompat.getDrawable(context, R.drawable.color_circle_gradient)
        if (ATHUtil.isWindowBackgroundDark(context) && PreferenceUtil.isDesaturatedColor) {
            val desaturatedColor = RetroColorUtil.desaturateColor(color, 0.4f)
            backgroundTintList = ColorStateList.valueOf(desaturatedColor)
            imageTintList =
                ColorStateList.valueOf(ATHUtil.resolveColor(context, com.google.android.material.R.attr.colorSurface))
        } else {
            val finalColor = MaterialColors.harmonize(
                color,
                ThemeStore.accentColor(context)
            )
            backgroundTintList = ColorStateList.valueOf(ColorUtil.adjustAlpha(finalColor, 0.22f))
            imageTintList = ColorStateList.valueOf(ColorUtil.withAlpha(finalColor, 0.75f))
        }
        requestLayout()
        invalidate()
    }
}
