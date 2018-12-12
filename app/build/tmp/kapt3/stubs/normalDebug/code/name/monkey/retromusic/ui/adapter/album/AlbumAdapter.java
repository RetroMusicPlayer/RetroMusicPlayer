package code.name.monkey.retromusic.ui.adapter.album;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u0000 A2\u0012\u0012\b\u0012\u00060\u0002R\u00020\u0000\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004:\u0002ABB7\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b\u0012\b\b\u0001\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u001f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\nH\u0014J\u0012\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020\u0003H\u0014J\u0012\u0010&\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020\u0003H\u0002J\u0012\u0010\'\u001a\u0004\u0018\u00010\u00032\u0006\u0010(\u001a\u00020\nH\u0014J\b\u0010)\u001a\u00020\nH\u0016J\u0010\u0010*\u001a\u00020+2\u0006\u0010(\u001a\u00020\nH\u0016J\u0010\u0010,\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0003H\u0014J\u0010\u0010-\u001a\u00020$2\u0006\u0010(\u001a\u00020\nH\u0016J\u001c\u0010.\u001a\b\u0012\u0004\u0012\u00020/0\b2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u000301H\u0002J\u001c\u00102\u001a\u0002032\u0006\u0010%\u001a\u00020\u00032\n\u00104\u001a\u00060\u0002R\u00020\u0000H\u0014J\u001c\u00105\u001a\u0002032\n\u00104\u001a\u00060\u0002R\u00020\u00002\u0006\u0010(\u001a\u00020\nH\u0016J\u001c\u00106\u001a\u00060\u0002R\u00020\u00002\u0006\u00107\u001a\u0002082\u0006\u0010\"\u001a\u00020\nH\u0016J\u001e\u00109\u001a\u0002032\u0006\u0010:\u001a\u00020;2\f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00030\bH\u0014J\u001c\u0010=\u001a\u0002032\u0006\u0010>\u001a\u00020\n2\n\u00104\u001a\u00060\u0002R\u00020\u0000H\u0014J\u0014\u0010?\u001a\u0002032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bJ\u000e\u0010@\u001a\u0002032\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u0002032\u0006\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0005\u001a\u00020\u0006X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R0\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\b@DX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\t\u001a\u00020\nX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u000b\u001a\u00020\fX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001e\u00a8\u0006C"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/base/AbsMultiSelectAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/model/Album;", "Lcom/simplecityapps/recyclerview_fastscroll/views/FastScrollRecyclerView$SectionedAdapter;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "itemLayoutRes", "", "usePalette", "", "cabHolder", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;IZLcode/name/monkey/retromusic/interfaces/CabHolder;)V", "getActivity", "()Landroidx/appcompat/app/AppCompatActivity;", "<set-?>", "getDataSet", "()Ljava/util/ArrayList;", "setDataSet", "(Ljava/util/ArrayList;)V", "getItemLayoutRes", "()I", "setItemLayoutRes", "(I)V", "getUsePalette", "()Z", "setUsePalette", "(Z)V", "createViewHolder", "view", "Landroid/view/View;", "viewType", "getAlbumText", "", "album", "getAlbumTitle", "getIdentifier", "position", "getItemCount", "getItemId", "", "getName", "getSectionName", "getSongList", "Lcode/name/monkey/retromusic/model/Song;", "albums", "", "loadAlbumCover", "", "holder", "onBindViewHolder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "onMultipleItemAction", "menuItem", "Landroid/view/MenuItem;", "selection", "setColors", "color", "swapDataSet", "useItemLayout", "Companion", "ViewHolder", "app_normalDebug"})
public class AlbumAdapter extends code.name.monkey.retromusic.ui.adapter.base.AbsMultiSelectAdapter<code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder, code.name.monkey.retromusic.model.Album> implements com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView.SectionedAdapter {
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<code.name.monkey.retromusic.model.Album> dataSet;
    private boolean usePalette;
    @org.jetbrains.annotations.NotNull()
    private final androidx.appcompat.app.AppCompatActivity activity = null;
    private int itemLayoutRes;
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Album> getDataSet() {
        return null;
    }
    
    protected final void setDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> p0) {
    }
    
    protected final boolean getUsePalette() {
        return false;
    }
    
    protected final void setUsePalette(boolean p0) {
    }
    
    public final void useItemLayout(int itemLayoutRes) {
    }
    
    public final void usePalette(boolean usePalette) {
    }
    
    public final void swapDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> dataSet) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    private final java.lang.String getAlbumTitle(code.name.monkey.retromusic.model.Album album) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected java.lang.String getAlbumText(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Album album) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder holder, int position) {
    }
    
    protected void setColors(int color, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder holder) {
    }
    
    protected void loadAlbumCover(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Album album, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter.ViewHolder holder) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    protected code.name.monkey.retromusic.model.Album getIdentifier(int position) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected java.lang.String getName(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Album album) {
        return null;
    }
    
    @java.lang.Override()
    protected void onMultipleItemAction(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem menuItem, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> selection) {
    }
    
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongList(java.util.List<code.name.monkey.retromusic.model.Album> albums) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getSectionName(int position) {
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
    
    public AlbumAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> dataSet, @androidx.annotation.LayoutRes()
    int itemLayoutRes, boolean usePalette, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder) {
        super(null, null, 0);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016J\u0012\u0010\b\u001a\u00020\t2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/base/MediaEntryViewHolder;", "itemView", "Landroid/view/View;", "(Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter;Landroid/view/View;)V", "onClick", "", "v", "onLongClick", "", "app_normalDebug"})
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
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/album/AlbumAdapter$Companion;", "", "()V", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
    public static final class Companion {
        
        public final java.lang.String getTAG() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}