package code.name.monkey.retromusic.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

/** Created by trung on 7/11/2017. */
public final class BitmapEditor {
  /**
   * Stack Blur v1.0 from http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html Java
   * Author: Mario Klingemann <mario at quasimondo.com> http://incubator.quasimondo.com
   *
   * <p>created Feburary 29, 2004 Android port : Yahel Bouaziz <yahel at kayenko.com>
   * http://www.kayenko.com ported april 5th, 2012
   *
   * <p>This is A compromise between Gaussian Blur and Box blur It creates much better looking blurs
   * than Box Blur, but is 7x faster than my Gaussian Blur implementation.
   *
   * <p>I called it Stack Blur because this describes best how this filter works internally: it
   * creates A kind of moving stack of colors whilst scanning through the image. Thereby it just has
   * to add one new block of color to the right side of the stack and removeFromParent the leftmost
   * color. The remaining colors on the topmost layer of the stack are either added on or reduced by
   * one, depending on if they are on the right or on the x side of the stack.
   *
   * <p>If you are using this algorithm in your code please add the following line: Stack Blur
   * Algorithm by Mario Klingemann <mario@quasimondo.com>
   */
  public static Bitmap FastBlurSupportAlpha(Bitmap sentBitmap, float scale, int radius) {

    int width = Math.round(sentBitmap.getWidth() * scale);
    int height = Math.round(sentBitmap.getHeight() * scale);
    sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

    Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

    if (radius < 1) {
      return (null);
    }

    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    int[] pix = new int[w * h];
    //  Log.e("pix", w + " " + h + " " + pix.length);
    bitmap.getPixels(pix, 0, w, 0, 0, w, h);

    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    int[] r = new int[wh];
    int[] g = new int[wh];
    int[] b = new int[wh];
    int[] a = new int[wh];
    int rsum, gsum, bsum, asum, x, y, i, p, yp, yi, yw;
    int[] vmin = new int[Math.max(w, h)];

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int[] dv = new int[256 * divsum];
    for (i = 0; i < 256 * divsum; i++) {
      dv[i] = (i / divsum);
    }

    yw = yi = 0;

    int[][] stack = new int[div][4];
    int stackpointer;
    int stackstart;
    int[] sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum, aoutsum;
    int rinsum, ginsum, binsum, ainsum;

    for (y = 0; y < h; y++) {
      rinsum =
          ginsum =
              binsum =
                  ainsum = routsum = goutsum = boutsum = aoutsum = rsum = gsum = bsum = asum = 0;
      for (i = -radius; i <= radius; i++) {
        p = pix[yi + Math.min(wm, Math.max(i, 0))];
        sir = stack[i + radius];
        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        sir[3] = 0xff & (p >> 24);

        rbs = r1 - Math.abs(i);
        rsum += sir[0] * rbs;
        gsum += sir[1] * rbs;
        bsum += sir[2] * rbs;
        asum += sir[3] * rbs;
        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
          ainsum += sir[3];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
          aoutsum += sir[3];
        }
      }
      stackpointer = radius;

      for (x = 0; x < w; x++) {

        r[yi] = dv[rsum];
        g[yi] = dv[gsum];
        b[yi] = dv[bsum];
        a[yi] = dv[asum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;
        asum -= aoutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];
        aoutsum -= sir[3];

        if (y == 0) {
          vmin[x] = Math.min(x + radius + 1, wm);
        }
        p = pix[yw + vmin[x]];

        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        sir[3] = 0xff & (p >> 24);

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];
        ainsum += sir[3];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;
        asum += ainsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[(stackpointer) % div];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];
        aoutsum += sir[3];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];
        ainsum -= sir[3];

        yi++;
      }
      yw += w;
    }
    for (x = 0; x < w; x++) {
      rinsum =
          ginsum =
              binsum =
                  ainsum = routsum = goutsum = boutsum = aoutsum = rsum = gsum = bsum = asum = 0;
      yp = -radius * w;
      for (i = -radius; i <= radius; i++) {
        yi = Math.max(0, yp) + x;

        sir = stack[i + radius];

        sir[0] = r[yi];
        sir[1] = g[yi];
        sir[2] = b[yi];
        sir[3] = a[yi];

        rbs = r1 - Math.abs(i);

        rsum += r[yi] * rbs;
        gsum += g[yi] * rbs;
        bsum += b[yi] * rbs;
        asum += a[yi] * rbs;

        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
          ainsum += sir[3];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
          aoutsum += sir[3];
        }

        if (i < hm) {
          yp += w;
        }
      }
      yi = x;
      stackpointer = radius;
      for (y = 0; y < h; y++) {
        pix[yi] = (dv[asum] << 24) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;
        asum -= aoutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];
        aoutsum -= sir[3];

        if (x == 0) {
          vmin[y] = Math.min(y + r1, hm) * w;
        }
        p = x + vmin[y];

        sir[0] = r[p];
        sir[1] = g[p];
        sir[2] = b[p];
        sir[3] = a[p];

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];
        ainsum += sir[3];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;
        asum += ainsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[stackpointer];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];
        aoutsum += sir[3];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];
        ainsum -= sir[3];

        yi += w;
      }
    }

    //   Log.e("pix", w + " " + h + " " + pix.length);
    bitmap.setPixels(pix, 0, w, 0, 0, w, h);

    return (bitmap);
  }

  public static boolean PerceivedBrightness(int will_White, int[] c) {
    double TBT = Math.sqrt(c[0] * c[0] * .241 + c[1] * c[1] * .691 + c[2] * c[2] * .068);
    //    Log.d("themee",TBT+"");
    return !(TBT > will_White);
  }

  public static int[] getAverageColorRGB(Bitmap bitmap) {
    final int width = bitmap.getWidth();
    final int height = bitmap.getHeight();
    int size = width * height;
    int pixelColor;
    int r, g, b;
    r = g = b = 0;
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        pixelColor = bitmap.getPixel(x, y);
        if (pixelColor == 0) {
          size--;
          continue;
        }
        r += Color.red(pixelColor);
        g += Color.green(pixelColor);
        b += Color.blue(pixelColor);
      }
    }
    r /= size;
    g /= size;
    b /= size;
    return new int[] {r, g, b};
  }

  public static Bitmap updateSat(Bitmap src, float settingSat) {

    int w = src.getWidth();
    int h = src.getHeight();

    Bitmap bitmapResult = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas canvasResult = new Canvas(bitmapResult);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(settingSat);
    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
    paint.setColorFilter(filter);
    canvasResult.drawBitmap(src, 0, 0, paint);
    canvasResult.setBitmap(null);
    canvasResult = null;
    return bitmapResult;
  }

  /**
   * Stack Blur v1.0 from http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html Java
   * Author: Mario Klingemann <mario at quasimondo.com> http://incubator.quasimondo.com
   *
   * <p>created Feburary 29, 2004 Android port : Yahel Bouaziz <yahel at kayenko.com>
   * http://www.kayenko.com ported april 5th, 2012
   *
   * <p>This is A compromise between Gaussian Blur and Box blur It creates much better looking blurs
   * than Box Blur, but is 7x faster than my Gaussian Blur implementation.
   *
   * <p>I called it Stack Blur because this describes best how this filter works internally: it
   * creates A kind of moving stack of colors whilst scanning through the image. Thereby it just has
   * to add one new block of color to the right side of the stack and removeFromParent the leftmost
   * color. The remaining colors on the topmost layer of the stack are either added on or reduced by
   * one, depending on if they are on the right or on the x side of the stack.
   *
   * <p>If you are using this algorithm in your code please add the following line: Stack Blur
   * Algorithm by Mario Klingemann <mario@quasimondo.com>
   */
  public static Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

    Bitmap afterscaleSentBitmap;
    Bitmap bitmap;
    if (scale != 1) {
      int width = Math.round(sentBitmap.getWidth() * scale); // lấy chiều rộng làm tròn
      int height = Math.round(sentBitmap.getHeight() * scale); // lấy chiều cao làm tròn
      afterscaleSentBitmap =
          Bitmap.createScaledBitmap(sentBitmap, width, height, false); // tạo bitmap scaled
      bitmap = afterscaleSentBitmap.copy(afterscaleSentBitmap.getConfig(), true);
      afterscaleSentBitmap.recycle();
    } else {
      bitmap = sentBitmap.copy(sentBitmap.getConfig(), true); // đơn giản chỉ copy
    }

    if (radius < 1) {
      return (sentBitmap.copy(sentBitmap.getConfig(), true));
    }

    int w = bitmap.getWidth(); //  w is the width of sample bitmap
    int h = bitmap.getHeight(); // h is the height of sample bitmap

    int[] pix = new int[w * h]; // pix is the arrary of all bitmap pixel

    bitmap.getPixels(pix, 0, w, 0, 0, w, h);

    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    int[] r = new int[wh];
    int[] g = new int[wh];
    int[] b = new int[wh];
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    int[] vmin = new int[Math.max(w, h)];

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int[] dv = new int[256 * divsum];
    for (i = 0; i < 256 * divsum; i++) {
      dv[i] = (i / divsum);
    }

    yw = yi = 0;

    int[][] stack = new int[div][3];
    int stackpointer;
    int stackstart;
    int[] sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      for (i = -radius; i <= radius; i++) {
        p = pix[yi + Math.min(wm, Math.max(i, 0))];
        sir = stack[i + radius];
        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        rbs = r1 - Math.abs(i);
        rsum += sir[0] * rbs;
        gsum += sir[1] * rbs;
        bsum += sir[2] * rbs;
        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }
      }
      stackpointer = radius;

      for (x = 0; x < w; x++) {

        r[yi] = dv[rsum];
        g[yi] = dv[gsum];
        b[yi] = dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];

        if (y == 0) {
          vmin[x] = Math.min(x + radius + 1, wm);
        }
        p = pix[yw + vmin[x]];

        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[(stackpointer) % div];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];

        yi++;
      }
      yw += w;
    }
    for (x = 0; x < w; x++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      yp = -radius * w;
      for (i = -radius; i <= radius; i++) {
        yi = Math.max(0, yp) + x;

        sir = stack[i + radius];

        sir[0] = r[yi];
        sir[1] = g[yi];
        sir[2] = b[yi];

        rbs = r1 - Math.abs(i);

        rsum += r[yi] * rbs;
        gsum += g[yi] * rbs;
        bsum += b[yi] * rbs;

        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }

        if (i < hm) {
          yp += w;
        }
      }
      yi = x;
      stackpointer = radius;
      for (y = 0; y < h; y++) {
        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];

        if (x == 0) {
          vmin[y] = Math.min(y + r1, hm) * w;
        }
        p = x + vmin[y];

        sir[0] = r[p];
        sir[1] = g[p];
        sir[2] = b[p];

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[stackpointer];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];

        yi += w;
      }
    }

    bitmap.setPixels(pix, 0, w, 0, 0, w, h);

    return (bitmap);
  }

  public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
    Bitmap output =
        Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    //    final ScreenSize rectF = new ScreenSize(rect);
    final float roundPx = pixels;

    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    //   canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    canvas.drawPath(
        BitmapEditor.RoundedRect(
            0, 0, bitmap.getWidth(), bitmap.getHeight(), roundPx, roundPx, false),
        paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);

    return output;
  }

  /**
   * getResizedBitmap method is used to Resized the Image according to custom width and height
   *
   * @param image
   * @param newHeight (new desired height)
   * @param newWidth (new desired Width)
   * @return image (new resized image)
   */
  public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
    int width = image.getWidth();
    int height = image.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // create A matrix for the manipulation
    Matrix matrix = new Matrix();
    // onTap the bit map
    matrix.postScale(scaleWidth, scaleHeight);
    // recreate the new Bitmap
    Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
    return resizedBitmap;
  }

  public static boolean TrueIfBitmapBigger(Bitmap bitmap, int size) {
    int sizeBitmap =
        (bitmap.getHeight() > bitmap.getWidth()) ? bitmap.getHeight() : bitmap.getWidth();
    return sizeBitmap > size;
  }

  public static Bitmap GetRoundedBitmapWithBlurShadow(
      Bitmap original, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
    int original_width = original.getWidth();
    int orginal_height = original.getHeight();
    int bitmap_width = original_width + paddingLeft + paddingRight;
    int bitmap_height = orginal_height + paddingTop + paddingBottom;
    Bitmap bitmap = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setStyle(Paint.Style.FILL);
    // paint.setAlpha(60);
    //   canvas.drawRect(0,0,bitmap_width,bitmap_height,paint);
    paint.setAntiAlias(true);
    canvas.drawBitmap(original, paddingLeft, paddingTop, paint);
    Bitmap blurred_bitmap = getBlurredWithGoodPerformance(bitmap, 1, 6, 4);
    canvas.setBitmap(null);
    bitmap.recycle();
    return blurred_bitmap;
  }

  //                                                                            Activity.
  //                                                                                |
  // Original bitmap.
  //                                                                                 |
  //        |              To make the blur background, the original must to padding.
  //                                                                                  |
  //         |                          |                |                            |
  //          |
  //                                                                                   V
  //         V                         V              V                           V
  //     V
  public static Bitmap GetRoundedBitmapWithBlurShadow(
      Context context,
      Bitmap original,
      int paddingTop,
      int paddingBottom,
      int paddingLeft,
      int paddingRight,
      int TopBack // this value makes the overview bitmap is higher or  belower the background.
          ,
      int alphaBlurBackground // this is the alpha of the background Bitmap, you need A number
          // between 0 -> 255, the value recommend is 180.
          ,
      int valueBlurBackground // this is the value used to blur the background Bitmap, the
          // recommended one is 12.
          ,
      int valueSaturationBlurBackground // this is the value used to background Bitmap more
      // colorful, if valueBlur is 12, the valudeSaturation should
      // be 2.
      ) {
    int original_width = original.getWidth();
    int orginal_height = original.getHeight();
    int bitmap_width = original_width + paddingLeft + paddingRight;
    int bitmap_height = orginal_height + paddingTop + paddingBottom;
    Bitmap bitmap = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setAntiAlias(true);
    canvas.drawBitmap(original, paddingLeft, paddingTop, paint);
    Bitmap blurred_bitmap =
        getBlurredWithGoodPerformance(
            context, bitmap, 1, valueBlurBackground, valueSaturationBlurBackground);
    //   Bitmap blurred_bitmap= getBlurredWithGoodPerformance(context, bitmap,1,15,3);
    Bitmap end_bitmap = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);
    canvas.setBitmap(end_bitmap);
    paint.setAlpha(alphaBlurBackground);

    canvas.drawBitmap(
        blurred_bitmap,
        new Rect(0, 0, blurred_bitmap.getWidth(), blurred_bitmap.getHeight()),
        new Rect(0, 0, bitmap_width, bitmap_height),
        paint);
    paint.setAlpha(255);

    canvas.drawBitmap(bitmap, 0, TopBack, paint); // drawVisualWave cái lớn
    canvas.setBitmap(null);
    blurred_bitmap.recycle();
    bitmap.recycle();
    return end_bitmap;
  }

  public static void setBitmapforImageView(ImageView imv, Bitmap apply) {
    Bitmap old = ((BitmapDrawable) imv.getDrawable()).getBitmap();
    imv.setImageBitmap(apply);
    if (old != null) old.recycle();
  }

  public static Bitmap getBlurredWithGoodPerformance(
      Bitmap bitmap, int scale, int radius, int saturation) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap bitmap1 = getResizedBitmap(bitmap, 50, 50);
    Bitmap updateSatBitmap = updateSat(bitmap1, saturation);
    Bitmap blurredBitmap = FastBlurSupportAlpha(updateSatBitmap, scale, radius);

    updateSatBitmap.recycle();
    bitmap1.recycle();
    return blurredBitmap;
  }

  public static Path RoundedRect(
      float left,
      float top,
      float right,
      float bottom,
      float rx,
      float ry,
      boolean conformToOriginalPost) {
    Path path = new Path();
    if (rx < 0) rx = 0;
    if (ry < 0) ry = 0;
    float width = right - left;
    float height = bottom - top;
    if (rx > width / 2) rx = width / 2;
    if (ry > height / 2) ry = height / 2;
    float widthMinusCorners = (width - (2 * rx)); // do dai phan "thang" cua chieu rong
    float heightMinusCorners = (height - (2 * ry)); // do dai phan "thang" cua chieu dai

    path.moveTo(right, top + ry); // bat dau tu  day
    path.rQuadTo(0, -ry, -rx, -ry); // y-right corner
    path.rLineTo(-widthMinusCorners, 0);
    path.rQuadTo(-rx, 0, -rx, ry); // y-x corner
    path.rLineTo(0, heightMinusCorners);

    if (conformToOriginalPost) {
      path.rLineTo(0, ry);
      path.rLineTo(width, 0);
      path.rLineTo(0, -ry);
    } else {

      path.rQuadTo(0, ry, rx, ry); // bottom-x corner
      path.rLineTo(widthMinusCorners, 0);
      path.rQuadTo(rx, 0, rx, -ry); // bottom-right corner
    }

    path.rLineTo(0, -heightMinusCorners);

    path.close(); // Given close, last lineto can be removed.

    return path;
  }

  public static int mixTwoColors(int color1, int color2, float amount) {
    final byte ALPHA_CHANNEL = 24;
    final byte RED_CHANNEL = 16;
    final byte GREEN_CHANNEL = 8;
    final byte BLUE_CHANNEL = 0;

    final float inverseAmount = 1.0f - amount;

    int a =
        ((int)
                (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount)
                    + ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount)))
            & 0xff;
    int r =
        ((int)
                (((float) (color1 >> RED_CHANNEL & 0xff) * amount)
                    + ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount)))
            & 0xff;
    int g =
        ((int)
                (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount)
                    + ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount)))
            & 0xff;
    int b =
        ((int) (((float) (color1 & 0xff) * amount) + ((float) (color2 & 0xff) * inverseAmount)))
            & 0xff;

    return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
  }

  public static Bitmap getBlurredWithGoodPerformance(
      Context context, Bitmap bitmap, int scale, int radius, float saturation) {
    Bitmap bitmap1 = getResizedBitmap(bitmap, 150, 150);
    Bitmap updateSatBimap = updateSat(bitmap1, saturation);
    Bitmap blurredBitmap = BlurBitmapWithRenderScript(context, updateSatBimap, radius);
    updateSatBimap.recycle();
    bitmap1.recycle();
    return blurredBitmap;
  }

  public static Bitmap getBlurredBimapWithRenderScript(
      Context context, Bitmap bitmapOriginal, float radius) {
    // define this only once if blurring multiple times
    RenderScript rs = RenderScript.create(context);

    // this will blur the bitmapOriginal with A radius of 8 and save it in bitmapOriginal
    final Allocation input =
        Allocation.createFromBitmap(
            rs, bitmapOriginal); // use this constructor for best performance, because it uses
    // USAGE_SHARED mode which reuses memory
    final Allocation output = Allocation.createTyped(rs, input.getType());
    final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    script.setRadius(radius);
    script.setInput(input);
    script.forEach(output);
    output.copyTo(bitmapOriginal);
    return bitmapOriginal;
  }

  public static Bitmap BlurBitmapWithRenderScript(Context context, Bitmap bitmap, float radius) {
    // Let's create an empty bitmap with the same size of the bitmap we want to blur
    Bitmap outBitmap =
        Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

    // Instantiate A new Renderscript
    RenderScript rs = RenderScript.create(context);

    // Create an Intrinsic Blur Script using the Renderscript
    ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

    // Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
    Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
    Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
    // Set the radius of the blur
    blurScript.setRadius(radius);

    // Perform the Renderscript
    blurScript.setInput(allIn);
    blurScript.forEach(allOut);

    // Copy the final bitmap created by the out Allocation to the outBitmap
    allOut.copyTo(outBitmap);

    // recycle the original bitmap

    // After finishing everything, we destroy the Renderscript.
    rs.destroy();

    return outBitmap;
  }

  public static Drawable covertBitmapToDrawable(Context context, Bitmap bitmap) {
    Drawable d = new BitmapDrawable(context.getResources(), bitmap);
    return d;
  }

  public static Bitmap convertDrawableToBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }
    Bitmap bitmap =
        Bitmap.createBitmap(
            drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  public static Bitmap changeBitmapColor(Bitmap sourceBitmap, int color) {
    Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
    Paint paint = new Paint();
    ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
    paint.setColorFilter(filter);
    Canvas canvas = new Canvas(resultBitmap);
    canvas.drawBitmap(resultBitmap, 0, 0, paint);
    return resultBitmap;
  }

  /**
   * @param mode
   * @return 0 : CLEAR <br>
   *     1 : SRC <br>
   *     2 : DST <br>
   *     3 : SRC_OVER <br>
   *     4 : DST_OVER <br>
   *     5 : SRC_IN <br>
   *     6 : DST_IN <br>
   *     7 : SRC_OUT <br>
   *     8 : DST_OUT <br>
   *     9 : SRC_ATOP <br>
   *     10 : DST_ATOP <br>
   *     11 : XOR <br>
   *     12 : ADD <br>
   *     13 : MULTIPLY <br>
   *     14 : SCREEN <br>
   *     15 : OVERLAY <br>
   *     16 : DARKEN <br>
   *     17 : LIGHTEN
   */
  public static PorterDuff.Mode getPorterMode(int mode) {
    switch (mode) {
      default:
      case 0:
        return PorterDuff.Mode.CLEAR;
      case 1:
        return PorterDuff.Mode.SRC;
      case 2:
        return PorterDuff.Mode.DST;
      case 3:
        return PorterDuff.Mode.SRC_OVER;
      case 4:
        return PorterDuff.Mode.DST_OVER;
      case 5:
        return PorterDuff.Mode.SRC_IN;
      case 6:
        return PorterDuff.Mode.DST_IN;
      case 7:
        return PorterDuff.Mode.SRC_OUT;
      case 8:
        return PorterDuff.Mode.DST_OUT;
      case 9:
        return PorterDuff.Mode.SRC_ATOP;
      case 10:
        return PorterDuff.Mode.DST_ATOP;
      case 11:
        return PorterDuff.Mode.XOR;
      case 16:
        return PorterDuff.Mode.DARKEN;
      case 17:
        return PorterDuff.Mode.LIGHTEN;
      case 13:
        return PorterDuff.Mode.MULTIPLY;
      case 14:
        return PorterDuff.Mode.SCREEN;
      case 12:
        return PorterDuff.Mode.ADD;
      case 15:
        return PorterDuff.Mode.OVERLAY;
    }
  }

  public static void applyNewColor4Bitmap(
      Context context, int[] idBitmaps, ImageView[] imageViews, int color, float alpha) {
    android.content.res.Resources resource = context.getResources();
    int size = idBitmaps.length;
    Bitmap usingBitmap, resultBitmap;
    for (int i = 0; i < size; i++) {
      usingBitmap = BitmapFactory.decodeResource(resource, idBitmaps[i]);
      resultBitmap = changeBitmapColor(usingBitmap, color);
      imageViews[i].setImageBitmap(resultBitmap);
      imageViews[i].setAlpha(alpha);
    }
  }

  public static void applyNewColor4Bitmap(
      Context context, int idBitmap, ImageView applyView, int color, float alpha) {

    android.content.res.Resources resource = context.getResources();
    Bitmap usingBitmap = BitmapFactory.decodeResource(resource, idBitmap);
    Bitmap resultBitmap = changeBitmapColor(usingBitmap, color);
    applyView.setImageBitmap(resultBitmap);
    applyView.setAlpha(alpha);
  }

  public static Bitmap getBitmapFromView(View view) {
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bitmap);
    view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    view.draw(c);
    return bitmap;
  }

  public static Bitmap getBitmapFromView(View view, int left, int top, int right, int bottom) {
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bitmap);
    view.layout(left, top, right, bottom);
    view.draw(c);
    return bitmap;
  }

  public static Bitmap getBackgroundBitmapAViewWithParent(View childView, View parentView) {
    int[] pos_child = new int[2];
    childView.getLocationOnScreen(pos_child);
    return getBitmapFromView(
        parentView, pos_child[0], pos_child[1], parentView.getRight(), parentView.getBottom());
  }

  public static Bitmap getBackgroundBlurAViewWithParent(
      Activity activity, View childView, View parentView) {
    Bitmap b1 = getBackgroundBitmapAViewWithParent(childView, parentView);
    Bitmap b2 = getBlurredWithGoodPerformance(activity, b1, 1, 8, 2);
    b1.recycle();
    return b2;
  }
}
