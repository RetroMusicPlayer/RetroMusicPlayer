package code.name.monkey.retromusic.ui.activities.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0018\b&\u0018\u0000 H2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001HB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0002J\b\u0010\u001e\u001a\u00020\u001fH$J\b\u0010 \u001a\u00020\u001cH\u0002J\u0006\u0010!\u001a\u00020\"J\b\u0010#\u001a\u00020\nH\u0016J\u000e\u0010$\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\nJ\b\u0010&\u001a\u00020\u001cH\u0016J\u0012\u0010\'\u001a\u00020\u001c2\b\u0010(\u001a\u0004\u0018\u00010)H\u0014J\b\u0010*\u001a\u00020\u001cH\u0016J\b\u0010+\u001a\u00020\u001cH\u0016J\b\u0010,\u001a\u00020\u001cH\u0016J\b\u0010-\u001a\u00020\u001cH\u0016J\u001a\u0010.\u001a\u00020\u001c2\b\u0010/\u001a\u0004\u0018\u00010\u001f2\u0006\u00100\u001a\u000201H\u0016J \u00102\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u00152\u0006\u00104\u001a\u00020\u0015H\u0016J\b\u00105\u001a\u00020\u001cH\u0016J\b\u00106\u001a\u00020\u001cH\u0016J\b\u00107\u001a\u00020\u001cH\u0014J\b\u00108\u001a\u00020\u001cH\u0016J\u000e\u00109\u001a\u00020\u001c2\u0006\u0010:\u001a\u00020\u0013J\u0010\u0010;\u001a\u00020\u001c2\u0006\u0010<\u001a\u00020\nH\u0016J\u0010\u0010=\u001a\u00020\u001c2\u0006\u0010<\u001a\u00020\nH\u0016J\u0012\u0010>\u001a\u00020\u001c2\b\b\u0001\u0010?\u001a\u000201H\u0002J\u0010\u0010@\u001a\u00020\u001c2\u0006\u0010A\u001a\u00020\u0013H\u0016J\u0010\u0010B\u001a\u00020\u001c2\u0006\u0010A\u001a\u00020\u0013H\u0016J\b\u0010C\u001a\u00020\u001cH\u0002J\u000e\u0010D\u001a\u00020\u001c2\u0006\u0010E\u001a\u00020\nJ\u0012\u0010F\u001a\u00020\u001f2\b\b\u0001\u0010G\u001a\u00020\u0013H\u0004R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u000e\u0010\f\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u00158F\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity;", "Lcom/sothree/slidinguppanel/SlidingUpPanelLayout$PanelSlideListener;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment$Callbacks;", "()V", "argbEvaluator", "Landroid/animation/ArgbEvaluator;", "currentNowPlayingScreen", "Lcode/name/monkey/retromusic/ui/fragments/NowPlayingScreen;", "isOneOfTheseThemes", "", "()Z", "lightNavigationBar", "lightStatusbar", "miniPlayerFragment", "Lcode/name/monkey/retromusic/ui/fragments/MiniPlayerFragment;", "navigationBarColorAnimator", "Landroid/animation/ValueAnimator;", "navigationbarColor", "", "panelState", "Lcom/sothree/slidinguppanel/SlidingUpPanelLayout$PanelState;", "getPanelState", "()Lcom/sothree/slidinguppanel/SlidingUpPanelLayout$PanelState;", "playerFragment", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment;", "taskColor", "chooseFragmentForTheme", "", "collapsePanel", "createContentView", "Landroid/view/View;", "expandPanel", "getBottomNavigationView", "Lcode/name/monkey/retromusic/views/BottomNavigationBarTinted;", "handleBackPress", "hideBottomBar", "hide", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPaletteColorChanged", "onPanelCollapsed", "onPanelExpanded", "onPanelSlide", "panel", "slideOffset", "", "onPanelStateChanged", "previousState", "newState", "onPlayingMetaChanged", "onQueueChanged", "onResume", "onServiceConnected", "setBottomBarVisibility", "gone", "setLightNavigationBar", "enabled", "setLightStatusbar", "setMiniPlayerAlphaProgress", "progress", "setNavigationbarColor", "color", "setTaskDescriptionColor", "setupSlidingUpPanel", "toggleBottomNavigationView", "toggle", "wrapSlidingMusicPanel", "resId", "Companion", "app_normalDebug"})
public abstract class AbsSlidingMusicPanelActivity extends code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity implements com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener, code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment.Callbacks {
    private code.name.monkey.retromusic.ui.fragments.MiniPlayerFragment miniPlayerFragment;
    private code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment playerFragment;
    private code.name.monkey.retromusic.ui.fragments.NowPlayingScreen currentNowPlayingScreen;
    private int navigationbarColor;
    private int taskColor;
    private boolean lightStatusbar;
    private boolean lightNavigationBar;
    private android.animation.ValueAnimator navigationBarColorAnimator;
    private final android.animation.ArgbEvaluator argbEvaluator = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    public final com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState getPanelState() {
        return null;
    }
    
    private final boolean isOneOfTheseThemes() {
        return false;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void setBottomBarVisibility(int gone) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected abstract android.view.View createContentView();
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onQueueChanged() {
    }
    
    public final void hideBottomBar(boolean hide) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final android.view.View wrapSlidingMusicPanel(@androidx.annotation.LayoutRes()
    int resId) {
        return null;
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    public boolean handleBackPress() {
        return false;
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    public final void toggleBottomNavigationView(boolean toggle) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final code.name.monkey.retromusic.views.BottomNavigationBarTinted getBottomNavigationView() {
        return null;
    }
    
    private final void setupSlidingUpPanel() {
    }
    
    @java.lang.Override()
    public void onPanelSlide(@org.jetbrains.annotations.Nullable()
    android.view.View panel, float slideOffset) {
    }
    
    @java.lang.Override()
    public void onPanelStateChanged(@org.jetbrains.annotations.NotNull()
    android.view.View panel, @org.jetbrains.annotations.NotNull()
    com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState previousState, @org.jetbrains.annotations.NotNull()
    com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState newState) {
    }
    
    public void onPanelCollapsed() {
    }
    
    public void onPanelExpanded() {
    }
    
    private final void setMiniPlayerAlphaProgress(@androidx.annotation.FloatRange(from = 0.0, to = 1.0)
    float progress) {
    }
    
    private final void chooseFragmentForTheme() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void collapsePanel() {
    }
    
    private final void expandPanel() {
    }
    
    @java.lang.Override()
    public void onPaletteColorChanged() {
    }
    
    @java.lang.Override()
    public void setLightStatusbar(boolean enabled) {
    }
    
    @java.lang.Override()
    public void setLightNavigationBar(boolean enabled) {
    }
    
    @java.lang.Override()
    public void setNavigationbarColor(int color) {
    }
    
    @java.lang.Override()
    public void setTaskDescriptionColor(int color) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    protected AbsSlidingMusicPanelActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
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