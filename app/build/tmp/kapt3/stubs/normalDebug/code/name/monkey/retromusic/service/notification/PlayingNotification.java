package code.name.monkey.retromusic.service.notification;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0003J\u000e\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\u0016\u001a\u00020\u0014J\b\u0010\u0017\u001a\u00020\u0014H&J\u0015\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0000\u00a2\u0006\u0002\b\u001bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0084.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001d"}, d2 = {"Lcode/name/monkey/retromusic/service/notification/PlayingNotification;", "", "()V", "notificationManager", "Landroid/app/NotificationManager;", "notifyMode", "", "service", "Lcode/name/monkey/retromusic/service/MusicService;", "getService", "()Lcode/name/monkey/retromusic/service/MusicService;", "setService", "(Lcode/name/monkey/retromusic/service/MusicService;)V", "stopped", "", "getStopped", "()Z", "setStopped", "(Z)V", "createNotificationChannel", "", "init", "stop", "update", "updateNotifyModeAndPostNotification", "notification", "Landroid/app/Notification;", "updateNotifyModeAndPostNotification$app_normalDebug", "Companion", "app_normalDebug"})
public abstract class PlayingNotification {
    @org.jetbrains.annotations.NotNull()
    protected code.name.monkey.retromusic.service.MusicService service;
    private boolean stopped;
    private int notifyMode;
    private android.app.NotificationManager notificationManager;
    public static final float NOTIFICATION_CONTROLS_SIZE_MULTIPLIER = 1.0F;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NOTIFICATION_CHANNEL_ID = "playing_notification";
    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 0;
    public static final code.name.monkey.retromusic.service.notification.PlayingNotification.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    protected final code.name.monkey.retromusic.service.MusicService getService() {
        return null;
    }
    
    protected final void setService(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.service.MusicService p0) {
    }
    
    protected final boolean getStopped() {
        return false;
    }
    
    protected final void setStopped(boolean p0) {
    }
    
    public final synchronized void init(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.service.MusicService service) {
    }
    
    public abstract void update();
    
    public final synchronized void stop() {
    }
    
    public final void updateNotifyModeAndPostNotification$app_normalDebug(@org.jetbrains.annotations.NotNull()
    android.app.Notification notification) {
    }
    
    @androidx.annotation.RequiresApi(value = 26)
    private final void createNotificationChannel() {
    }
    
    public PlayingNotification() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcode/name/monkey/retromusic/service/notification/PlayingNotification$Companion;", "", "()V", "NOTIFICATION_CHANNEL_ID", "", "NOTIFICATION_CONTROLS_SIZE_MULTIPLIER", "", "NOTIFICATION_ID", "", "NOTIFY_MODE_BACKGROUND", "NOTIFY_MODE_FOREGROUND", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}