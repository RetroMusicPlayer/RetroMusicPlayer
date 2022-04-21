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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.App;

public class RetroUtil {

  public static String formatValue(float value) {
    String[] arr = {"", "K", "M", "B", "T", "P", "E"};
    int index = 0;
    while ((value / 1000) >= 1) {
      value = value / 1000;
      index++;
    }
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    return String.format("%s %s", decimalFormat.format(value), arr[index]);
  }

  public static float frequencyCount(int frequency) {
    return (float) (frequency / 1000.0);
  }

  public static Point getScreenSize(@NonNull Context c) {
    Display display = null;
    if (c.getSystemService(Context.WINDOW_SERVICE) != null) {
      display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }
    Point size = new Point();
    if (display != null) {
      display.getSize(size);
    }
    return size;
  }

  public static int getStatusBarHeight() {
    int result = 0;
    int resourceId =
            App.Companion.getContext()
                    .getResources()
                    .getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = App.Companion.getContext().getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public static int getNavigationBarHeight() {
    int result = 0;
    int resourceId =
            App.Companion.getContext()
                    .getResources()
                    .getIdentifier("navigation_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = App.Companion.getContext().getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  @NonNull
  public static Drawable getTintedVectorDrawable(
          @NonNull Context context, @DrawableRes int id, @ColorInt int color) {
    return TintHelper.createTintedDrawable(
            getVectorDrawable(context.getResources(), id, context.getTheme()), color);
  }

  @NonNull
  public static Drawable getTintedVectorDrawable(
          @NonNull Resources res,
          @DrawableRes int resId,
          @Nullable Resources.Theme theme,
          @ColorInt int color) {
    return TintHelper.createTintedDrawable(getVectorDrawable(res, resId, theme), color);
  }

  @Nullable
  public static Drawable getVectorDrawable(
          @NonNull Resources res, @DrawableRes int resId, @Nullable Resources.Theme theme) {
    return ResourcesCompat.getDrawable(res, resId, theme);
  }

  public static boolean isLandscape() {
    return App.Companion.getContext().getResources().getConfiguration().orientation
            == Configuration.ORIENTATION_LANDSCAPE;
  }

  public static boolean isTablet() {
    return App.Companion.getContext().getResources().getConfiguration().smallestScreenWidthDp
            >= 600;
  }

  public static String getIpAddress(boolean useIPv4) {
    try {
      List<NetworkInterface> interfaces =
              Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress();
            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
            boolean isIPv4 = sAddr.indexOf(':') < 0;
            if (useIPv4) {
              if (isIPv4) return sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                if (delim < 0) {
                  return sAddr.toUpperCase();
                } else {
                  return sAddr.substring(
                          0,
                          delim
                  ).toUpperCase();
                }
              }
            }
          }
        }

      }
    } catch (Exception ignored) {
    }
    return "";
  }
}
