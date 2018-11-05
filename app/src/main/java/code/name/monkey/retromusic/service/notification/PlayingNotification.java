package code.name.monkey.retromusic.service.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.service.MusicService;

import static android.content.Context.NOTIFICATION_SERVICE;


public abstract class PlayingNotification {

    protected static final float NOTIFICATION_CONTROLS_SIZE_MULTIPLIER = 1.0f;
    static final String NOTIFICATION_CHANNEL_ID = "playing_notification";
    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 0;
    protected MusicService service;
    boolean stopped;
    private int notifyMode = NOTIFY_MODE_BACKGROUND;
    private NotificationManager notificationManager;

    public synchronized void init(MusicService service) {
        this.service = service;
        notificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    public abstract void update();

    public synchronized void stop() {
        stopped = true;
        service.stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    void updateNotifyModeAndPostNotification(Notification notification) {
        int newNotifyMode;
        if (service.isPlaying()) {
            newNotifyMode = NOTIFY_MODE_FOREGROUND;
        } else {
            newNotifyMode = NOTIFY_MODE_BACKGROUND;
        }

        if (notifyMode != newNotifyMode && newNotifyMode == NOTIFY_MODE_BACKGROUND) {
            service.stopForeground(false);
        }

        if (newNotifyMode == NOTIFY_MODE_FOREGROUND) {
            service.startForeground(NOTIFICATION_ID, notification);
        } else if (newNotifyMode == NOTIFY_MODE_BACKGROUND) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        notifyMode = newNotifyMode;
    }

    @RequiresApi(26)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = notificationManager
                .getNotificationChannel(NOTIFICATION_CHANNEL_ID);
        if (notificationChannel == null) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    service.getString(R.string.playing_notification_name),
                    NotificationManager.IMPORTANCE_LOW);
            notificationChannel
                    .setDescription(service.getString(R.string.playing_notification_description));
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}