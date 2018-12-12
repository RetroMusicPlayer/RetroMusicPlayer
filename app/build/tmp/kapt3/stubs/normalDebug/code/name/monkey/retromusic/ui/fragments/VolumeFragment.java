package code.name.monkey.retromusic.ui.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000e\u0018\u0000 .2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0001.B\u0005\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0010\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J&\u0010\u0014\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010\u001b\u001a\u00020\rH\u0016J \u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020!H\u0016J\b\u0010\"\u001a\u00020\rH\u0016J\u0010\u0010#\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010$\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u001a\u0010%\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0006\u0010&\u001a\u00020\rJ\u0010\u0010\'\u001a\u00020\r2\u0006\u0010(\u001a\u00020!H\u0002J\u0010\u0010)\u001a\u00020\r2\u0006\u0010*\u001a\u00020\u000fH\u0002J\u000e\u0010+\u001a\u00020\r2\u0006\u0010,\u001a\u00020\u000fJ\u0006\u0010-\u001a\u00020\rR\u0016\u0010\u0006\u001a\u0004\u0018\u00010\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/VolumeFragment;", "Landroidx/fragment/app/Fragment;", "Landroid/widget/SeekBar$OnSeekBarChangeListener;", "Lcode/name/monkey/retromusic/volume/OnAudioVolumeChangedListener;", "Landroid/view/View$OnClickListener;", "()V", "audioManager", "Landroid/media/AudioManager;", "getAudioManager", "()Landroid/media/AudioManager;", "audioVolumeObserver", "Lcode/name/monkey/retromusic/volume/AudioVolumeObserver;", "onAudioVolumeChanged", "", "currentVolume", "", "maxVolume", "onClick", "view", "Landroid/view/View;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onProgressChanged", "seekBar", "Landroid/widget/SeekBar;", "i", "b", "", "onResume", "onStartTrackingTouch", "onStopTrackingTouch", "onViewCreated", "removeThumb", "setPauseWhenZeroVolume", "pauseWhenZeroVolume", "setProgressBarColor", "newColor", "setTintable", "color", "tintWhiteColor", "Companion", "app_normalDebug"})
public final class VolumeFragment extends androidx.fragment.app.Fragment implements android.widget.SeekBar.OnSeekBarChangeListener, code.name.monkey.retromusic.volume.OnAudioVolumeChangedListener, android.view.View.OnClickListener {
    private code.name.monkey.retromusic.volume.AudioVolumeObserver audioVolumeObserver;
    public static final code.name.monkey.retromusic.ui.fragments.VolumeFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final android.media.AudioManager getAudioManager() {
        return null;
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
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onAudioVolumeChanged(int currentVolume, int maxVolume) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @java.lang.Override()
    public void onProgressChanged(@org.jetbrains.annotations.NotNull()
    android.widget.SeekBar seekBar, int i, boolean b) {
    }
    
    @java.lang.Override()
    public void onStartTrackingTouch(@org.jetbrains.annotations.NotNull()
    android.widget.SeekBar seekBar) {
    }
    
    @java.lang.Override()
    public void onStopTrackingTouch(@org.jetbrains.annotations.NotNull()
    android.widget.SeekBar seekBar) {
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void tintWhiteColor() {
    }
    
    private final void setProgressBarColor(int newColor) {
    }
    
    public final void setTintable(int color) {
    }
    
    public final void removeThumb() {
    }
    
    private final void setPauseWhenZeroVolume(boolean pauseWhenZeroVolume) {
    }
    
    public VolumeFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/VolumeFragment$Companion;", "", "()V", "newInstance", "Lcode/name/monkey/retromusic/ui/fragments/VolumeFragment;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.ui.fragments.VolumeFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}