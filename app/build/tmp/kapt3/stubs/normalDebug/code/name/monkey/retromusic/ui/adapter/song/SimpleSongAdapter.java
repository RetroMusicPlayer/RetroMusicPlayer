package code.name.monkey.retromusic.ui.adapter.song;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u000b\u001a\u00020\bH\u0016J\u001c\u0010\f\u001a\u00020\r2\n\u0010\u000e\u001a\u00060\u000fR\u00020\u00012\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u001c\u0010\u0011\u001a\u00060\u000fR\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\bH\u0016J\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\bJ\u0016\u0010\u0016\u001a\u00020\r2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016R\u000e\u0010\n\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/SimpleSongAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "context", "Landroidx/appcompat/app/AppCompatActivity;", "songs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "i", "", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;I)V", "textColor", "getItemCount", "onBindViewHolder", "", "holder", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$ViewHolder;", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setTextColor", "swapDataSet", "dataSet", "app_normalDebug"})
public final class SimpleSongAdapter extends code.name.monkey.retromusic.ui.adapter.song.SongAdapter {
    private int textColor;
    
    @java.lang.Override()
    public void swapDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void setTextColor(int textColor) {
    }
    
    public SimpleSongAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity context, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs, @androidx.annotation.LayoutRes()
    int i) {
        super(null, null, 0, false, null, false);
    }
}