package code.name.monkey.retromusic.ui.fragments.mainactivity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 /2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004:\u0001/B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\u0002H\u0014J\b\u0010\u000f\u001a\u00020\u0003H\u0014J\b\u0010\u0010\u001a\u00020\u0007H\u0014J\b\u0010\u0011\u001a\u00020\u0007H\u0014J\b\u0010\u0012\u001a\u00020\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\rH\u0016J\u0012\u0010\u0017\u001a\u00020\r2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\b\u0010\u001a\u001a\u00020\rH\u0016J\b\u0010\u001b\u001a\u00020\rH\u0016J\b\u0010\u001c\u001a\u00020\rH\u0016J\u0010\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\u0007H\u0014J\u0010\u0010\u001f\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\u0007H\u0014J\u0010\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\u0013H\u0014J\u0010\u0010\"\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u0015H\u0016J\u0010\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\u0007H\u0014J\u0010\u0010&\u001a\u00020\r2\u0006\u0010\'\u001a\u00020\u0015H\u0016J\u0010\u0010(\u001a\u00020\r2\u0006\u0010!\u001a\u00020\u0013H\u0014J\u0010\u0010)\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u0015H\u0016J\u0016\u0010*\u001a\u00020\r2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020-0,H\u0016J\b\u0010.\u001a\u00020\rH\u0016R\u0014\u0010\u0006\u001a\u00020\u00078TX\u0094\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/mainactivity/SongsFragment;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsLibraryPagerRecyclerViewCustomGridSizeFragment;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "Landroidx/recyclerview/widget/GridLayoutManager;", "Lcode/name/monkey/retromusic/mvp/contract/SongContract$SongView;", "()V", "emptyMessage", "", "getEmptyMessage", "()I", "presenter", "Lcode/name/monkey/retromusic/mvp/presenter/SongPresenter;", "completed", "", "createAdapter", "createLayoutManager", "loadGridSize", "loadGridSizeLand", "loadSortOrder", "", "loadUsePalette", "", "loading", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onMediaStoreChanged", "onResume", "saveGridSize", "gridColumns", "saveGridSizeLand", "saveSortOrder", "sortOrder", "saveUsePalette", "usePalette", "setGridSize", "gridSize", "setMenuVisibility", "menuVisible", "setSortOrder", "setUsePalette", "showData", "list", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "showEmptyView", "Companion", "app_normalDebug"})
public final class SongsFragment extends code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment<code.name.monkey.retromusic.ui.adapter.song.SongAdapter, androidx.recyclerview.widget.GridLayoutManager> implements code.name.monkey.retromusic.mvp.contract.SongContract.SongView {
    private code.name.monkey.retromusic.mvp.presenter.SongPresenter presenter;
    public static final code.name.monkey.retromusic.ui.fragments.mainactivity.SongsFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected int getEmptyMessage() {
        return 0;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected androidx.recyclerview.widget.GridLayoutManager createLayoutManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected code.name.monkey.retromusic.ui.adapter.song.SongAdapter createAdapter() {
        return null;
    }
    
    @java.lang.Override()
    public void onMediaStoreChanged() {
    }
    
    @java.lang.Override()
    protected int loadGridSize() {
        return 0;
    }
    
    @java.lang.Override()
    protected void saveGridSize(int gridColumns) {
    }
    
    @java.lang.Override()
    protected int loadGridSizeLand() {
        return 0;
    }
    
    @java.lang.Override()
    protected void saveGridSizeLand(int gridColumns) {
    }
    
    @java.lang.Override()
    public void saveUsePalette(boolean usePalette) {
    }
    
    @java.lang.Override()
    public boolean loadUsePalette() {
        return false;
    }
    
    @java.lang.Override()
    public void setUsePalette(boolean usePalette) {
    }
    
    @java.lang.Override()
    protected void setGridSize(int gridSize) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void setMenuVisibility(boolean menuVisible) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public void loading() {
    }
    
    @java.lang.Override()
    public void showData(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> list) {
    }
    
    @java.lang.Override()
    public void showEmptyView() {
    }
    
    @java.lang.Override()
    public void completed() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected java.lang.String loadSortOrder() {
        return null;
    }
    
    @java.lang.Override()
    protected void saveSortOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String sortOrder) {
    }
    
    @java.lang.Override()
    protected void setSortOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String sortOrder) {
    }
    
    public SongsFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/mainactivity/SongsFragment$Companion;", "", "()V", "newInstance", "Lcode/name/monkey/retromusic/ui/fragments/mainactivity/SongsFragment;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.ui.fragments.mainactivity.SongsFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}