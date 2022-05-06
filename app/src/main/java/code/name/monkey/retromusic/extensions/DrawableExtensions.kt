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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap

fun Context.scaledDrawableResources(
    @DrawableRes id: Int,
    @DimenRes width: Int,
    @DimenRes height: Int,
): Drawable {
    val w = resources.getDimension(width).toInt()
    val h = resources.getDimension(height).toInt()
    return scaledDrawable(id, w, h)
}

fun Context.scaledDrawable(@DrawableRes id: Int, width: Int, height: Int): Drawable {
    val bmp = BitmapFactory.decodeResource(resources, id)
    val bmpScaled = Bitmap.createScaledBitmap(bmp, width, height, false)
    return BitmapDrawable(resources, bmpScaled)
}

fun Drawable.toBitmap(scaleFactor: Float, config: Bitmap.Config? = null): Bitmap {
    return toBitmap((intrinsicHeight*scaleFactor).toInt(), (intrinsicWidth*scaleFactor).toInt(), config)
}

fun Drawable.getBitmapDrawable(): Bitmap {
    val bmp = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    return bmp
}