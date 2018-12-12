package code.name.monkey.retromusic.ui.adapter.song;

import java.lang.System;

/**
 * * Created by hemanths on 13/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u0000 >2\u0012\u0012\b\u0012\u00060\u0002R\u00020\u0000\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u00042\u00020\u0005:\u0002>?BC\b\u0007\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\r\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010!\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\"\u001a\u00020#H\u0014J\u0012\u0010$\u001a\u0004\u0018\u00010\u00032\u0006\u0010%\u001a\u00020\u000bH\u0014J\b\u0010&\u001a\u00020\u000bH\u0016J\u0010\u0010\'\u001a\u00020(2\u0006\u0010%\u001a\u00020\u000bH\u0016J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u0003H\u0014J\u0010\u0010,\u001a\u00020*2\u0006\u0010%\u001a\u00020\u000bH\u0016J\u0012\u0010-\u001a\u0004\u0018\u00010*2\u0006\u0010+\u001a\u00020\u0003H\u0002J\u0012\u0010.\u001a\u0004\u0018\u00010*2\u0006\u0010+\u001a\u00020\u0003H\u0002J\u001c\u0010/\u001a\u0002002\u0006\u0010+\u001a\u00020\u00032\n\u00101\u001a\u00060\u0002R\u00020\u0000H\u0014J\u001c\u00102\u001a\u0002002\n\u00101\u001a\u00060\u0002R\u00020\u00002\u0006\u0010%\u001a\u00020\u000bH\u0016J\u001c\u00103\u001a\u00060\u0002R\u00020\u00002\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u000bH\u0016J\u001e\u00107\u001a\u0002002\u0006\u00108\u001a\u0002092\f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u0014J\u001c\u0010;\u001a\u0002002\u0006\u0010<\u001a\u00020\u000b2\n\u00101\u001a\u00060\u0002R\u00020\u0000H\u0002J\u0016\u0010=\u001a\u0002002\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u0016J\u0010\u0010\f\u001a\u0002002\u0006\u0010\f\u001a\u00020\rH\u0016R\u0014\u0010\u0006\u001a\u00020\u0007X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R0\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\t@DX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\n\u001a\u00020\u000bX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u000e\u0010\u0010\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\rX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 \u00a8\u0006@"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/base/AbsMultiSelectAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/model/Song;", "Lcom/afollestad/materialcab/MaterialCab$Callback;", "Lcom/simplecityapps/recyclerview_fastscroll/views/FastScrollRecyclerView$SectionedAdapter;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "itemLayoutRes", "", "usePalette", "", "cabHolder", "Lcode/name/monkey/retromusic/interfaces/CabHolder;", "showSectionName", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;IZLcode/name/monkey/retromusic/interfaces/CabHolder;Z)V", "getActivity", "()Landroidx/appcompat/app/AppCompatActivity;", "<set-?>", "getDataSet", "()Ljava/util/ArrayList;", "setDataSet", "(Ljava/util/ArrayList;)V", "getItemLayoutRes", "()I", "setItemLayoutRes", "(I)V", "getUsePalette", "()Z", "setUsePalette", "(Z)V", "createViewHolder", "view", "Landroid/view/View;", "getIdentifier", "position", "getItemCount", "getItemId", "", "getName", "", "song", "getSectionName", "getSongText", "getSongTitle", "loadAlbumCover", "", "holder", "onBindViewHolder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onMultipleItemAction", "menuItem", "Landroid/view/MenuItem;", "selection", "setColors", "color", "swapDataSet", "Companion", "ViewHolder", "app_normalDebug"})
public class SongAdapter extends code.name.monkey.retromusic.ui.adapter.base.AbsMultiSelectAdapter<code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder, code.name.monkey.retromusic.model.Song> implements com.afollestad.materialcab.MaterialCab.Callback, com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView.SectionedAdapter {
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet;
    private boolean usePalette;
    private boolean showSectionName;
    @org.jetbrains.annotations.NotNull()
    private final androidx.appcompat.app.AppCompatActivity activity = null;
    private int itemLayoutRes;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.ui.adapter.song.SongAdapter.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getDataSet() {
        return null;
    }
    
    protected final void setDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> p0) {
    }
    
    protected final boolean getUsePalette() {
        return false;
    }
    
    protected final void setUsePalette(boolean p0) {
    }
    
    public void swapDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet) {
    }
    
    public void usePalette(boolean usePalette) {
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    protected code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder, int position) {
    }
    
    private final void setColors(int color, code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder) {
    }
    
    protected void loadAlbumCover(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder) {
    }
    
    private final java.lang.String getSongTitle(code.name.monkey.retromusic.model.Song song) {
        return null;
    }
    
    private final java.lang.String getSongText(code.name.monkey.retromusic.model.Song song) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    protected code.name.monkey.retromusic.model.Song getIdentifier(int position) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected java.lang.String getName(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song) {
        return null;
    }
    
    @java.lang.Override()
    protected void onMultipleItemAction(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem menuItem, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> selection) {
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
    
    public SongAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet, @androidx.annotation.LayoutRes()
    int itemLayoutRes, boolean usePalette, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder, boolean showSectionName) {
        super(null, null, 0);
    }
    
    public SongAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet, @androidx.annotation.LayoutRes()
    int itemLayoutRes, boolean usePalette, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.interfaces.CabHolder cabHolder) {
        super(null, null, 0);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0096\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0016J\u0012\u0010\u0012\u001a\u00020\u00132\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0016J\u0010\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0014R\u0014\u0010\u0005\u001a\u00020\u00068TX\u0094\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0017"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/base/MediaEntryViewHolder;", "itemView", "Landroid/view/View;", "(Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;Landroid/view/View;)V", "song", "Lcode/name/monkey/retromusic/model/Song;", "getSong", "()Lcode/name/monkey/retromusic/model/Song;", "songMenuRes", "", "getSongMenuRes", "()I", "setSongMenuRes", "(I)V", "onClick", "", "v", "onLongClick", "", "onSongMenuItemClick", "item", "Landroid/view/MenuItem;", "app_normalDebug"})
    public class ViewHolder extends code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder {
        private int songMenuRes;
        
        protected int getSongMenuRes() {
            return 0;
        }
        
        protected void setSongMenuRes(int p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        protected code.name.monkey.retromusic.model.Song getSong() {
            return null;
        }
        
        protected boolean onSongMenuItemClick(@org.jetbrains.annotations.NotNull()
        android.view.MenuItem item) {
            return false;
        }
        
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
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
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