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
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

public class LollipopFixedWebView extends WebView {
  public LollipopFixedWebView(Context context) {
    super(getFixedContext(context));
  }

  public LollipopFixedWebView(Context context, AttributeSet attrs) {
    super(getFixedContext(context), attrs);
  }

  public LollipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(getFixedContext(context), attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public LollipopFixedWebView(
      Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(getFixedContext(context), attrs, defStyleAttr, defStyleRes);
  }

  public LollipopFixedWebView(
      Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
    super(getFixedContext(context), attrs, defStyleAttr, privateBrowsing);
  }

  public static Context getFixedContext(Context context) {
    return context.createConfigurationContext(new Configuration());
  }
}
