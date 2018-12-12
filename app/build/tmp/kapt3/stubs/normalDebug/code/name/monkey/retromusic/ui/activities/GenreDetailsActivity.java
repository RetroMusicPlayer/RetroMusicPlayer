package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

/**
 * * @author Hemanth S (h4h13).
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 -2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001-B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u000eH\u0016J\b\u0010\u0013\u001a\u00020\u000eH\u0016J\u0012\u0010\u0014\u001a\u00020\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u000eH\u0016J\u0010\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u000eH\u0014J\b\u0010 \u001a\u00020\u000eH\u0014J\u0018\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0016J\b\u0010&\u001a\u00020\u000eH\u0002J\b\u0010\'\u001a\u00020\u000eH\u0002J\u0016\u0010(\u001a\u00020\u000e2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020+0*H\u0016J\b\u0010,\u001a\u00020\u000eH\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/GenreDetailsActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity;", "Lcode/name/monkey/retromusic/mvp/contract/GenreDetailsContract$GenreDetailsView;", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "()V", "cab", "Lcom/afollestad/materialcab/MaterialCab;", "genre", "Lcode/name/monkey/retromusic/model/Genre;", "presenter", "Lcode/name/monkey/retromusic/mvp/presenter/GenreDetailsPresenter;", "songAdapter", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "checkIsEmpty", "", "completed", "createContentView", "Landroid/view/View;", "loading", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onMediaStoreChanged", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onPause", "onResume", "openCab", "menuRes", "", "callback", "Lcom/afollestad/materialcab/MaterialCab$Callback;", "setUpToolBar", "setupRecyclerView", "showData", "list", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "showEmptyView", "Companion", "app_normalDebug"})
public final class GenreDetailsActivity extends code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity implements code.name.monkey.retromusic.mvp.contract.GenreDetailsContract.GenreDetailsView, code.name.monkey.retromusic.interfaces.CabHolder {
    private code.name.monkey.retromusic.model.Genre genre;
    private code.name.monkey.retromusic.mvp.presenter.GenreDetailsPresenter presenter;
    private code.name.monkey.retromusic.ui.adapter.song.SongAdapter songAdapter;
    private com.afollestad.materialcab.MaterialCab cab;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_GENRE_ID = "extra_genre_id";
    public static final code.name.monkey.retromusic.ui.activities.GenreDetailsActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final void checkIsEmpty() {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpToolBar() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected android.view.View createContentView() {
        return null;
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
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void setupRecyclerView() {
    }
    
    @java.lang.Override()
    public void showData(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> list) {
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
    
    public GenreDetailsActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/GenreDetailsActivity$Companion;", "", "()V", "EXTRA_GENRE_ID", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}