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

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette

fun getSuitableColorFor(palette: Palette, i: Int, i2: Int): Int {
    val dominantSwatch = palette.dominantSwatch
    if (dominantSwatch != null) {
        if (hasEnoughContrast(i, dominantSwatch.rgb)) {
            return dominantSwatch.rgb
        }
    }
    val vibrantSwatch = palette.vibrantSwatch
    if (vibrantSwatch != null) {
        if (hasEnoughContrast(i, vibrantSwatch.rgb)) {
            return vibrantSwatch.rgb
        }
    }
    val darkVibrantSwatch = palette.darkVibrantSwatch
    if (darkVibrantSwatch != null) {
        if (hasEnoughContrast(i, darkVibrantSwatch.rgb)) {
            return darkVibrantSwatch.rgb
        }
    }
    val lightVibrantSwatch = palette.lightVibrantSwatch
    if (lightVibrantSwatch != null) {
        if (hasEnoughContrast(i, lightVibrantSwatch.rgb)) {
            return lightVibrantSwatch.rgb
        }
    }
    val darkMutedSwatch = palette.darkMutedSwatch
    if (darkMutedSwatch != null) {
        if (hasEnoughContrast(i, darkMutedSwatch.rgb)) {
            return darkMutedSwatch.rgb
        }
    }
    val lightMutedSwatch = palette.lightMutedSwatch
    if (lightMutedSwatch != null) {
        if (hasEnoughContrast(i, lightMutedSwatch.rgb)) {
            return lightMutedSwatch.rgb
        }
    }
    val mutedSwatch = palette.mutedSwatch
    if (mutedSwatch != null) {
        if (hasEnoughContrast(i, mutedSwatch.rgb)) {
            return mutedSwatch.rgb
        }
    }
    return i2
}

fun hasEnoughContrast(i: Int, i2: Int): Boolean {
    return ColorUtils.calculateContrast(i2, i) >= 2.toDouble()
}

fun hasEnoughLuminance(i: Int): Boolean {
    return ColorUtils.calculateLuminance(i) >= 0.4f.toDouble()
}

fun isBlack(fArr: FloatArray): Boolean {
    return fArr[2] <= 0.035f
}

fun isNearRedLine(fArr: FloatArray): Boolean {
    val f = fArr[0]
    return f in 10.0f..37.0f && fArr[1] <= 0.82f
}

fun isDark(@ColorInt i: Int): Boolean {
    return ColorUtils.calculateLuminance(i) < 0.5
}
