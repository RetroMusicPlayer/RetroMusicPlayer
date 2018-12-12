package code.name.monkey.retromusic.ui.adapter.artist;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u0012\u0012\b\u0012\u00060\u0002R\u00020\u0000\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004:\u0001=B7\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b\u0012\b\b\u0001\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\u0002\u0010\u000fJ\u0014\u0010\u001e\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001f\u001a\u00020 H\u0004J\u0012\u0010!\u001a\u0004\u0018\u00010\u00032\u0006\u0010\"\u001a\u00020\nH\u0014J\b\u0010#\u001a\u00020\nH\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010\"\u001a\u00020\nH\u0016J\u0010\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u0003H\u0014J\u0010\u0010)\u001a\u00020\'2\u0006\u0010\"\u001a\u00020\nH\u0016J\u001c\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\b2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00030-H\u0002J\u001c\u0010.\u001a\u00020/2\u0006\u0010(\u001a\u00020\u00032\n\u00100\u001a\u00060\u0002R\u00020\u0000H\u0002J\u001c\u00101\u001a\u00020/2\n\u00100\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\"\u001a\u00020\nH\u0016J\u001c\u00102\u001a\u00060\u0002R\u00020\u00002\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u00020\nH\u0016J\u001e\u00106\u001a\u00020/2\u0006\u00107\u001a\u0002082\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00030\bH\u0014J\u001a\u0010:\u001a\u00020/2\u0006\u0010;\u001a\u00020\n2\n\u00100\u001a\u00060\u0002R\u00020\u0000J\u0014\u0010<\u001a\u00020/2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bJ\u000e\u0010\u000b\u001a\u00020/2\u0006\u0010\u000b\u001a\u00020\fR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R \u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u0006>"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/artist/ArtistAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/base/AbsMultiSelectAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/artist/ArtistAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/model/Artist;", "Lcom/simplecityapps/recyclerview_fastscroll/views/FastScrollRecyclerView$SectionedAdapter;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "itemLayoutRes", "", "usePalette", "", "cabHolder", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;IZLcode/name/monkey/retromusic/interfaces/CabHolder;)V", "getActivity", "()Landroidx/appcompat/app/AppCompatActivity;", "getDataSet", "()Ljava/util/ArrayList;", "setDataSet", "(Ljava/util/ArrayList;)V", "getItemLayoutRes", "()I", "setItemLayoutRes", "(I)V", "getUsePalette", "()Z", "setUsePalette", "(Z)V", "createViewHolder", "view", "Landroid/view/View;", "getIdentifier", "position", "getItemCount", "getItemId", "", "getName", "", "artist", "getSectionName", "getSongList", "Lcode/name/monkey/retromusic/model/Song;", "artists", "", "loadArtistImage", "", "holder", "onBindViewHolder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onMultipleItemAction", "menuItem", "Landroid/view/MenuItem;", "selection", "setColors", "color", "swapDataSet", "ViewHolder", "app_normalDebug"})
public final class ArtistAdapter extends code.name.monkey.retromusic.ui.adapter.base.AbsMultiSelectAdapter<code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter.ViewHolder, code.name.monkey.retromusic.model.Artist> implements com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView.SectionedAdapter {
    @org.jetbrains.annotations.NotNull()
    private final androidx.appcompat.app.AppCompatActivity activity = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<code.name.monkey.retromusic.model.Artist> dataSet;
    private int itemLayoutRes;
    private boolean usePalette;
    
    public final void swapDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Artist> dataSet) {
    }
    
    public final void usePalette(boolean usePalette) {
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter.ViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter.ViewHolder holder, int position) {
    }
    
    public final void setColors(int color, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter.ViewHolder holder) {
    }
    
    private final void loadArtistImage(code.name.monkey.retromusic.model.Artist artist, code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter.ViewHolder holder) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    protected code.name.monkey.retromusic.model.Artist getIdentifier(int position) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected java.lang.String getName(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Artist artist) {
        return null;
    }
    
    @java.lang.Override()
    protected void onMultipleItemAction(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem menuItem, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Artist> selection) {
    }
    
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongList(java.util.List<code.name.monkey.retromusic.model.Artist> artists) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getSectionName(int position) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.appcompat.app.AppCompatActivity getActivity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Artist> getDataSet() {
        return null;
    }
    
    public final void setDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Artist> p0) {
    }
    
    public final int getItemLayoutRes() {
        return 0;
    }
    
    public final void setItemLayoutRes(int p0) {
    }
    
    public final boolean getUsePalette() {
        return false;
    }
    
    public final void setUsePalette(boolean p0) {
    }
    
    public ArtistAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Artist> dataSet, @androidx.annotation.LayoutRes()
    int itemLayoutRes, boolean usePalette, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder) {
        super(null, null, 0);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016J\u0012\u0010\b\u001a\u00020\t2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/artist/ArtistAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/base/MediaEntryViewHolder;", "itemView", "Landroid/view/View;", "(Lcode/name/monkey/retromusic/ui/adapter/artist/ArtistAdapter;Landroid/view/View;)V", "onClick", "", "v", "onLongClick", "", "app_normalDebug"})
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
}