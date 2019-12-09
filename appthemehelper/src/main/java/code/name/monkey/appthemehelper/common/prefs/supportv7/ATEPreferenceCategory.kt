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
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import code.name.monkey.appthemehelper.ThemeStore

class ATEPreferenceCategory @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int = -1,
        defStyleRes: Int = -1
) : PreferenceCategory(context, attrs, defStyleAttr, defStyleRes) {
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val mTitle = holder.itemView.findViewById<TextView>(android.R.id.title)
        mTitle.setTextColor(ThemeStore.accentColor(holder.itemView.context))
        /*mTitle.textSize = dip2px(context, 4f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTitle.setTextAppearance(R.style.TextAppearance_MaterialComponents_Overline)
        }*/
    }

    fun dip2px(context: Context, dpVale: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpVale * scale + 0.5f)
    }
}
