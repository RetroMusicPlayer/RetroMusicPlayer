package code.name.monkey.retromusic.helper;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\u0018\u0000 \u00142\u00020\u0001:\u0002\u0013\u0014B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u0006H\u0002J\u0006\u0010\u0011\u001a\u00020\nJ\u0006\u0010\u0012\u001a\u00020\nR\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper;", "Landroid/os/Handler;", "callback", "Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Callback;", "(Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Callback;)V", "intervalPlaying", "", "intervalPaused", "(Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Callback;II)V", "handleMessage", "", "msg", "Landroid/os/Message;", "queueNextRefresh", "delay", "", "refreshProgressViews", "start", "stop", "Callback", "Companion", "app_normalDebug"})
public final class MusicProgressViewUpdateHelper extends android.os.Handler {
    private code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback callback;
    private int intervalPlaying;
    private int intervalPaused;
    private static final int CMD_REFRESH_PROGRESS_VIEWS = 1;
    private static final int MIN_INTERVAL = 20;
    private static final int UPDATE_INTERVAL_PLAYING = 1000;
    private static final int UPDATE_INTERVAL_PAUSED = 500;
    public static final code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Companion Companion = null;
    
    public final void start() {
    }
    
    public final void stop() {
    }
    
    @java.lang.Override()
    public void handleMessage(@org.jetbrains.annotations.NotNull()
    android.os.Message msg) {
    }
    
    private final int refreshProgressViews() {
        return 0;
    }
    
    private final void queueNextRefresh(long delay) {
    }
    
    public MusicProgressViewUpdateHelper(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback callback) {
        super();
    }
    
    public MusicProgressViewUpdateHelper(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback callback, int intervalPlaying, int intervalPaused) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Callback;", "", "onUpdateProgressViews", "", "progress", "", "total", "app_normalDebug"})
    public static abstract interface Callback {
        
        public abstract void onUpdateProgressViews(int progress, int total);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Companion;", "", "()V", "CMD_REFRESH_PROGRESS_VIEWS", "", "MIN_INTERVAL", "UPDATE_INTERVAL_PAUSED", "UPDATE_INTERVAL_PLAYING", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}