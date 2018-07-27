package code.name.monkey.retromusic.appwidgets;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import code.name.monkey.retromusic.service.MusicService;


/**
 * @author Eugene Cheung (arkon)
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        // Start music service if there are any existing widgets
        if (widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetBig.class)).length > 0 ||
                widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetClassic.class)).length > 0 ||
                widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetSmall.class)).length > 0 ||
                widgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetCard.class)).length > 0) {
            final Intent serviceIntent = new Intent(context, MusicService.class);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // not allowed on Oreo
                context.startService(serviceIntent);
            }
        }
    }
}
