package code.name.monkey.retromusic.ui.activities.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0015\n\u0002\b\b\b&\u0018\u0000 &2\u00020\u0001:\u0001&B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0006\u0010\u0011\u001a\u00020\u0006J\u0013\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\bH\u0016\u00a2\u0006\u0002\u0010\u0013J\b\u0010\u0014\u001a\u00020\u0004H\u0004J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\u0010\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0014\u001a\u00020\u0004H\u0014J\u0012\u0010\u001a\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J+\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016\u00a2\u0006\u0002\u0010 J\b\u0010!\u001a\u00020\u0016H\u0014J\b\u0010\"\u001a\u00020\u0016H\u0014J\u0010\u0010#\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\u0006H\u0004J\b\u0010%\u001a\u00020\u0016H\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\bX\u0082.\u00a2\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006\'"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsBaseActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsThemeActivity;", "()V", "hadPermissions", "", "permissionDeniedMessage", "", "permissions", "", "[Ljava/lang/String;", "snackBarContainer", "Landroid/view/View;", "getSnackBarContainer", "()Landroid/view/View;", "dispatchKeyEvent", "event", "Landroid/view/KeyEvent;", "getPermissionDeniedMessage", "getPermissionsToRequest", "()[Ljava/lang/String;", "hasPermissions", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onHasPermissionsChanged", "onPostCreate", "onRequestPermissionsResult", "requestCode", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "requestPermissions", "setPermissionDeniedMessage", "message", "showOverflowMenu", "Companion", "app_normalDebug"})
public abstract class AbsBaseActivity extends code.name.monkey.retromusic.ui.activities.base.AbsThemeActivity {
    private boolean hadPermissions;
    private java.lang.String[] permissions;
    private java.lang.String permissionDeniedMessage;
    public static final int PERMISSION_REQUEST = 100;
    public static final code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public java.lang.String[] getPermissionsToRequest() {
        return null;
    }
    
    protected final void setPermissionDeniedMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPermissionDeniedMessage() {
        return null;
    }
    
    private final android.view.View getSnackBarContainer() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onPostCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    protected void onHasPermissionsChanged(boolean hasPermissions) {
    }
    
    @java.lang.Override()
    public boolean dispatchKeyEvent(@org.jetbrains.annotations.NotNull()
    android.view.KeyEvent event) {
        return false;
    }
    
    protected final void showOverflowMenu() {
    }
    
    protected void requestPermissions() {
    }
    
    protected final boolean hasPermissions() {
        return false;
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    public AbsBaseActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsBaseActivity$Companion;", "", "()V", "PERMISSION_REQUEST", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}