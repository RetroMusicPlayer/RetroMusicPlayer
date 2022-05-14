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

package code.name.monkey.retromusic.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class HeightFitSquareLayout extends FrameLayout {
  private boolean forceSquare = true;

  public HeightFitSquareLayout(Context context) {
    super(context);
  }

  public HeightFitSquareLayout(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public HeightFitSquareLayout(Context context, AttributeSet attributeSet, int i) {
    super(context, attributeSet, i);
  }

  @TargetApi(21)
  public HeightFitSquareLayout(Context context, AttributeSet attributeSet, int i, int i2) {
    super(context, attributeSet, i, i2);
  }

  public void forceSquare(boolean z) {
    this.forceSquare = z;
    requestLayout();
  }

  protected void onMeasure(int i, int i2) {
    if (this.forceSquare) {
      i = i2;
    }
    super.onMeasure(i, i2);
  }
}
