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

package code.name.monkey.retromusic.util;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

/**
 * Created on : June 18, 2016 Author : zetbaitsu Name : Zetra GitHub : https://github.com/zetbaitsu
 */
public class ImageUtil {

  private ImageUtil() {}

  public static Bitmap resizeBitmap(@NonNull Bitmap src, int maxForSmallerSize) {
    int width = src.getWidth();
    int height = src.getHeight();

    final int dstWidth;
    final int dstHeight;

    if (width < height) {
      if (maxForSmallerSize >= width) {
        return src;
      }
      float ratio = (float) height / width;
      dstWidth = maxForSmallerSize;
      dstHeight = Math.round(maxForSmallerSize * ratio);
    } else {
      if (maxForSmallerSize >= height) {
        return src;
      }
      float ratio = (float) width / height;
      dstWidth = Math.round(maxForSmallerSize * ratio);
      dstHeight = maxForSmallerSize;
    }

    return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
  }

  public static int calculateInSampleSize(int width, int height, int reqWidth) {
    // setting reqWidth matching to desired 1:1 ratio and screen-size
    if (width < height) {
      reqWidth = (height / width) * reqWidth;
    } else {
      reqWidth = (width / height) * reqWidth;
    }

    int inSampleSize = 1;

    if (height > reqWidth || width > reqWidth) {
      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqWidth && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }
}
