package code.name.monkey.retromusic.ui.fragments.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b&\u0018\u0000 .2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0002-.B\u0005\u00a2\u0006\u0002\u0010\u0005J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H&J\b\u0010\u0015\u001a\u00020\u0010H\u0016J\b\u0010\u0016\u001a\u00020\u0010H\u0016J\b\u0010\u0017\u001a\u00020\u0010H&J\u0010\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0010H\u0016J\b\u0010\u001c\u001a\u00020\u0010H\u0016J\b\u0010\u001d\u001a\u00020\u0010H&J\u001a\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u000e\u0010#\u001a\u00020\u00102\u0006\u0010$\u001a\u00020 J\u0010\u0010%\u001a\u00020\u00102\u0006\u0010&\u001a\u00020\'H\u0014J\b\u0010(\u001a\u00020)H&J\b\u0010*\u001a\u00020+H&J\b\u0010,\u001a\u00020\u0010H\u0007R(\u0010\b\u001a\u0004\u0018\u00010\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007@BX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001c\u0010\r\u001a\u0010\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsMusicServiceFragment;", "Landroidx/appcompat/widget/Toolbar$OnMenuItemClickListener;", "Lcode/name/monkey/retromusic/interfaces/PaletteColorHolder;", "Lcode/name/monkey/retromusic/ui/fragments/player/PlayerAlbumCoverFragment$Callbacks;", "()V", "<set-?>", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment$Callbacks;", "callbacks", "getCallbacks", "()Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment$Callbacks;", "setCallbacks", "(Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment$Callbacks;)V", "updateIsFavoriteTask", "Landroid/os/AsyncTask;", "onAttach", "", "context", "Landroid/content/Context;", "onBackPressed", "", "onDestroyView", "onDetach", "onHide", "onMenuItemClick", "item", "Landroid/view/MenuItem;", "onPlayingMetaChanged", "onServiceConnected", "onShow", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "setSafeArea", "safeArea", "toggleFavorite", "song", "Lcode/name/monkey/retromusic/model/Song;", "toolbarGet", "Landroidx/appcompat/widget/Toolbar;", "toolbarIconColor", "", "updateIsFavorite", "Callbacks", "Companion", "app_normalDebug"})
public abstract class AbsPlayerFragment extends code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment implements androidx.appcompat.widget.Toolbar.OnMenuItemClickListener, code.name.monkey.retromusic.interfaces.PaletteColorHolder, code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment.Callbacks {
    @org.jetbrains.annotations.Nullable()
    private code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment.Callbacks callbacks;
    private android.os.AsyncTask<?, ?, ?> updateIsFavoriteTask;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    public final code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment.Callbacks getCallbacks() {
        return null;
    }
    
    private final void setCallbacks(code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment.Callbacks p0) {
    }
    
    @java.lang.Override()
    public void onAttach(@org.jetbrains.annotations.Nullable()
    android.content.Context context) {
    }
    
    @java.lang.Override()
    public void onDetach() {
    }
    
    @java.lang.Override()
    public boolean onMenuItemClick(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    protected void toggleFavorite(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.appcompat.widget.Toolbar toolbarGet();
    
    public abstract void onShow();
    
    public abstract void onHide();
    
    public abstract boolean onBackPressed();
    
    public abstract int toolbarIconColor();
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @android.annotation.SuppressLint(value = {"StaticFieldLeak"})
    public final void updateIsFavorite() {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void setSafeArea(@org.jetbrains.annotations.NotNull()
    android.view.View safeArea) {
    }
    
    public AbsPlayerFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment$Callbacks;", "", "onPaletteColorChanged", "", "app_normalDebug"})
    public static abstract interface Callbacks {
        
        public abstract void onPaletteColorChanged();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerFragment$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
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