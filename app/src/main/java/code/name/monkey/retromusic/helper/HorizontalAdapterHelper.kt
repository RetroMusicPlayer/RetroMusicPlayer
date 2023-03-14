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
package code.name.monkey.retromusic.helper

import android.content.Context
import android.view.ViewGroup
import code.name.monkey.retromusic.R

object HorizontalAdapterHelper {

    const val LAYOUT_RES = R.layout.item_image

    private const val TYPE_FIRST = 1
    private const val TYPE_MIDDLE = 2
    private const val TYPE_LAST = 3

    fun applyMarginToLayoutParams(
        context: Context,
        layoutParams: ViewGroup.MarginLayoutParams,
        viewType: Int
    ) {
        val listMargin = context.resources
            .getDimensionPixelSize(R.dimen.now_playing_top_margin)
        if (viewType == TYPE_FIRST) {
            layoutParams.leftMargin = listMargin
        } else if (viewType == TYPE_LAST) {
            layoutParams.rightMargin = listMargin
        }
    }

    fun getItemViewType(position: Int, itemCount: Int): Int {
        return when (position) {
            0 -> TYPE_FIRST
            itemCount - 1 -> TYPE_LAST
            else -> TYPE_MIDDLE
        }
    }
}
