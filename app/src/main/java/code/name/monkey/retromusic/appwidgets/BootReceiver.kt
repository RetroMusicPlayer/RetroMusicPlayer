/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.appwidgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import code.name.monkey.retromusic.service.MusicService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val widgetManager = AppWidgetManager.getInstance(context)

        // Start music service if there are any existing widgets
        if (widgetManager.getAppWidgetIds(
                ComponentName(
                    context, AppWidgetBig::class.java
                )
            ).isNotEmpty() || widgetManager.getAppWidgetIds(
                ComponentName(
                    context, AppWidgetClassic::class.java
                )
            ).isNotEmpty() || widgetManager.getAppWidgetIds(
                ComponentName(
                    context, AppWidgetSmall::class.java
                )
            ).isNotEmpty() || widgetManager.getAppWidgetIds(
                ComponentName(
                    context, AppWidgetCard::class.java
                )
            ).isNotEmpty()
        ) {
            val serviceIntent = Intent(context, MusicService::class.java)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // not allowed on Oreo
                context.startService(serviceIntent)
            }
        }
    }
}
