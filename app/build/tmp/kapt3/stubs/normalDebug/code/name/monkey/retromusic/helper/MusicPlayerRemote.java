package code.name.monkey.retromusic.helper;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u001c\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0003\\]^B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u00103\u001a\u000204J\u0018\u00105\u001a\u0004\u0018\u0001062\u0006\u00107\u001a\u00020\u001a2\u0006\u00108\u001a\u000209J\u0006\u0010:\u001a\u00020\u0015J\u0006\u0010;\u001a\u00020\u0015J\u000e\u0010<\u001a\u00020\u00152\u0006\u0010=\u001a\u00020\u0011J\u0014\u0010<\u001a\u00020\u00152\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00110$J\u001a\u0010?\u001a\u0004\u0018\u00010\u00042\u0006\u00107\u001a\u00020\u001a2\u0006\u0010@\u001a\u00020AH\u0002J\u000e\u0010B\u001a\u00020C2\u0006\u0010\'\u001a\u00020\tJ\u0010\u0010D\u001a\u00020\u00042\u0006\u0010@\u001a\u00020AH\u0003J\u0016\u0010E\u001a\u00020\u00152\u0006\u0010F\u001a\u00020\t2\u0006\u0010G\u001a\u00020\tJ\u001c\u0010H\u001a\u0002042\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00110$2\u0006\u0010J\u001a\u00020\u0015J$\u0010K\u001a\u0002042\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00110$2\u0006\u0010L\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u0015J\u0006\u0010M\u001a\u000204J\u000e\u0010N\u001a\u0002042\u0006\u0010@\u001a\u00020AJ\u000e\u0010O\u001a\u00020\u00152\u0006\u0010=\u001a\u00020\u0011J\u0014\u0010O\u001a\u00020\u00152\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00110$J\u0006\u0010P\u001a\u000204J\u0006\u0010Q\u001a\u000204J\u000e\u0010R\u001a\u0002042\u0006\u0010\'\u001a\u00020\tJ\u000e\u0010S\u001a\u00020\u00152\u0006\u0010=\u001a\u00020\u0011J\u000e\u0010S\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020\tJ\u0006\u0010T\u001a\u000204J\u000e\u0010U\u001a\u00020\t2\u0006\u0010V\u001a\u00020\tJ\u000e\u0010W\u001a\u00020\u00152\u0006\u0010-\u001a\u00020\tJ\u0006\u0010X\u001a\u00020\u0015J&\u0010Y\u001a\u00020\u00152\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00110$2\u0006\u0010L\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u0015H\u0002J\u0010\u0010Z\u001a\u0002042\b\u0010[\u001a\u0004\u0018\u000106R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\u0004\u0018\u00010\r8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\u00158F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u00158F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0016R\u001a\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001b0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00110$8F\u00a2\u0006\u0006\u001a\u0004\b%\u0010&R$\u0010\'\u001a\u00020\t2\u0006\u0010\'\u001a\u00020\t8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b(\u0010\u000b\"\u0004\b)\u0010*R\u0011\u0010+\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b,\u0010\u000bR\u0011\u0010-\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b.\u0010\u000bR\u0011\u0010/\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b0\u0010\u000bR\u0011\u00101\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b2\u0010\u000b\u00a8\u0006_"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicPlayerRemote;", "", "()V", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "audioSessionId", "", "getAudioSessionId", "()I", "castSession", "Lcom/google/android/gms/cast/framework/CastSession;", "getCastSession", "()Lcom/google/android/gms/cast/framework/CastSession;", "currentSong", "Lcode/name/monkey/retromusic/model/Song;", "getCurrentSong", "()Lcode/name/monkey/retromusic/model/Song;", "isPlaying", "", "()Z", "isServiceConnected", "mConnectionMap", "Ljava/util/WeakHashMap;", "Landroid/content/Context;", "Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$ServiceBinder;", "musicService", "Lcode/name/monkey/retromusic/service/MusicService;", "getMusicService", "()Lcode/name/monkey/retromusic/service/MusicService;", "setMusicService", "(Lcode/name/monkey/retromusic/service/MusicService;)V", "playbackLocation", "playingQueue", "Ljava/util/ArrayList;", "getPlayingQueue", "()Ljava/util/ArrayList;", "position", "getPosition", "setPosition", "(I)V", "repeatMode", "getRepeatMode", "shuffleMode", "getShuffleMode", "songDurationMillis", "getSongDurationMillis", "songProgressMillis", "getSongProgressMillis", "back", "", "bindToService", "Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$ServiceToken;", "context", "callback", "Landroid/content/ServiceConnection;", "clearQueue", "cycleRepeatMode", "enqueue", "song", "songs", "getFilePathFromUri", "uri", "Landroid/net/Uri;", "getQueueDurationMillis", "", "getSongIdFromMediaProvider", "moveSong", "from", "to", "openAndShuffleQueue", "queue", "startPlaying", "openQueue", "startPosition", "pauseSong", "playFromUri", "playNext", "playNextSong", "playPreviousSong", "playSongAt", "removeFromQueue", "resumePlaying", "seekTo", "millis", "setShuffleMode", "toggleShuffleMode", "tryToHandleOpenPlayingQueue", "unbindFromService", "token", "PlaybackLocation", "ServiceBinder", "ServiceToken", "app_normalDebug"})
public final class MusicPlayerRemote {
    private static final java.lang.String TAG = null;
    private static final java.util.WeakHashMap<android.content.Context, code.name.monkey.retromusic.helper.MusicPlayerRemote.ServiceBinder> mConnectionMap = null;
    @org.jetbrains.annotations.Nullable()
    private static code.name.monkey.retromusic.service.MusicService musicService;
    private static int playbackLocation;
    public static final code.name.monkey.retromusic.helper.MusicPlayerRemote INSTANCE = null;
    
