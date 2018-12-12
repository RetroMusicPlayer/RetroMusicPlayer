package code.name.monkey.retromusic.ui.activities.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b&\u0018\u0000  2\u00020\u0001:\u0001 B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0017H\u0002J\u0012\u0010\u0019\u001a\u00020\u00172\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0017H\u0014J\b\u0010\u001d\u001a\u00020\u0017H\u0014J\b\u0010\u001e\u001a\u00020\u0017H\u0002J\b\u0010\u001f\u001a\u00020\u0017H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R(\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b@BX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsCastActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsCheckPiracy;", "()V", "castContext", "Lcom/google/android/gms/cast/framework/CastContext;", "castServer", "Lcode/name/monkey/retromusic/cast/WebServer;", "<set-?>", "Lcom/google/android/gms/cast/framework/CastSession;", "castSession", "getCastSession", "()Lcom/google/android/gms/cast/framework/CastSession;", "setCastSession", "(Lcom/google/android/gms/cast/framework/CastSession;)V", "playServicesAvailable", "", "getPlayServicesAvailable", "()Z", "setPlayServicesAvailable", "(Z)V", "sessionManagerListener", "Lcom/google/android/gms/cast/framework/SessionManagerListener;", "hideCastMiniController", "", "initCast", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "onResume", "setupCastListener", "showCastMiniController", "Companion", "app_normalDebug"})
public abstract class AbsCastActivity extends code.name.monkey.retromusic.ui.activities.base.AbsCheckPiracy {
    private boolean playServicesAvailable;
    private com.google.android.gms.cast.framework.CastContext castContext;
    private com.google.android.gms.cast.framework.SessionManagerListener<com.google.android.gms.cast.framework.CastSession> sessionManagerListener;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.cast.framework.CastSession castSession;
    private code.name.monkey.retromusic.cast.WebServer castServer;
    private static final java.lang.String TAG = "AbsCastActivity";
    public static final code.name.monkey.retromusic.ui.activities.base.AbsCastActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    public final boolean getPlayServicesAvailable() {
        return false;
    }
    
    public final void setPlayServicesAvailable(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.android.gms.cast.framework.CastSession getCastSession() {
        return null;
    }
    
    private final void setCastSession(com.google.android.gms.cast.framework.CastSession p0) {
    }
    
    private final void setupCastListener() {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    private final void initCast() {
    }
    
    public void showCastMiniController() {
    }
    
    public void hideCastMiniController() {
    }
    
    public AbsCastActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsCastActivity$Companion;", "", "()V", "TAG", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}