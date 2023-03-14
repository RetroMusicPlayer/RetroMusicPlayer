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

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Util;

public class BitmapPaletteResource implements Resource<BitmapPaletteWrapper> {

  private final BitmapPaletteWrapper bitmapPaletteWrapper;

  public BitmapPaletteResource(BitmapPaletteWrapper bitmapPaletteWrapper) {
    this.bitmapPaletteWrapper = bitmapPaletteWrapper;
  }

  @NonNull
  @Override
  public BitmapPaletteWrapper get() {
    return bitmapPaletteWrapper;
  }

  @NonNull
  @Override
  public Class<BitmapPaletteWrapper> getResourceClass() {
    return BitmapPaletteWrapper.class;
  }

  @Override
  public int getSize() {
    return Util.getBitmapByteSize(bitmapPaletteWrapper.getBitmap());
  }

  @Override
  public void recycle() {
    bitmapPaletteWrapper.getBitmap().recycle();
  }
}
