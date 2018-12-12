package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u0010H\u0016J\b\u0010\u0014\u001a\u00020\u0010H\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0014J\b\u0010\u0016\u001a\u00020\u0010H\u0016J\b\u0010\u0017\u001a\u00020\u0010H\u0016J\b\u0010\u0018\u001a\u00020\u0010H\u0002J\b\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u0010H\u0002J\b\u0010\u001c\u001a\u00020\u0010H\u0002J\b\u0010\u001d\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/PlayingQueueActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsMusicServiceActivity;", "()V", "linearLayoutManager", "Landroidx/recyclerview/widget/LinearLayoutManager;", "playingQueueAdapter", "Lcode/name/monkey/retromusic/ui/adapter/song/PlayingQueueAdapter;", "recyclerViewDragDropManager", "Lcom/h6ah4i/android/widget/advrecyclerview/draggable/RecyclerViewDragDropManager;", "upNextAndQueueTime", "", "getUpNextAndQueueTime", "()Ljava/lang/String;", "wrappedAdapter", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onMediaStoreChanged", "onPause", "onPlayingMetaChanged", "onQueueChanged", "resetToCurrentPosition", "setUpRecyclerView", "setupToolbar", "updateCurrentSong", "updateQueue", "updateQueuePosition", "app_normalDebug"})
public final class PlayingQueueActivity extends code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity {
    private androidx.recyclerview.widget.RecyclerView.Adapter<?> wrappedAdapter;
    private com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager recyclerViewDragDropManager;
    private code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter playingQueueAdapter;
    private androidx.recyclerview.widget.LinearLayoutManager linearLayoutManager;
    private java.util.HashMap _$_findViewCache;
    
    private final java.lang.String getUpNextAndQueueTime() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpRecyclerView() {
    }
    
    @java.lang.Override()
    public void onQueueChanged() {
    }
    
    @java.lang.Override()
    public void onMediaStoreChanged() {
    }
    
    private final void updateCurrentSong() {
    }
    
    @java.lang.Override()
    public void onPlayingMetaChanged() {
    }
    
    private final void updateQueuePosition() {
    }
    
    private final void updateQueue() {
    }
    
    private final void resetToCurrentPosition() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void setupToolbar() {
    }
    
    public PlayingQueueActivity() {
        super();
    }
}