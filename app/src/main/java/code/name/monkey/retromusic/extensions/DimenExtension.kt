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
package code.name.monkey.retromusic.extensions

import android.app.Activity
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.dimToPixel(@DimenRes dimenRes: Int): Int {
    return resources.getDimensionPixelSize(dimenRes)
}

fun Activity.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}

fun Fragment.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}
