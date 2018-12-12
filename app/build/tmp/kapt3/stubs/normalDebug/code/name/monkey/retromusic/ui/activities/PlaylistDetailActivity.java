package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 62\u00020\u00012\u00020\u00022\u00020\u0003:\u00016B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0012H\u0016J\b\u0010\u0017\u001a\u00020\u0012H\u0016J\u0012\u0010\u0018\u001a\u00020\u00122\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0014J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u0012H\u0016J\b\u0010 \u001a\u00020\u0012H\u0016J\u0010\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020#H\u0016J\b\u0010$\u001a\u00020\u0012H\u0016J\b\u0010%\u001a\u00020\u0012H\u0016J\b\u0010&\u001a\u00020\u0012H\u0014J\u0018\u0010\'\u001a\u00020\b2\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0016J\u0010\u0010,\u001a\u00020\u00122\u0006\u0010-\u001a\u00020.H\u0002J\b\u0010/\u001a\u00020\u0012H\u0002J\b\u00100\u001a\u00020\u0012H\u0002J\u0016\u00101\u001a\u00020\u00122\f\u00102\u001a\b\u0012\u0004\u0012\u00020403H\u0016J\b\u00105\u001a\u00020\u0012H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/PlaylistDetailActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity;", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "Lcode/name/monkey/retromusic/mvp/contract/PlaylistSongsContract$PlaylistSongsView;", "()V", "adapter", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "cab", "Lcom/afollestad/materialcab/MaterialCab;", "playlist", "Lcode/name/monkey/retromusic/model/Playlist;", "recyclerViewDragDropManager", "Lcom/h6ah4i/android/widget/advrecyclerview/draggable/RecyclerViewDragDropManager;", "songsPresenter", "Lcode/name/monkey/retromusic/mvp/presenter/PlaylistSongsPresenter;", "wrappedAdapter", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "checkIsEmpty", "", "completed", "createContentView", "Landroid/view/View;", "loading", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onDestroy", "onMediaStoreChanged", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onPause", "onPlayingMetaChanged", "onResume", "openCab", "menuRes", "", "callback", "Lcom/afollestad/materialcab/MaterialCab$Callback;", "setToolbarTitle", "title", "", "setUpRecyclerView", "setUpToolBar", "showData", "list", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "showEmptyView", "Companion", "app_normalDebug"})
public final class PlaylistDetailActivity extends code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity implements code.name.monkey.retromusic.interfaces.CabHolder, code.name.monkey.retromusic.mvp.contract.PlaylistSongsContract.PlaylistSongsView {
    private code.name.monkey.retromusic.model.Playlist playlist;
    private com.afollestad.materialcab.MaterialCab cab;
    private code.name.monkey.retromusic.ui.adapter.song.SongAdapter adapter;
    private androidx.recyclerview.widget.RecyclerView.Adapter<?> wrappedAdapter;
    private com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager recyclerViewDragDropManager;
    private code.name.monkey.retromusic.mvp.presenter.PlaylistSongsPresenter songsPresenter;
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String EXTRA_PLAYLIST;
    public static final code.name.monkey.retromusic.ui.activities.PlaylistDetailActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected android.view.View createContentView() {
        return null;
    }
    
    private final void setUpRecyclerView() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void setUpToolBar() {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.afollestad.materialcab.MaterialCab openCab(int menuRes, @org.jetbrains.annotations.NotNull()
    com.afollestad.materialcab.MaterialCab.Callback callback) {
        return null;
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    @java.lang.Override()
    public void onMediaStoreChanged() {
    }
    
    private final void setToolbarTitle(java.lang.String title) {
    }
    
    private final void checkIsEmpty() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    @java.lang.Override()
    public void loading() {
    }
    
    @java.lang.Override()
    public void showEmptyView() {
    }
    
    @java.lang.Override()
    public void completed() {
    }
    
    @java.lang.Override()
    public void showData(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> list) {
    }
    
    public PlaylistDetailActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/PlaylistDetailActivity$Companion;", "", "()V", "EXTRA_PLAYLIST", "", "getEXTRA_PLAYLIST", "()Ljava/lang/String;", "setEXTRA_PLAYLIST", "(Ljava/lang/String;)V", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getEXTRA_PLAYLIST() {
            return null;
        }
        
        public final void setEXTRA_PLAYLIST(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        private Companion() {
            super();
        }
    }
}