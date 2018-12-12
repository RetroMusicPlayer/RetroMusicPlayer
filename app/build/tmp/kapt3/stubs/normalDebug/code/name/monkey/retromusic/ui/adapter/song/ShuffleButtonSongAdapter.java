package code.name.monkey.retromusic.ui.adapter.song;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0017B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u00060\u000fR\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0014J\u001c\u0010\u0013\u001a\u00020\u00142\n\u0010\u0015\u001a\u00060\u000fR\u00020\u00102\u0006\u0010\u0016\u001a\u00020\bH\u0016\u00a8\u0006\u0018"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/ShuffleButtonSongAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/song/AbsOffsetSongAdapter;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "itemLayoutRes", "", "usePalette", "", "cabHolder", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;IZLcode/name/monkey/retromusic/interfaces/CabHolder;)V", "createViewHolder", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "view", "Landroid/view/View;", "onBindViewHolder", "", "holder", "position", "ViewHolder", "app_normalDebug"})
public final class ShuffleButtonSongAdapter extends code.name.monkey.retromusic.ui.adapter.song.AbsOffsetSongAdapter {
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder, int position) {
    }
    
    public ShuffleButtonSongAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet, @androidx.annotation.LayoutRes()
    int itemLayoutRes, boolean usePalette, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder) {
        super(null, null, 0, false, null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0012\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0004H\u0016\u00a8\u0006\t"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/ShuffleButtonSongAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/song/AbsOffsetSongAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/song/AbsOffsetSongAdapter;", "itemView", "Landroid/view/View;", "(Lcode/name/monkey/retromusic/ui/adapter/song/ShuffleButtonSongAdapter;Landroid/view/View;)V", "onClick", "", "v", "app_normalDebug"})
    public final class ViewHolder extends code.name.monkey.retromusic.ui.adapter.song.AbsOffsetSongAdapter.ViewHolder {
        
        @java.lang.Override()
        public void onClick(@org.jetbrains.annotations.Nullable()
        android.view.View v) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}