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

package code.name.monkey.retromusic.glide.palette;

import android.graphics.Bitmap;

import androidx.palette.graphics.Palette;

public class BitmapPaletteWrapper {
  private final Bitmap mBitmap;
  private final Palette mPalette;

  public BitmapPaletteWrapper(Bitmap bitmap, Palette palette) {
    mBitmap = bitmap;
    mPalette = palette;
  }

  public Bitmap getBitmap() {
    return mBitmap;
  }

  public Palette getPalette() {
    return mPalette;
  }
}
