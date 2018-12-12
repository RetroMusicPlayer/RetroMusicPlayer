package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\u0018\u0000 92\u00020\u00012\u00020\u0002:\u00019B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0007H\u0002J\u0010\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0013H\u0002J\u0014\u0010\u001b\u001a\u00020\u00132\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u0013H\u0016J\"\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\b\u0010#\u001a\u0004\u0018\u00010$H\u0014J\u0012\u0010%\u001a\u00020\u00132\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J\u0010\u0010(\u001a\u00020\r2\u0006\u0010)\u001a\u00020*H\u0016J\b\u0010+\u001a\u00020\u0013H\u0016J\u0010\u0010,\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010-\u001a\u00020\u0013H\u0014J\b\u0010.\u001a\u00020\u0013H\u0002J\u0010\u0010/\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u0010\u00100\u001a\u00020\u00132\u0006\u00101\u001a\u00020!H\u0002J\b\u00102\u001a\u00020\u0013H\u0002J\b\u00103\u001a\u00020\u0013H\u0002J\b\u00104\u001a\u00020\u0013H\u0002J\b\u00105\u001a\u00020\u0013H\u0002J\b\u00106\u001a\u00020\u0013H\u0002J\u0010\u00107\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u00108\u001a\u00020\u0013H\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/ArtistDetailActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsSlidingMusicPanelActivity;", "Lcode/name/monkey/retromusic/mvp/contract/ArtistDetailContract$ArtistsDetailsView;", "()V", "albumAdapter", "Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter;", "artist", "Lcode/name/monkey/retromusic/model/Artist;", "artistDetailsPresenter", "Lcode/name/monkey/retromusic/mvp/presenter/ArtistDetailsPresenter;", "biography", "Landroid/text/Spanned;", "forceDownload", "", "lastFMRestClient", "Lcode/name/monkey/retromusic/rest/LastFMRestClient;", "songAdapter", "Lcode/name/monkey/retromusic/ui/adapter/song/SimpleSongAdapter;", "completed", "", "createContentView", "Landroid/view/View;", "getArtist", "handleSortOrderMenuItem", "item", "Landroid/view/MenuItem;", "loadArtistImage", "loadBiography", "lang", "", "loading", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onMediaStoreChanged", "onOptionsItemSelected", "onPause", "reload", "setArtist", "setColors", "color", "setUpViews", "setupContainerHeight", "setupRecyclerView", "setupToolbarMarginHeight", "setupWindowTransistion", "showData", "showEmptyView", "Companion", "app_normalDebug"})
public final class ArtistDetailActivity extends code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity implements code.name.monkey.retromusic.mvp.contract.ArtistDetailContract.ArtistsDetailsView {
    private android.text.Spanned biography;
    private code.name.monkey.retromusic.model.Artist artist;
    private code.name.monkey.retromusic.rest.LastFMRestClient lastFMRestClient;
    private code.name.monkey.retromusic.mvp.presenter.ArtistDetailsPresenter artistDetailsPresenter;
    private code.name.monkey.retromusic.ui.adapter.song.SimpleSongAdapter songAdapter;
    private code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter albumAdapter;
    private boolean forceDownload;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ARTIST_ID = "extra_artist_id";
    public static final int REQUEST_CODE_SELECT_IMAGE = 9003;
    public static final code.name.monkey.retromusic.ui.activities.ArtistDetailActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final void setupWindowTransistion() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected android.view.View createContentView() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpViews() {
    }
    
    private final void setupContainerHeight() {
    }
    
    private final void setupToolbarMarginHeight() {
    }
    
    private final void setupRecyclerView() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
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
    code.name.monkey.retromusic.model.Artist artist) {
    }
    
    private final code.name.monkey.retromusic.model.Artist getArtist() {
        return null;
    }
    
    private final void setArtist(code.name.monkey.retromusic.model.Artist artist) {
    }
    
    private final void loadBiography(java.lang.String lang) {
    }
    
    private final void loadArtistImage() {
    }
    
    private final void setColors(int color) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final boolean handleSortOrderMenuItem(android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public void onMediaStoreChanged() {
    }
    
    private final void reload() {
    }
    
    public ArtistDetailActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/ArtistDetailActivity$Companion;", "", "()V", "EXTRA_ARTIST_ID", "", "REQUEST_CODE_SELECT_IMAGE", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}