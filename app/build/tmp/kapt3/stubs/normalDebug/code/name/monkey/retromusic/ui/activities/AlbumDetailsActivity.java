package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 52\u00020\u00012\u00020\u0002:\u00015B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0014J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u0014H\u0002J\u0010\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\b\u0010\u001d\u001a\u00020\u0014H\u0016J\u0012\u0010\u001e\u001a\u00020\u00142\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014J\u0010\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020#H\u0016J\b\u0010$\u001a\u00020\u0014H\u0016J\u0010\u0010%\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010&\u001a\u00020\u0014H\u0014J\b\u0010\'\u001a\u00020\u0014H\u0002J\u0010\u0010(\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*H\u0002J\u0012\u0010+\u001a\u00020\u00142\b\u0010,\u001a\u0004\u0018\u00010\u000eH\u0002J\u0010\u0010-\u001a\u00020\u00142\u0006\u0010,\u001a\u00020.H\u0002J\b\u0010/\u001a\u00020\u0014H\u0002J\b\u00100\u001a\u00020\u0014H\u0002J\b\u00101\u001a\u00020\u0014H\u0002J\u0010\u00102\u001a\u00020\u00142\u0006\u00103\u001a\u00020\u0005H\u0016J\b\u00104\u001a\u00020\u0014H\u0016R(\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005@BX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00066"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/AlbumDetailsActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity;", "Lcode/name/monkey/retromusic/mvp/contract/AlbumDetailsContract$AlbumDetailsView;", "()V", "<set-?>", "Lcode/name/monkey/retromusic/model/Album;", "album", "getAlbum", "()Lcode/name/monkey/retromusic/model/Album;", "setAlbum", "(Lcode/name/monkey/retromusic/model/Album;)V", "albumDetailsPresenter", "Lcode/name/monkey/retromusic/mvp/presenter/AlbumDetailsPresenter;", "savedSortOrder", "", "getSavedSortOrder", "()Ljava/lang/String;", "simpleSongAdapter", "Lcode/name/monkey/retromusic/ui/adapter/song/SimpleSongAdapter;", "completed", "", "createContentView", "Landroid/view/View;", "handleSortOrderMenuItem", "", "item", "Landroid/view/MenuItem;", "loadAlbumCover", "loadMoreFrom", "loading", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onMediaStoreChanged", "onOptionsItemSelected", "onPause", "reload", "setColors", "color", "", "setSaveSortOrder", "sortOrder", "setUpSortOrderMenu", "Landroid/view/SubMenu;", "setupRecyclerView", "setupToolbarMarginHeight", "setupWindowTransition", "showData", "list", "showEmptyView", "Companion", "app_normalDebug"})
public final class AlbumDetailsActivity extends code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity implements code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract.AlbumDetailsView {
    private code.name.monkey.retromusic.mvp.presenter.AlbumDetailsPresenter albumDetailsPresenter;
    private code.name.monkey.retromusic.ui.adapter.song.SimpleSongAdapter simpleSongAdapter;
    @org.jetbrains.annotations.Nullable()
    private code.name.monkey.retromusic.model.Album album;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ALBUM_ID = "extra_album_id";
    private static final int TAG_EDITOR_REQUEST = 2001;
    public static final code.name.monkey.retromusic.ui.activities.AlbumDetailsActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    public final code.name.monkey.retromusic.model.Album getAlbum() {
        return null;
    }
    
    private final void setAlbum(code.name.monkey.retromusic.model.Album p0) {
    }
    
    private final java.lang.String getSavedSortOrder() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected android.view.View createContentView() {
        return null;
    }
    
    private final void setupWindowTransition() {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupToolbarMarginHeight() {
    }
    
    @java.lang.Override()
    protected void onPause() {
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
    code.name.monkey.retromusic.model.Album list) {
    }
    
    private final void loadMoreFrom(code.name.monkey.retromusic.model.Album album) {
    }
    
    private final void loadAlbumCover() {
    }
    
    private final void setColors(int color) {
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
    
    private final boolean handleSortOrderMenuItem(android.view.MenuItem item) {
        return false;
    }
    
    private final void setUpSortOrderMenu(android.view.SubMenu sortOrder) {
    }
    
    private final void setSaveSortOrder(java.lang.String sortOrder) {
    }
    
    @java.lang.Override()
    public void onMediaStoreChanged() {
    }
    
    private final void reload() {
    }
    
    public AlbumDetailsActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/AlbumDetailsActivity$Companion;", "", "()V", "EXTRA_ALBUM_ID", "", "TAG_EDITOR_REQUEST", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}