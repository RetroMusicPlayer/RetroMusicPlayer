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

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;

public class StatusBarView extends View {

  public StatusBarView(@NonNull Context context) {
    super(context);
    init(context);
  }

  public StatusBarView(@NonNull Context context, @NonNull AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public StatusBarView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public static int getStatusBarHeight(@NonNull Resources r) {
    int result = 0;
    int resourceId = r.getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = r.getDimensionPixelSize(resourceId);
    }
    return result;
  }

  private void init(Context context) {}

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getStatusBarHeight(getResources()));
  }
}
