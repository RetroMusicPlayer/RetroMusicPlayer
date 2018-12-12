package code.name.monkey.retromusic.ui.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\n\b\u0016\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0012\u0010\u000b\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J&\u0010\u000e\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\b\u0010\u0013\u001a\u00020\bH\u0016J\b\u0010\u0014\u001a\u00020\bH\u0016J\b\u0010\u0015\u001a\u00020\bH\u0016J\b\u0010\u0016\u001a\u00020\bH\u0016J\b\u0010\u0017\u001a\u00020\bH\u0016J\u0018\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0016J\u001a\u0010\u001c\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\u000e\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\u001aJ\b\u0010\u001f\u001a\u00020\bH\u0002J\b\u0010 \u001a\u00020\bH\u0002J\b\u0010!\u001a\u00020\bH\u0004J\b\u0010\"\u001a\u00020\bH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/MiniPlayerFragment;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsMusicServiceFragment;", "Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Callback;", "Landroid/view/View$OnClickListener;", "()V", "progressViewUpdateHelper", "Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper;", "onClick", "", "view", "Landroid/view/View;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onPause", "onPlayStateChanged", "onPlayingMetaChanged", "onResume", "onServiceConnected", "onUpdateProgressViews", "progress", "", "total", "onViewCreated", "setColor", "playerFragmentColor", "setUpMiniPlayer", "setUpPlayPauseButton", "updatePlayPauseDrawableState", "updateSongTitle", "FlingPlayBackController", "app_normalDebug"})
public class MiniPlayerFragment extends code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment implements code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback, android.view.View.OnClickListener {
    private code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper progressViewUpdateHelper;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpMiniPlayer() {
    }
    
    private final void setUpPlayPauseButton() {
    }
    
    private final void updateSongTitle() {
    }
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    @java.lang.Override()
    public void onPlayStateChanged() {
    }
    
    @java.lang.Override()
    public void onUpdateProgressViews(int progress, int total) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    protected final void updatePlayPauseDrawableState() {
    }
    
    public final void setColor(int playerFragmentColor) {
    }
    
    public MiniPlayerFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0017R\u001a\u0010\u0005\u001a\u00020\u0006X\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/MiniPlayerFragment$FlingPlayBackController;", "Landroid/view/View$OnTouchListener;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "flingPlayBackController", "Landroid/view/GestureDetector;", "getFlingPlayBackController$app_normalDebug", "()Landroid/view/GestureDetector;", "setFlingPlayBackController$app_normalDebug", "(Landroid/view/GestureDetector;)V", "onTouch", "", "v", "Landroid/view/View;", "event", "Landroid/view/MotionEvent;", "app_normalDebug"})
    public static final class FlingPlayBackController implements android.view.View.OnTouchListener {
        @org.jetbrains.annotations.NotNull()
        private android.view.GestureDetector flingPlayBackController;
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.GestureDetector getFlingPlayBackController$app_normalDebug() {
            return null;
        }
        
        public final void setFlingPlayBackController$app_normalDebug(@org.jetbrains.annotations.NotNull()
        android.view.GestureDetector p0) {
        }
        
        @android.annotation.SuppressLint(value = {"ClickableViewAccessibility"})
        @java.lang.Override()
        public boolean onTouch(@org.jetbrains.annotations.NotNull()
        android.view.View v, @org.jetbrains.annotations.NotNull()
        android.view.MotionEvent event) {
            return false;
        }
        
        public FlingPlayBackController(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            super();
        }
    }
}