package code.name.monkey.retromusic.ui.adapter.album;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u00060\rR\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0014J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0006H\u0014J\u0010\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0011H\u0016J\u001c\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u00062\n\u0010\u0019\u001a\u00060\rR\u00020\u0001H\u0014J\u001c\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00112\n\u0010\u0019\u001a\u00060\rR\u00020\u0001H\u0014\u00a8\u0006\u001d"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/album/HorizontalAlbumAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Album;", "usePalette", "", "cabHolder", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;ZLcode/name/monkey/retromusic/interfaces/CabHolder;)V", "createViewHolder", "Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter$ViewHolder;", "view", "Landroid/view/View;", "viewType", "", "getAlbumText", "", "album", "getItemViewType", "position", "loadAlbumCover", "", "holder", "setColors", "color", "Companion", "app_normalDebug"})
public final class HorizontalAlbumAdapter extends code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.adapter.album.HorizontalAlbumAdapter.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    protected void setColors(int color, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder holder) {
    }
    
    @java.lang.Override()
    protected void loadAlbumCover(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Album album, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder holder) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    protected java.lang.String getAlbumText(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Album album) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    public HorizontalAlbumAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> dataSet, boolean usePalette, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder) {
        super(null, null, 0, false, null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/album/HorizontalAlbumAdapter$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
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