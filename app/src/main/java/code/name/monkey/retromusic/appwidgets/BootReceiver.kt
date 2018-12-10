package code.name.monkey.retromusic.appwidgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build

import code.name.monkey.retromusic.service.MusicService


/**
 * @author Eugene Cheung (arkon)
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val widgetManager = AppWidgetManager.getInstance(context)

        // Start music service if there are any existing widgets
        if (widgetManager.getAppWidgetIds(ComponentName(context, AppWidgetBig::class.java)).isNotEmpty() ||
                widgetManager.getAppWidgetIds(ComponentName(context, AppWidgetClassic::class.java)).isNotEmpty() ||
                widgetManager.getAppWidgetIds(ComponentName(context, AppWidgetSmall::class.java)).isNotEmpty() ||
                widgetManager.getAppWidgetIds(ComponentName(context, AppWidgetCard::class.java)).isNotEmpty()) {
            val serviceIntent = Intent(context, MusicService::class.java)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // not allowed on Oreo
                context.startService(serviceIntent)
            }
        }
    }
}
