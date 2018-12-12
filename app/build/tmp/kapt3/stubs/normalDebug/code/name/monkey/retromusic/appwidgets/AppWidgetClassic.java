package code.name.monkey.retromusic.appwidgets;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0014J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u001a\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016R\u0016\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcode/name/monkey/retromusic/appwidgets/AppWidgetClassic;", "Lcode/name/monkey/retromusic/appwidgets/base/BaseAppWidget;", "()V", "target", "Lcom/bumptech/glide/request/target/Target;", "Lcode/name/monkey/retromusic/glide/palette/BitmapPaletteWrapper;", "defaultAppWidget", "", "context", "Landroid/content/Context;", "appWidgetIds", "", "linkButtons", "views", "Landroid/widget/RemoteViews;", "performUpdate", "service", "Lcode/name/monkey/retromusic/service/MusicService;", "Companion", "app_normalDebug"})
public final class AppWidgetClassic extends code.name.monkey.retromusic.appwidgets.base.BaseAppWidget {
    private com.bumptech.glide.request.target.Target<code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper> target;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NAME = "app_widget_classic";
    private static code.name.monkey.retromusic.appwidgets.AppWidgetClassic mInstance;
    private static int imageSize;
    private static float cardRadius;
    public static final code.name.monkey.retromusic.appwidgets.AppWidgetClassic.Companion Companion = null;
    
    /**
     * * Initialize given widgets to default state, where we launch Music on default click and hide
     *     * actions if service not running.
     */
    @java.lang.Override()
    protected void defaultAppWidget(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    int[] appWidgetIds) {
    }
    
    /**
     * * Update all active widget instances by pushing changes
     */
    @java.lang.Override()
    public void performUpdate(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.service.MusicService service, @org.jetbrains.annotations.Nullable()
    int[] appWidgetIds) {
    }
    
    /**
     * * Link up various button actions using [PendingIntent].
     */
    private final void linkButtons(android.content.Context context, android.widget.RemoteViews views) {
    }
    
    public AppWidgetClassic() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\r\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcode/name/monkey/retromusic/appwidgets/AppWidgetClassic$Companion;", "", "()V", "NAME", "", "cardRadius", "", "imageSize", "", "instance", "Lcode/name/monkey/retromusic/appwidgets/AppWidgetClassic;", "getInstance", "()Lcode/name/monkey/retromusic/appwidgets/AppWidgetClassic;", "mInstance", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final synchronized code.name.monkey.retromusic.appwidgets.AppWidgetClassic getInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}