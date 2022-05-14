/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.appshortcuts

import android.content.Context
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.getTintedDrawable
import code.name.monkey.retromusic.util.PreferenceUtil

@RequiresApi(Build.VERSION_CODES.N_MR1)
object AppShortcutIconGenerator {
    fun generateThemedIcon(context: Context, iconId: Int): Icon {
        return if (PreferenceUtil.isColoredAppShortcuts) {
            generateUserThemedIcon(context, iconId)
        } else {
            generateDefaultThemedIcon(context, iconId)
        }
    }

    private fun generateDefaultThemedIcon(context: Context, iconId: Int): Icon {
        // Return an Icon of iconId with default colors
        return generateThemedIcon(
            context,
            iconId,
            context.getColor(R.color.app_shortcut_default_foreground),
            context.getColor(R.color.app_shortcut_default_background)
        )
    }

    private fun generateUserThemedIcon(context: Context, iconId: Int): Icon {
        // Get background color from context's theme
        val typedColorBackground = TypedValue()
        context.theme.resolveAttribute(android.R.attr.colorBackground, typedColorBackground, true)

        // Return an Icon of iconId with those colors
        return generateThemedIcon(
            context, iconId, ThemeStore.accentColor(context), typedColorBackground.data
        )
    }

    private fun generateThemedIcon(
        context: Context,
        iconId: Int,
        foregroundColor: Int,
        backgroundColor: Int,
    ): Icon {
        // Get and tint foreground and background drawables
        val vectorDrawable = context.getTintedDrawable(iconId, foregroundColor)
        val backgroundDrawable =
            context.getTintedDrawable(R.drawable.ic_app_shortcut_background, backgroundColor)

        // Squash the two drawables together
        val layerDrawable = LayerDrawable(arrayOf(backgroundDrawable, vectorDrawable))

        // Return as an Icon
        return Icon.createWithBitmap(layerDrawable.toBitmap())
    }
}
