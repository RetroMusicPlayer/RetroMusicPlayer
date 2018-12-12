package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 42\u00020\u00012\u00020\u0002:\u00014B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0014J\b\u0010\u0010\u001a\u00020\u0005H\u0016J\u0012\u0010\u0011\u001a\u00020\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0002J\"\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\b\u0010\u0018\u001a\u0004\u0018\u00010\u0013H\u0014J\u0012\u0010\u0019\u001a\u00020\r2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\rH\u0016J\u0010\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\rH\u0014J\b\u0010!\u001a\u00020\rH\u0016J\u0018\u0010\"\u001a\u00020\r2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0016J \u0010\'\u001a\u00020(2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010)\u001a\u00020&2\u0006\u0010*\u001a\u00020&H\u0002J\b\u0010+\u001a\u00020\rH\u0014J\b\u0010,\u001a\u00020\rH\u0002J\u0010\u0010-\u001a\u00020\r2\u0006\u0010.\u001a\u00020\u0016H\u0002J\u0016\u0010/\u001a\u00020\r2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u0005J\b\u00103\u001a\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/MainActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity;", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "()V", "blockRequestPermissions", "", "broadcastReceiver", "Landroid/content/BroadcastReceiver;", "currentFragment", "Lcode/name/monkey/retromusic/interfaces/MainActivityFragmentCallbacks;", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "checkShowChangelog", "", "createContentView", "Landroid/view/View;", "handleBackPress", "handlePlaybackIntent", "intent", "Landroid/content/Intent;", "onActivityResult", "requestCode", "", "resultCode", "data", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onResume", "onServiceConnected", "onSharedPreferenceChanged", "sharedPreferences", "Landroid/content/SharedPreferences;", "key", "", "parseIdFromIntent", "", "longKey", "stringKey", "requestPermissions", "restoreCurrentFragment", "selectedFragment", "itemId", "setCurrentFragment", "fragment", "Landroidx/fragment/app/Fragment;", "b", "showPromotionalOffer", "Companion", "app_normalDebug"})
public final class MainActivity extends code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {
    private code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks currentFragment;
    private boolean blockRequestPermissions;
    private final io.reactivex.disposables.CompositeDisposable disposable = null;
    private final android.content.BroadcastReceiver broadcastReceiver = null;
    public static final int APP_INTRO_REQUEST = 2323;
    public static final int LIBRARY = 1;
    public static final int FOLDERS = 3;
    public static final int HOME = 0;
    private static final java.lang.String TAG = "MainActivity";
    private static final int APP_USER_INFO_REQUEST = 9003;
    private static final int REQUEST_CODE_THEME = 9002;
    private static final int PURCHASE_REQUEST = 101;
    public static final code.name.monkey.retromusic.ui.activities.MainActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected android.view.View createContentView() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void checkShowChangelog() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public final void setCurrentFragment(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment, boolean b) {
    }
    
    private final void restoreCurrentFragment() {
    }
    
    private final void handlePlaybackIntent(android.content.Intent intent) {
    }
    
    private final long parseIdFromIntent(android.content.Intent intent, java.lang.String longKey, java.lang.String stringKey) {
        return 0L;
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    @java.lang.Override()
    public boolean handleBackPress() {
        return false;
    }
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    @java.lang.Override()
    protected void requestPermissions() {
    }
    
    @java.lang.Override()
    public void onSharedPreferenceChanged(@org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences sharedPreferences, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    private final void showPromotionalOffer() {
    }
    
    private final void selectedFragment(int itemId) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    public MainActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/MainActivity$Companion;", "", "()V", "APP_INTRO_REQUEST", "", "APP_USER_INFO_REQUEST", "FOLDERS", "HOME", "LIBRARY", "PURCHASE_REQUEST", "REQUEST_CODE_THEME", "TAG", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}