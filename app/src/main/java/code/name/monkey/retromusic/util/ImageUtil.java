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
