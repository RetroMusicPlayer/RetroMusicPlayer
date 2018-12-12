package code.name.monkey.retromusic.ui.adapter.playlist;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 82\u0012\u0012\b\u0012\u00060\u0002R\u00020\u0000\u0012\u0004\u0012\u00020\u00030\u0001:\u000289B/\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\u001c\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u001eH\u0004J\u0010\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u0003H\u0002J\u0012\u0010!\u001a\u0004\u0018\u00010\u00032\u0006\u0010\"\u001a\u00020\tH\u0014J\b\u0010#\u001a\u00020\tH\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010\"\u001a\u00020\tH\u0016J\u0010\u0010&\u001a\u00020\t2\u0006\u0010\"\u001a\u00020\tH\u0016J\u0010\u0010\'\u001a\u00020(2\u0006\u0010 \u001a\u00020\u0003H\u0014J\u001c\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00190\u00072\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00030+H\u0002J\u0018\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u00072\u0006\u0010 \u001a\u00020\u0003H\u0002J\u001c\u0010,\u001a\u00020-2\n\u0010.\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\"\u001a\u00020\tH\u0016J\u001c\u0010/\u001a\u00060\u0002R\u00020\u00002\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\tH\u0016J\u001e\u00103\u001a\u00020-2\u0006\u00104\u001a\u0002052\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007H\u0014J\u0014\u00107\u001a\u00020-2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007R\u0014\u0010\u0004\u001a\u00020\u0005X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR0\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u00072\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007@DX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\b\u001a\u00020\tX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R \u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0011\"\u0004\b\u001b\u0010\u0013\u00a8\u0006:"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/playlist/PlaylistAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/base/AbsMultiSelectAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/playlist/PlaylistAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/model/Playlist;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "itemLayoutRes", "", "cabHolder", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;ILcode/name/monkey/retromusic/interfaces/CabHolder;)V", "getActivity", "()Landroidx/appcompat/app/AppCompatActivity;", "<set-?>", "getDataSet", "()Ljava/util/ArrayList;", "setDataSet", "(Ljava/util/ArrayList;)V", "getItemLayoutRes", "()I", "setItemLayoutRes", "(I)V", "songs", "Lcode/name/monkey/retromusic/model/Song;", "getSongs", "setSongs", "createViewHolder", "view", "Landroid/view/View;", "getIconRes", "playlist", "getIdentifier", "position", "getItemCount", "getItemId", "", "getItemViewType", "getName", "", "getSongList", "playlists", "", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onMultipleItemAction", "menuItem", "Landroid/view/MenuItem;", "selection", "swapDataSet", "Companion", "ViewHolder", "app_normalDebug"})
public final class PlaylistAdapter extends code.name.monkey.retromusic.ui.adapter.base.AbsMultiSelectAdapter<code.name.monkey.retromusic.ui.adapter.playlist.PlaylistAdapter.ViewHolder, code.name.monkey.retromusic.model.Playlist> {
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> dataSet;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs;
    @org.jetbrains.annotations.NotNull()
    private final androidx.appcompat.app.AppCompatActivity activity = null;
    private int itemLayoutRes;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    private static final int SMART_PLAYLIST = 0;
    private static final int DEFAULT_PLAYLIST = 1;
    public static final code.name.monkey.retromusic.ui.adapter.playlist.PlaylistAdapter.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> getDataSet() {
        return null;
    }
    
    protected final void setDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongs() {
        return null;
    }
    
    public final void setSongs(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> p0) {
    }
    
    public final void swapDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> dataSet) {
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public code.name.monkey.retromusic.ui.adapter.playlist.PlaylistAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final code.name.monkey.retromusic.ui.adapter.playlist.PlaylistAdapter.ViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.playlist.PlaylistAdapter.ViewHolder holder, int position) {
    }
    
    private final int getIconRes(code.name.monkey.retromusic.model.Playlist playlist) {
        return 0;
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    protected code.name.monkey.retromusic.model.Playlist getIdentifier(int position) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected java.lang.String getName(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist playlist) {
        return null;
    }
    
    @java.lang.Override()
    protected void onMultipleItemAction(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem menuItem, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> selection) {
    }
    
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongList(java.util.List<? extends code.name.monkey.retromusic.model.Playlist> playlists) {
        return null;
    }
    
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongs(code.name.monkey.retromusic.model.Playlist playlist) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final androidx.appcompat.app.AppCompatActivity getActivity() {
        return null;
    }
    
    protected final int getItemLayoutRes() {
        return 0;
    }
    
    protected final void setItemLayoutRes(int p0) {
    }
    
    public PlaylistAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Playlist> dataSet, @androidx.annotation.LayoutRes()
    int itemLayoutRes, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder) {
        super(null, null, 0);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016J\u0012\u0010\b\u001a\u00020\t2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/playlist/PlaylistAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/base/MediaEntryViewHolder;", "itemView", "Landroid/view/View;", "(Lcode/name/monkey/retromusic/ui/adapter/playlist/PlaylistAdapter;Landroid/view/View;)V", "onClick", "", "v", "onLongClick", "", "app_normalDebug"})
    public final class ViewHolder extends code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder {
        
        @java.lang.Override()
        public void onClick(@org.jetbrains.annotations.Nullable()
        android.view.View v) {
        }
        
        @java.lang.Override()
        public boolean onLongClick(@org.jetbrains.annotations.Nullable()
        android.view.View v) {
            return false;
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/playlist/PlaylistAdapter$Companion;", "", "()V", "DEFAULT_PLAYLIST", "", "SMART_PLAYLIST", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
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