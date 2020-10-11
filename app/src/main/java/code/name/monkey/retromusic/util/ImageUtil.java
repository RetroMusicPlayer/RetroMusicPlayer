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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import code.name.monkey.appthemehelper.util.TintHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on : June 18, 2016 Author : zetbaitsu Name : Zetra GitHub : https://github.com/zetbaitsu
 */
public class ImageUtil {

  private static final int TOLERANCE = 20;
  // Alpha amount for which values below are considered transparent.
  private static final int ALPHA_TOLERANCE = 50;
  private static int[] mTempBuffer;

  private ImageUtil() {}

  public static boolean isGrayscale(Bitmap bitmap) {
    final int height = bitmap.getHeight();
    final int width = bitmap.getWidth();
    int size = height * width;
    ensureBufferSize(size);
    bitmap.getPixels(mTempBuffer, 0, width, 0, 0, width, height);
    for (int i = 0; i < size; i++) {
      if (!isGrayscale(mTempBuffer[i])) {
        return false;
      }
    }
    return true;
  }

  public static Bitmap createBitmap(Drawable drawable) {
    return createBitmap(drawable, 1f);
  }

  public static Bitmap createBitmap(Drawable drawable, float sizeMultiplier) {
    Bitmap bitmap =
        Bitmap.createBitmap(
            (int) (drawable.getIntrinsicWidth() * sizeMultiplier),
            (int) (drawable.getIntrinsicHeight() * sizeMultiplier),
            Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bitmap);
    drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
    drawable.draw(c);
    return bitmap;
  }

  public static Drawable getTintedVectorDrawable(
      @NonNull Resources res,
      @DrawableRes int resId,
      @Nullable Resources.Theme theme,
      @ColorInt int color) {
    return TintHelper.createTintedDrawable(getVectorDrawable(res, resId, theme), color);
  }

  public static Drawable getTintedVectorDrawable(
      @NonNull Context context, @DrawableRes int id, @ColorInt int color) {
    return TintHelper.createTintedDrawable(
        getVectorDrawable(context.getResources(), id, context.getTheme()), color);
  }

  public static Drawable getVectorDrawable(@NonNull Context context, @DrawableRes int id) {
    return getVectorDrawable(context.getResources(), id, context.getTheme());
  }

  public static Drawable getVectorDrawable(
      @NonNull Resources res, @DrawableRes int resId, @Nullable Resources.Theme theme) {
    if (Build.VERSION.SDK_INT >= 21) {
      return res.getDrawable(resId, theme);
    }
    return VectorDrawableCompat.create(res, resId, theme);
  }

  /** Makes sure that {@code mTempBuffer} has at least length {@code size}. */
  private static void ensureBufferSize(int size) {
    if (mTempBuffer == null || mTempBuffer.length < size) {
      mTempBuffer = new int[size];
    }
  }

  public static Bitmap setBitmapColor(Bitmap bitmap, int color) {
    Bitmap result =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
    Paint paint = new Paint();
    paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));

    Canvas canvas = new Canvas(result);
    canvas.drawBitmap(result, 0, 0, paint);

    return result;
  }

  public static boolean isGrayscale(int color) {
    int alpha = 0xFF & (color >> 24);
    if (alpha < ALPHA_TOLERANCE) {
      return true;
    }
    int r = 0xFF & (color >> 16);
    int g = 0xFF & (color >> 8);
    int b = 0xFF & color;
    return Math.abs(r - g) < TOLERANCE
        && Math.abs(r - b) < TOLERANCE
        && Math.abs(g - b) < TOLERANCE;
  } // Amount (max is 255) that two channels can differ before the color is no longer "gray".

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

  static File compressImage(
      File imageFile,
      int reqWidth,
      int reqHeight,
      Bitmap.CompressFormat compressFormat,
      int quality,
      String destinationPath)
      throws IOException {
    FileOutputStream fileOutputStream = null;
    File file = new File(destinationPath).getParentFile();
    if (!file.exists()) {
      file.mkdirs();
    }
    try {
      fileOutputStream = new FileOutputStream(destinationPath);
      // write the compressed bitmap at the destination specified by destinationPath.
      decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight)
          .compress(compressFormat, quality, fileOutputStream);
    } finally {
      if (fileOutputStream != null) {
        fileOutputStream.flush();
        fileOutputStream.close();
      }
    }

    return new File(destinationPath);
  }

  static Bitmap decodeSampledBitmapFromFile(File imageFile, int reqWidth, int reqHeight)
      throws IOException {
    // First decode with inJustDecodeBounds=true to check dimensions
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;

    Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

    // check the rotation of the image and display it properly
    ExifInterface exif;
    exif = new ExifInterface(imageFile.getAbsolutePath());
    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
    Matrix matrix = new Matrix();
    if (orientation == 6) {
      matrix.postRotate(90);
    } else if (orientation == 3) {
      matrix.postRotate(180);
    } else if (orientation == 8) {
      matrix.postRotate(270);
    }
    scaledBitmap =
        Bitmap.createBitmap(
            scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    return scaledBitmap;
  }

  private static int calculateInSampleSize(
      BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }

  @NonNull
  public static Bitmap getResizedBitmap(@NonNull Bitmap image, int maxSize) {
    int width = image.getWidth();
    int height = image.getHeight();

    float bitmapRatio = (float) width / (float) height;
    if (bitmapRatio > 1) {
      width = maxSize;
      height = (int) (width / bitmapRatio);
    } else {
      height = maxSize;
      width = (int) (height * bitmapRatio);
    }
    return Bitmap.createScaledBitmap(image, width, height, true);
  }

  public static Bitmap resize(InputStream stream, int scaledWidth, int scaledHeight) {
    final Bitmap bitmap = BitmapFactory.decodeStream(stream);
    return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
  }
}
