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
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;

public class StatusBarMarginFrameLayout extends FrameLayout {

  public StatusBarMarginFrameLayout(@NonNull Context context) {
    super(context);
  }

  public StatusBarMarginFrameLayout(@NonNull Context context, @NonNull AttributeSet attrs) {
    super(context, attrs);
  }

  public StatusBarMarginFrameLayout(
      @NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @NonNull
  @Override
  public WindowInsets onApplyWindowInsets(@NonNull WindowInsets insets) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
      lp.topMargin = insets.getSystemWindowInsetTop();
      lp.bottomMargin = insets.getSystemWindowInsetBottom();
      setLayoutParams(lp);
    }
    return super.onApplyWindowInsets(insets);
  }
}
