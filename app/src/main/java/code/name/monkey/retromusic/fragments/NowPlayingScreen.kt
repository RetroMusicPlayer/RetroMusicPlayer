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
package code.name.monkey.retromusic.fragments

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import code.name.monkey.retromusic.R

enum class NowPlayingScreen constructor(
    @param:StringRes @field:StringRes
    val titleRes: Int,
    @param:DrawableRes @field:DrawableRes val drawableResId: Int,
    val id: Int
) {

    Adaptive(R.string.adaptive, R.drawable.np_adaptive, 10),
    Blur(R.string.blur, R.drawable.np_blur, 4),
    BlurCard(R.string.blur_card, R.drawable.np_blur_card, 9),
    Card(R.string.card, R.drawable.np_card, 6),
    Circle(R.string.circle, R.drawable.np_minimalistic_circle, 15),
    Classic(R.string.classic, R.drawable.np_classic, 16),
    Color(R.string.color, R.drawable.np_color, 5),
    Fit(R.string.fit, R.drawable.np_fit, 12),
    Flat(R.string.flat, R.drawable.np_flat, 1),
    Full(R.string.full, R.drawable.np_full, 2),
    Gradient(R.string.gradient, R.drawable.np_gradient, 17),
    Material(R.string.material, R.drawable.np_material, 11),
    Normal(R.string.normal, R.drawable.np_normal, 0),
    Peak(R.string.peak, R.drawable.np_peak, 14),
    Plain(R.string.plain, R.drawable.np_plain, 3),
    Simple(R.string.simple, R.drawable.np_simple, 8),
    Tiny(R.string.tiny, R.drawable.np_tiny, 7),
}