    public final java.lang.String getTAG() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final code.name.monkey.retromusic.service.MusicService getMusicService() {
        return null;
    }
    
    public final void setMusicService(@org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.service.MusicService p0) {
    }
    
    private final com.google.android.gms.cast.framework.CastSession getCastSession() {
        return null;
    }
    
    public final boolean isPlaying() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final code.name.monkey.retromusic.model.Song getCurrentSong() {
        return null;
    }
    
    public final int getPosition() {
        return 0;
    }
    
    public final void setPosition(int position) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getPlayingQueue() {
        return null;
    }
    
    public final int getSongProgressMillis() {
        return 0;
    }
    
    public final int getSongDurationMillis() {
        return 0;
    }
    
    public final int getRepeatMode() {
        return 0;
    }
    
    public final int getShuffleMode() {
        return 0;
    }
    
    public final int getAudioSessionId() {
        return 0;
    }
    
    public final boolean isServiceConnected() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final code.name.monkey.retromusic.helper.MusicPlayerRemote.ServiceToken bindToService(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.ServiceConnection callback) {
        return null;
    }
    
    public final void unbindFromService(@org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.helper.MusicPlayerRemote.ServiceToken token) {
    }
    
    private final java.lang.String getFilePathFromUri(android.content.Context context, android.net.Uri uri) {
        return null;
    }
    
    /**
     * * Async
     */
    public final void playSongAt(int position) {
    }
    
    public final void pauseSong() {
    }
    
    /**
     * * Async
     */
    public final void playNextSong() {
    }
    
    /**
     * * Async
     */
    public final void playPreviousSong() {
    }
    
    /**
     * * Async
     */
    public final void back() {
    }
    
    public final void resumePlaying() {
    }
    
    /**
     * * Async
     */
    public final void openQueue(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> queue, int startPosition, boolean startPlaying) {
    }
    
    /**
     * * Async
     */
    public final void openAndShuffleQueue(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> queue, boolean startPlaying) {
    }
    
    private final boolean tryToHandleOpenPlayingQueue(java.util.ArrayList<code.name.monkey.retromusic.model.Song> queue, int startPosition, boolean startPlaying) {
        return false;
    }
    
