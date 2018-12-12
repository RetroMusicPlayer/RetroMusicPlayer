package code.name.monkey.retromusic.ui.fragments.player;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u000f\u0018\u0000 )2\u00020\u00012\u00020\u0002:\u0002()B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0002J&\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u000bH\u0016J\u0010\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\tH\u0016J \u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\tH\u0016J\u0010\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\tH\u0016J\b\u0010\u001e\u001a\u00020\u000bH\u0016J\b\u0010\u001f\u001a\u00020\u000bH\u0016J\b\u0010 \u001a\u00020\u000bH\u0016J\u001a\u0010!\u001a\u00020\u000b2\u0006\u0010\"\u001a\u00020\u000e2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0006\u0010#\u001a\u00020\u000bJ\u0006\u0010$\u001a\u00020\u000bJ\u000e\u0010%\u001a\u00020\u000b2\u0006\u0010&\u001a\u00020\u0005J\b\u0010\'\u001a\u00020\u000bH\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/player/PlayerAlbumCoverFragment;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsMusicServiceFragment;", "Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;", "()V", "callbacks", "Lcode/name/monkey/retromusic/ui/fragments/player/PlayerAlbumCoverFragment$Callbacks;", "colorReceiver", "Lcode/name/monkey/retromusic/ui/adapter/album/AlbumCoverPagerAdapter$AlbumCoverFragment$ColorReceiver;", "currentPosition", "", "notifyColorChange", "", "color", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onPageScrollStateChanged", "state", "onPageScrolled", "position", "positionOffset", "", "positionOffsetPixels", "onPageSelected", "onPlayingMetaChanged", "onQueueChanged", "onServiceConnected", "onViewCreated", "view", "removeEffect", "removeSlideEffect", "setCallbacks", "listener", "updatePlayingQueue", "Callbacks", "Companion", "app_normalDebug"})
public final class PlayerAlbumCoverFragment extends code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment implements androidx.viewpager.widget.ViewPager.OnPageChangeListener {
    private code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment.Callbacks callbacks;
    private int currentPosition;
    private final code.name.monkey.retromusic.ui.adapter.album.AlbumCoverPagerAdapter.AlbumCoverFragment.ColorReceiver colorReceiver = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    public final void removeSlideEffect() {
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
    public void onDestroyView() {
    }
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    @java.lang.Override()
    public void onQueueChanged() {
    }
    
    private final void updatePlayingQueue() {
    }
    
    @java.lang.Override()
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    
    @java.lang.Override()
    public void onPageSelected(int position) {
    }
    
    @java.lang.Override()
    public void onPageScrollStateChanged(int state) {
    }
    
    private final void notifyColorChange(int color) {
    }
    
    public final void setCallbacks(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment.Callbacks listener) {
    }
    
    public final void removeEffect() {
    }
    
    public PlayerAlbumCoverFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0003H&\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/player/PlayerAlbumCoverFragment$Callbacks;", "", "onColorChanged", "", "color", "", "onFavoriteToggled", "app_normalDebug"})
    public static abstract interface Callbacks {
        
        public abstract void onColorChanged(int color);
        
        public abstract void onFavoriteToggled();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/player/PlayerAlbumCoverFragment$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
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