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
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import code.name.monkey.retromusic.R
import kotlinx.android.synthetic.main.list_setting_item_view.view.*

/**
 * Created by hemanths on 2019-12-10.
 */
class SettingListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        View.inflate(context, R.layout.list_setting_item_view, this)
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.SettingListItemView)
        icon as ColorIconsImageView
        if (typedArray.hasValue(R.styleable.SettingListItemView_settingListItemIcon)) {
            icon.setImageDrawable(typedArray.getDrawable(R.styleable.SettingListItemView_settingListItemIcon))
        }
        icon.setIconBackgroundColor(
            typedArray.getColor(
                R.styleable.SettingListItemView_settingListItemIconColor,
                Color.WHITE
            )
        )
        title.text = typedArray.getText(R.styleable.SettingListItemView_settingListItemTitle)
        text.text = typedArray.getText(R.styleable.SettingListItemView_settingListItemText)
        typedArray.recycle()
    }
}