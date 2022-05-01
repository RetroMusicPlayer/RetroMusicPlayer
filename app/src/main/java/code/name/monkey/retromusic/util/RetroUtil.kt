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
package code.name.monkey.retromusic.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.Point
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.App.Companion.getContext
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.util.*

object RetroUtil {
    fun formatValue(value: Float): String {
        var value = value
        val arr = arrayOf("", "K", "M", "B", "T", "P", "E")
        var index = 0
        while (value / 1000 >= 1) {
            value /= 1000
            index++
        }
        val decimalFormat = DecimalFormat("#.##")
        return String.format("%s %s", decimalFormat.format(value.toDouble()), arr[index])
    }

    fun frequencyCount(frequency: Int): Float {
        return (frequency / 1000.0).toFloat()
    }

    fun getScreenSize(context: Context): Point {
        val x: Int = context.resources.displayMetrics.widthPixels
        val y: Int = context.resources.displayMetrics.heightPixels
        return Point(x, y)
    }

    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = getContext()
                .resources
                .getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = getContext().resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    val navigationBarHeight: Int
        get() {
            var result = 0
            val resourceId = getContext()
                .resources
                .getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = getContext().resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    fun getTintedVectorDrawable(
        context: Context,
        @DrawableRes id: Int,
        @ColorInt color: Int,
    ): Drawable {
        return TintHelper.createTintedDrawable(
            getVectorDrawable(context.resources, id, context.theme), color)
    }

    fun getTintedVectorDrawable(
        res: Resources,
        @DrawableRes resId: Int,
        theme: Theme?,
        @ColorInt color: Int,
    ): Drawable {
        return TintHelper.createTintedDrawable(getVectorDrawable(res, resId, theme), color)
    }

    private fun getVectorDrawable(
        res: Resources, @DrawableRes resId: Int, theme: Theme?,
    ): Drawable? {
        return ResourcesCompat.getDrawable(res, resId, theme)
    }

    val isLandscape: Boolean
        get() = (getContext().resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)
    val isTablet: Boolean
        get() = (getContext().resources.configuration.smallestScreenWidthDp
                >= 600)

    fun getIpAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress

                        if (sAddr != null) {
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4) return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) {
                                        sAddr.uppercase()
                                    } else {
                                        sAddr.substring(
                                            0,
                                            delim
                                        ).uppercase()
                                    }
                                }
                            }
                        } else {
                            return null
                        }

                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }
}