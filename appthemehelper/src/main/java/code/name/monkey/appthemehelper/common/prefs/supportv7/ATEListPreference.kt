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

package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.preference.ListPreference
import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore


class ATEListPreference @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : ListPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        icon?.setColorFilter(ThemeStore.accentColor(context), PorterDuff.Mode.SRC_IN)
        layoutResource = R.layout.ate_preference_custom_support
        if (summary == null || summary.toString().trim { it <= ' ' }.isEmpty())
            summary = "%s"
    }
}
