package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 #2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u001a\u0010\r\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0016J\u0012\u0010\u0010\u001a\u00020\t2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\tH\u0016J\b\u0010\u0018\u001a\u00020\tH\u0016J\u0018\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0018\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\b\b\u0001\u0010!\u001a\u00020\u000fJ\b\u0010\"\u001a\u00020\tH\u0002R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SettingsActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsBaseActivity;", "Lcom/afollestad/materialdialogs/color/ColorChooserDialog$ColorCallback;", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "()V", "fragmentManager", "Landroidx/fragment/app/FragmentManager;", "kotlin.jvm.PlatformType", "onBackPressed", "", "onColorChooserDismissed", "dialog", "Lcom/afollestad/materialdialogs/color/ColorChooserDialog;", "onColorSelection", "selectedColor", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onPause", "onResume", "onSharedPreferenceChanged", "sharedPreferences", "Landroid/content/SharedPreferences;", "key", "", "setupFragment", "fragment", "Landroidx/fragment/app/Fragment;", "titleName", "setupToolbar", "Companion", "app_normalDebug"})
public final class SettingsActivity extends code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity implements com.afollestad.materialdialogs.color.ColorChooserDialog.ColorCallback, android.content.SharedPreferences.OnSharedPreferenceChangeListener {
    private final androidx.fragment.app.FragmentManager fragmentManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG = "SettingsActivity";
    public static final code.name.monkey.retromusic.ui.activities.SettingsActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onColorSelection(@org.jetbrains.annotations.NotNull()
    com.afollestad.materialdialogs.color.ColorChooserDialog dialog, @androidx.annotation.ColorInt()
    int selectedColor) {
    }
    
    @java.lang.Override()
    public void onColorChooserDismissed(@org.jetbrains.annotations.NotNull()
    com.afollestad.materialdialogs.color.ColorChooserDialog dialog) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupToolbar() {
    }
    
    public final void setupFragment(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment, @androidx.annotation.StringRes()
    int titleName) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onSharedPreferenceChanged(@org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences sharedPreferences, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public SettingsActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SettingsActivity$Companion;", "", "()V", "TAG", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}