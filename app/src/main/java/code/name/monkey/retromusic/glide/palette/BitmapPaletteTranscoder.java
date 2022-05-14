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

import android.content.Context;
import android.graphics.Bitmap;
import code.name.monkey.retromusic.util.RetroColorUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class BitmapPaletteTranscoder implements ResourceTranscoder<Bitmap, BitmapPaletteWrapper> {
  private final BitmapPool bitmapPool;

  public BitmapPaletteTranscoder(Context context) {
    this(Glide.get(context).getBitmapPool());
  }

  public BitmapPaletteTranscoder(BitmapPool bitmapPool) {
    this.bitmapPool = bitmapPool;
  }

  @Override
  public Resource<BitmapPaletteWrapper> transcode(Resource<Bitmap> bitmapResource) {
    Bitmap bitmap = bitmapResource.get();
    BitmapPaletteWrapper bitmapPaletteWrapper =
        new BitmapPaletteWrapper(bitmap, RetroColorUtil.generatePalette(bitmap));
    return new BitmapPaletteResource(bitmapPaletteWrapper, bitmapPool);
  }

  @Override
  public String getId() {
    return "BitmapPaletteTranscoder.com.kabouzeid.gramophone.glide.palette";
  }
}