    public final long getQueueDurationMillis(int position) {
        return 0L;
    }
    
    public final int seekTo(int millis) {
        return 0;
    }
    
    public final boolean cycleRepeatMode() {
        return false;
    }
    
    public final boolean toggleShuffleMode() {
        return false;
    }
    
    public final boolean setShuffleMode(int shuffleMode) {
        return false;
    }
    
    public final boolean playNext(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song) {
        return false;
    }
    
    public final boolean playNext(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs) {
        return false;
    }
    
    public final boolean enqueue(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song) {
        return false;
    }
    
    public final boolean enqueue(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs) {
        return false;
    }
    
    public final boolean removeFromQueue(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song) {
        return false;
    }
    
    public final boolean removeFromQueue(int position) {
        return false;
    }
    
    public final boolean moveSong(int from, int to) {
        return false;
    }
    
    public final boolean clearQueue() {
        return false;
    }
    
    public final void playFromUri(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    @android.annotation.TargetApi(value = android.os.Build.VERSION_CODES.KITKAT)
    private final java.lang.String getSongIdFromMediaProvider(android.net.Uri uri) {
        return null;
    }
    
    private MusicPlayerRemote() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0002\b\u0002\b\u0080\u0002\u0018\u0000 \u00022\u00020\u0001:\u0001\u0002B\u0000\u00a8\u0006\u0003"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$PlaybackLocation;", "", "Companion", "app_normalDebug"})
    @java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
    public static abstract @interface PlaybackLocation {
        public static final code.name.monkey.retromusic.helper.MusicPlayerRemote.PlaybackLocation.Companion Companion = null;
        
        @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006\u00a8\u0006\t"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$PlaybackLocation$Companion;", "", "()V", "LOCAL", "", "getLOCAL", "()I", "REMOTE", "getREMOTE", "app_normalDebug"})
        public static final class Companion {
            private static final int REMOTE = 0;
            private static final int LOCAL = 1;
            
            public final int getREMOTE() {
                return 0;
            }
            
            public final int getLOCAL() {
                return 0;
            }
            
            private Companion() {
                super();
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0000\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$ServiceBinder;", "Landroid/content/ServiceConnection;", "mCallback", "(Landroid/content/ServiceConnection;)V", "onServiceConnected", "", "className", "Landroid/content/ComponentName;", "service", "Landroid/os/IBinder;", "onServiceDisconnected", "app_normalDebug"})
    public static final class ServiceBinder implements android.content.ServiceConnection {
        private final android.content.ServiceConnection mCallback = null;
        
        @java.lang.Override()
        public void onServiceConnected(@org.jetbrains.annotations.NotNull()
        android.content.ComponentName className, @org.jetbrains.annotations.NotNull()
        android.os.IBinder service) {
        }
        
        @java.lang.Override()
        public void onServiceDisconnected(@org.jetbrains.annotations.NotNull()
        android.content.ComponentName className) {
        }
        
        public ServiceBinder(@org.jetbrains.annotations.Nullable()
        android.content.ServiceConnection mCallback) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$ServiceToken;", "", "mWrappedContext", "Landroid/content/ContextWrapper;", "(Landroid/content/ContextWrapper;)V", "getMWrappedContext$app_normalDebug", "()Landroid/content/ContextWrapper;", "setMWrappedContext$app_normalDebug", "app_normalDebug"})
    public static final class ServiceToken {
        @org.jetbrains.annotations.NotNull()
        private android.content.ContextWrapper mWrappedContext;
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.ContextWrapper getMWrappedContext$app_normalDebug() {
            return null;
        }
        
        public final void setMWrappedContext$app_normalDebug(@org.jetbrains.annotations.NotNull()
        android.content.ContextWrapper p0) {
        }
        
        public ServiceToken(@org.jetbrains.annotations.NotNull()
        android.content.ContextWrapper mWrappedContext) {
            super();
        }
    }
}