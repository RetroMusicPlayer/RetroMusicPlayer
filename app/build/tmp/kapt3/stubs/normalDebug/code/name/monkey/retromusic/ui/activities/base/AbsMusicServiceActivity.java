package code.name.monkey.retromusic.ui.activities.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\b&\u0018\u0000 \"2\u00020\u00012\u00020\u0002:\u0002\"#B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002J\u0013\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0016\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0013\u001a\u00020\r2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\b\u0010\u0016\u001a\u00020\rH\u0016J\u0010\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\tH\u0014J\b\u0010\u0019\u001a\u00020\rH\u0016J\b\u0010\u001a\u001a\u00020\rH\u0016J\b\u0010\u001b\u001a\u00020\rH\u0016J\b\u0010\u001c\u001a\u00020\rH\u0016J\b\u0010\u001d\u001a\u00020\rH\u0016J\b\u0010\u001e\u001a\u00020\rH\u0016J\b\u0010\u001f\u001a\u00020\rH\u0016J\b\u0010 \u001a\u00020\rH\u0016J\u0010\u0010!\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsCastActivity;", "Lcode/name/monkey/retromusic/interfaces/MusicServiceEventListener;", "()V", "mMusicServiceEventListeners", "Ljava/util/ArrayList;", "musicStateReceiver", "Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity$MusicStateReceiver;", "receiverRegistered", "", "serviceToken", "Lcode/name/monkey/retromusic/helper/MusicPlayerRemote$ServiceToken;", "addMusicServiceEventListener", "", "listener", "getPermissionsToRequest", "", "", "()[Ljava/lang/String;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onHasPermissionsChanged", "hasPermissions", "onMediaStoreChanged", "onPlayStateChanged", "onPlayingMetaChanged", "onQueueChanged", "onRepeatModeChanged", "onServiceConnected", "onServiceDisconnected", "onShuffleModeChanged", "removeMusicServiceEventListener", "Companion", "MusicStateReceiver", "app_normalDebug"})
public abstract class AbsMusicServiceActivity extends code.name.monkey.retromusic.ui.activities.base.AbsCastActivity implements code.name.monkey.retromusic.interfaces.MusicServiceEventListener {
    private final java.util.ArrayList<code.name.monkey.retromusic.interfaces.MusicServiceEventListener> mMusicServiceEventListeners = null;
    private code.name.monkey.retromusic.helper.MusicPlayerRemote.ServiceToken serviceToken;
    private code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity.MusicStateReceiver musicStateReceiver;
    private boolean receiverRegistered;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public final void addMusicServiceEventListener(@org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.MusicServiceEventListener listener) {
    }
    
    public final void removeMusicServiceEventListener(@org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.MusicServiceEventListener listener) {
    }
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onServiceDisconnected() {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    @java.lang.Override()
    public void onQueueChanged() {
    }
    
    @java.lang.Override()
    public void onPlayStateChanged() {
    }
    
    @java.lang.Override()
    public void onMediaStoreChanged() {
    }
    
    @java.lang.Override()
    public void onRepeatModeChanged() {
    }
    
    @java.lang.Override()
    public void onShuffleModeChanged() {
    }
    
    @java.lang.Override()
    protected void onHasPermissionsChanged(boolean hasPermissions) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String[] getPermissionsToRequest() {
        return null;
    }
    
    public AbsMusicServiceActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity$MusicStateReceiver;", "Landroid/content/BroadcastReceiver;", "activity", "Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity;", "(Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity;)V", "reference", "Ljava/lang/ref/WeakReference;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_normalDebug"})
    static final class MusicStateReceiver extends android.content.BroadcastReceiver {
        private final java.lang.ref.WeakReference<code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity> reference = null;
        
        @java.lang.Override()
        public void onReceive(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        android.content.Intent intent) {
        }
        
        public MusicStateReceiver(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity activity) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTAG() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}