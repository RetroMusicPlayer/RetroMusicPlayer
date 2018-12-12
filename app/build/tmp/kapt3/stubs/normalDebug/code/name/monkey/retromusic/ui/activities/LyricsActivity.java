package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001c\u0010\f\u001a\u00020\u00052\b\u0010\r\u001a\u0004\u0018\u00010\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u0005H\u0002J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00102\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0012\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0012\u0010\u0015\u001a\u00020\u00122\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0012H\u0016J\b\u0010\u001d\u001a\u00020\u0012H\u0016J\b\u0010\u001e\u001a\u00020\u0012H\u0002J\b\u0010\u001f\u001a\u00020\u0012H\u0002J\b\u0010 \u001a\u00020\u0012H\u0002R\u0014\u0010\u0004\u001a\u00020\u00058BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/LyricsActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity;", "Landroid/view/View$OnClickListener;", "()V", "googleSearchLrcUrl", "", "getGoogleSearchLrcUrl", "()Ljava/lang/String;", "lyrics", "Lcode/name/monkey/retromusic/model/lyrics/Lyrics;", "song", "Lcode/name/monkey/retromusic/model/Song;", "getGoogleSearchUrl", "title", "text", "getSongPaths", "Ljava/util/ArrayList;", "onClick", "", "v", "Landroid/view/View;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onPlayingMetaChanged", "onServiceConnected", "setupWakelock", "showLyricsSaveDialog", "showSyncedLyrics", "app_normalDebug"})
public final class LyricsActivity extends code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity implements android.view.View.OnClickListener {
    private code.name.monkey.retromusic.model.Song song;
    private code.name.monkey.retromusic.model.lyrics.Lyrics lyrics;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.Nullable()
    android.view.View v) {
    }
    
    private final java.lang.String getGoogleSearchLrcUrl() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    private final void setupWakelock() {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void showSyncedLyrics() {
    }
    
    private final void showLyricsSaveDialog() {
    }
    
    private final java.util.ArrayList<java.lang.String> getSongPaths(code.name.monkey.retromusic.model.Song song) {
        return null;
    }
    
    private final java.lang.String getGoogleSearchUrl(java.lang.String title, java.lang.String text) {
        return null;
    }
    
    public LyricsActivity() {
        super();
    }
}