package code.name.monkey.retromusic.appwidgets;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0014J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016\u00a8\u0006\u0010"}, d2 = {"Lcode/name/monkey/retromusic/appwidgets/AppWidgetText;", "Lcode/name/monkey/retromusic/appwidgets/base/BaseAppWidget;", "()V", "defaultAppWidget", "", "context", "Landroid/content/Context;", "appWidgetIds", "", "linkButtons", "views", "Landroid/widget/RemoteViews;", "performUpdate", "service", "Lcode/name/monkey/retromusic/service/MusicService;", "Companion", "app_normalDebug"})
public final class AppWidgetText extends code.name.monkey.retromusic.appwidgets.base.BaseAppWidget {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NAME = "app_widget_text";
    private static code.name.monkey.retromusic.appwidgets.AppWidgetText mInstance;
    public static final code.name.monkey.retromusic.appwidgets.AppWidgetText.Companion Companion = null;
    
    @java.lang.Override()
    protected void defaultAppWidget(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    int[] appWidgetIds) {
    }
    
    /**
     * * Link up various button actions using [PendingIntent].
     */
    private final void linkButtons(android.content.Context context, android.widget.RemoteViews views) {
    }
    
    @java.lang.Override()
    public void performUpdate(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.service.MusicService service, @org.jetbrains.annotations.Nullable()
    int[] appWidgetIds) {
    }
    
    public AppWidgetText() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/appwidgets/AppWidgetText$Companion;", "", "()V", "NAME", "", "instance", "Lcode/name/monkey/retromusic/appwidgets/AppWidgetText;", "getInstance", "()Lcode/name/monkey/retromusic/appwidgets/AppWidgetText;", "mInstance", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final synchronized code.name.monkey.retromusic.appwidgets.AppWidgetText getInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}