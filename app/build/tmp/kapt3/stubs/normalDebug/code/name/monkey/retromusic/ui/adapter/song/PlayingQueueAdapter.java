package code.name.monkey.retromusic.ui.adapter.song;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\b\u0018\u0000 12\u00020\u00012\f\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0002:\u000212B/\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\b\u0001\u0010\u000b\u001a\u00020\n\u00a2\u0006\u0002\u0010\fB9\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\b\u0001\u0010\u000b\u001a\u00020\n\u0012\b\b\u0001\u0010\r\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u00060\u0010R\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012H\u0014J\u0010\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\nH\u0016J\u001c\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\b2\n\u0010\u0018\u001a\u00060\u0010R\u00020\u0001H\u0014J\u001c\u0010\u0019\u001a\u00020\u00162\n\u0010\u0018\u001a\u00060\u0010R\u00020\u00012\u0006\u0010\u0014\u001a\u00020\nH\u0016J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\nH\u0016J,\u0010\u001e\u001a\u00020\u001b2\n\u0010\u0018\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020\nH\u0016J\u001e\u0010!\u001a\u0004\u0018\u00010\"2\n\u0010\u0018\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\nH\u0016J \u0010#\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\n2\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\u001bH\u0016J\u0010\u0010\'\u001a\u00020\u00162\u0006\u0010\u0014\u001a\u00020\nH\u0016J\u0018\u0010(\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\n2\u0006\u0010%\u001a\u00020\nH\u0016J\u001c\u0010)\u001a\u00020\u00162\n\u0010\u0018\u001a\u00060\u0010R\u00020\u00012\u0006\u0010*\u001a\u00020+H\u0002J\u001c\u0010,\u001a\u00020\u00162\n\u0010\u0018\u001a\u00060\u0010R\u00020\u00012\u0006\u0010-\u001a\u00020\nH\u0002J\u000e\u0010.\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\nJ\u001c\u0010/\u001a\u00020\u00162\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u0014\u001a\u00020\nJ\u0010\u00100\u001a\u00020\u00162\u0006\u00100\u001a\u00020\u001bH\u0016R\u000e\u0010\r\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/PlayingQueueAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "Lcom/h6ah4i/android/widget/advrecyclerview/draggable/DraggableItemAdapter;", "Lcode/name/monkey/retromusic/ui/adapter/song/PlayingQueueAdapter$ViewHolder;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "dataSet", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "current", "", "itemLayoutRes", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;II)V", "color", "(Landroidx/appcompat/app/AppCompatActivity;Ljava/util/ArrayList;III)V", "createViewHolder", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$ViewHolder;", "view", "Landroid/view/View;", "getItemViewType", "position", "loadAlbumCover", "", "song", "holder", "onBindViewHolder", "onCheckCanDrop", "", "draggingPosition", "dropPosition", "onCheckCanStartDrag", "x", "y", "onGetItemDraggableRange", "Lcom/h6ah4i/android/widget/advrecyclerview/draggable/ItemDraggableRange;", "onItemDragFinished", "fromPosition", "toPosition", "result", "onItemDragStarted", "onMoveItem", "setAlpha", "alpha", "", "setColor", "white", "setCurrent", "swapDataSet", "usePalette", "Companion", "ViewHolder", "app_normalDebug"})
public final class PlayingQueueAdapter extends code.name.monkey.retromusic.ui.adapter.song.SongAdapter implements com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter<code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter.ViewHolder> {
    private int current;
    private int color;
    private static final int HISTORY = 0;
    private static final int CURRENT = 1;
    private static final int UP_NEXT = 2;
    public static final code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter.Companion Companion = null;
    
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
    
    private final void setColor(code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder, int white) {
    }
    
    @java.lang.Override()
    public void usePalette(boolean usePalette) {
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override()
    protected void loadAlbumCover(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Song song, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder) {
    }
    
    public final void swapDataSet(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet, int position) {
    }
    
    public final void setCurrent(int current) {
    }
    
    private final void setAlpha(code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder holder, float alpha) {
    }
    
    @java.lang.Override()
    public boolean onCheckCanStartDrag(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter.ViewHolder holder, int position, int x, int y) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange onGetItemDraggableRange(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter.ViewHolder holder, int position) {
        return null;
    }
    
    @java.lang.Override()
    public void onMoveItem(int fromPosition, int toPosition) {
    }
    
    @java.lang.Override()
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return false;
    }
    
    @java.lang.Override()
    public void onItemDragStarted(int position) {
    }
    
    @java.lang.Override()
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
    }
    
    public PlayingQueueAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet, int current, @androidx.annotation.LayoutRes()
    int itemLayoutRes) {
        super(null, null, 0, false, null, false);
    }
    
    public PlayingQueueAdapter(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> dataSet, int current, @androidx.annotation.LayoutRes()
    int itemLayoutRes, @androidx.annotation.ColorInt()
    int color) {
        super(null, null, 0, false, null, false);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00060\u0001R\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0011\u001a\u00020\bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0014J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\bH\u0016R\u0014\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\b\n\u0000\u0012\u0004\b\t\u0010\nR$\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b8T@TX\u0094\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0019"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/PlayingQueueAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter$ViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/song/SongAdapter;", "Lcom/h6ah4i/android/widget/advrecyclerview/draggable/DraggableItemViewHolder;", "itemView", "Landroid/view/View;", "(Lcode/name/monkey/retromusic/ui/adapter/song/PlayingQueueAdapter;Landroid/view/View;)V", "mDragStateFlags", "", "mDragStateFlags$annotations", "()V", "value", "songMenuRes", "getSongMenuRes", "()I", "setSongMenuRes", "(I)V", "getDragStateFlags", "onSongMenuItemClick", "", "item", "Landroid/view/MenuItem;", "setDragStateFlags", "", "flags", "app_normalDebug"})
    public final class ViewHolder extends code.name.monkey.retromusic.ui.adapter.song.SongAdapter.ViewHolder implements com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder {
        private int mDragStateFlags;
        
        @com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags()
        private static void mDragStateFlags$annotations() {
        }
        
        @java.lang.Override()
        protected int getSongMenuRes() {
            return 0;
        }
        
        @java.lang.Override()
        protected void setSongMenuRes(int value) {
        }
        
        @java.lang.Override()
        protected boolean onSongMenuItemClick(@org.jetbrains.annotations.NotNull()
        android.view.MenuItem item) {
            return false;
        }
        
        @com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags()
        @java.lang.Override()
        public int getDragStateFlags() {
            return 0;
        }
        
        @java.lang.Override()
        public void setDragStateFlags(@com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags()
        int flags) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/song/PlayingQueueAdapter$Companion;", "", "()V", "CURRENT", "", "HISTORY", "UP_NEXT", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}