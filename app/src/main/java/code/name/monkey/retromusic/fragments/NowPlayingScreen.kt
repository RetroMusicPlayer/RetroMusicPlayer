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
    val id: Int,
    val defaultCoverTheme: AlbumCoverStyle?
) {
    // Some Now playing themes look better with particular Album cover theme

    Adaptive(R.string.adaptive, R.drawable.np_adaptive, 10, AlbumCoverStyle.FullCard),
    Blur(R.string.blur, R.drawable.np_blur, 4, AlbumCoverStyle.Normal),
    BlurCard(R.string.blur_card, R.drawable.np_blur_card, 9, AlbumCoverStyle.Card),
    Card(R.string.card, R.drawable.np_card, 6, AlbumCoverStyle.Full),
    Circle(R.string.circle, R.drawable.np_minimalistic_circle, 15, null),
    Classic(R.string.classic, R.drawable.np_classic, 16, AlbumCoverStyle.Full),
    Color(R.string.color, R.drawable.np_color, 5, AlbumCoverStyle.Normal),
    Fit(R.string.fit, R.drawable.np_fit, 12, AlbumCoverStyle.Full),
    Flat(R.string.flat, R.drawable.np_flat, 1, AlbumCoverStyle.Flat),
    Full(R.string.full, R.drawable.np_full, 2, AlbumCoverStyle.Full),
    Gradient(R.string.gradient, R.drawable.np_gradient, 17, AlbumCoverStyle.Full),
    Material(R.string.material, R.drawable.np_material, 11, AlbumCoverStyle.Normal),
    MD3(R.string.md3, R.drawable.np_normal, 18, AlbumCoverStyle.Normal),
    Normal(R.string.normal, R.drawable.np_normal, 0, AlbumCoverStyle.Normal),
    Peek(R.string.peek, R.drawable.np_peek, 14, AlbumCoverStyle.Normal),
    Plain(R.string.plain, R.drawable.np_plain, 3, AlbumCoverStyle.Normal),
    Simple(R.string.simple, R.drawable.np_simple, 8, AlbumCoverStyle.Normal),
    Tiny(R.string.tiny, R.drawable.np_tiny, 7, null),
}
