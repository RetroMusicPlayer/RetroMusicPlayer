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

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

public class BitmapPaletteResource implements Resource<BitmapPaletteWrapper> {

  private final BitmapPaletteWrapper bitmapPaletteWrapper;
  private final BitmapPool bitmapPool;

  public BitmapPaletteResource(BitmapPaletteWrapper bitmapPaletteWrapper, BitmapPool bitmapPool) {
    this.bitmapPaletteWrapper = bitmapPaletteWrapper;
    this.bitmapPool = bitmapPool;
  }

  @Override
  public BitmapPaletteWrapper get() {
    return bitmapPaletteWrapper;
  }

  @Override
  public int getSize() {
    return Util.getBitmapByteSize(bitmapPaletteWrapper.getBitmap());
  }

  @Override
  public void recycle() {
    if (!bitmapPool.put(bitmapPaletteWrapper.getBitmap())) {
      bitmapPaletteWrapper.getBitmap().recycle();
    }
  }
}
