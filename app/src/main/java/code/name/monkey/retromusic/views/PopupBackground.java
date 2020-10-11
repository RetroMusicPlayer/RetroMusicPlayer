/*
 * Copyright (c) 2020 Hemanth Savarala.
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

package code.name.monkey.retromusic.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;

public class PopupBackground extends Drawable {

  private final int mPaddingEnd;

  private final int mPaddingStart;

  @NonNull private final Paint mPaint;

  @NonNull private final Path mPath = new Path();

  @NonNull private final Matrix mTempMatrix = new Matrix();

  public PopupBackground(@NonNull Context context) {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(ThemeStore.Companion.accentColor(context));
    mPaint.setStyle(Paint.Style.FILL);
    Resources resources = context.getResources();
    mPaddingStart = resources.getDimensionPixelOffset(R.dimen.afs_md2_popup_padding_start);
    mPaddingEnd = resources.getDimensionPixelOffset(R.dimen.afs_md2_popup_padding_end);
  }

  private static void pathArcTo(
      @NonNull Path path,
      float centerX,
      float centerY,
      float radius,
      float startAngle,
      float sweepAngle) {
    path.arcTo(
        centerX - radius,
        centerY - radius,
        centerX + radius,
        centerY + radius,
        startAngle,
        sweepAngle,
        false);
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    canvas.drawPath(mPath, mPaint);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public void getOutline(@NonNull Outline outline) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !mPath.isConvex()) {
      // The outline path must be convex before Q, but we may run into floating point error
      // caused by calculation involving sqrt(2) or OEM implementation difference, so in this
      // case we just omit the shadow instead of crashing.
      super.getOutline(outline);
      return;
    }
    outline.setConvexPath(mPath);
  }

  @Override
  public void setAlpha(int alpha) {}

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {}

  @Override
  public boolean getPadding(@NonNull Rect padding) {
    if (needMirroring()) {
      padding.set(mPaddingEnd, 0, mPaddingStart, 0);
    } else {
      padding.set(mPaddingStart, 0, mPaddingEnd, 0);
    }
    return true;
  }

  @Override
  public boolean isAutoMirrored() {
    return true;
  }

  @Override
  public boolean onLayoutDirectionChanged(int layoutDirection) {
    updatePath();
    return true;
  }

  @Override
  protected void onBoundsChange(@NonNull Rect bounds) {
    updatePath();
  }

  private boolean needMirroring() {
    return DrawableCompat.getLayoutDirection(this) == View.LAYOUT_DIRECTION_RTL;
  }

  private void updatePath() {

    mPath.reset();

    Rect bounds = getBounds();
    float width = bounds.width();
    float height = bounds.height();
    float r = height / 2;
    float sqrt2 = (float) Math.sqrt(2);
    // Ensure we are convex.
    width = Math.max(r + sqrt2 * r, width);
    pathArcTo(mPath, r, r, r, 90, 180);
    float o1X = width - sqrt2 * r;
    pathArcTo(mPath, o1X, r, r, -90, 45f);
    float r2 = r / 5;
    float o2X = width - sqrt2 * r2;
    pathArcTo(mPath, o2X, r, r2, -45, 90);
    pathArcTo(mPath, o1X, r, r, 45f, 45f);
    mPath.close();

    if (needMirroring()) {
      mTempMatrix.setScale(-1, 1, width / 2, 0);
    } else {
      mTempMatrix.reset();
    }
    mTempMatrix.postTranslate(bounds.left, bounds.top);
    mPath.transform(mTempMatrix);
  }
}
