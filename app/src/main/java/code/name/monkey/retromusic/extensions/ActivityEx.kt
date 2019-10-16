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

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R

fun AppCompatActivity.applyToolbar(toolbar: Toolbar) {
    toolbar.apply {
        setNavigationOnClickListener { onBackPressed() }
        setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        ToolbarContentTintHelper.colorBackButton(toolbar )
        setBackgroundColor(ATHUtil.resolveColor(this@applyToolbar, R.attr.colorPrimary))
    }
    setSupportActionBar(toolbar)
}