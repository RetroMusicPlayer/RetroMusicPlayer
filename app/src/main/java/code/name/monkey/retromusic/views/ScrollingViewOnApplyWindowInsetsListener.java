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

import android.graphics.Rect;
import android.view.View;
import android.view.WindowInsets;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.zhanghai.android.fastscroll.FastScroller;

public class ScrollingViewOnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {

  @NonNull private final Rect mPadding = new Rect();
  @Nullable private final FastScroller mFastScroller;

  public ScrollingViewOnApplyWindowInsetsListener(
      @Nullable View view, @Nullable FastScroller fastScroller) {
    if (view != null) {
      mPadding.set(
          view.getPaddingLeft(),
          view.getPaddingTop(),
          view.getPaddingRight(),
          view.getPaddingBottom());
    }
    mFastScroller = fastScroller;
  }

  public ScrollingViewOnApplyWindowInsetsListener() {
    this(null, null);
  }

  @NonNull
  @Override
  public WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets insets) {
    view.setPadding(
        mPadding.left + insets.getSystemWindowInsetLeft(),
        mPadding.top,
        mPadding.right + insets.getSystemWindowInsetRight(),
        mPadding.bottom + insets.getSystemWindowInsetBottom());
    if (mFastScroller != null) {
      mFastScroller.setPadding(
          insets.getSystemWindowInsetLeft(),
          0,
          insets.getSystemWindowInsetRight(),
          insets.getSystemWindowInsetBottom());
    }
    return insets;
  }
}
