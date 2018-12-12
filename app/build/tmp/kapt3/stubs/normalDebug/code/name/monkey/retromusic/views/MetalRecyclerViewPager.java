package code.name.monkey.retromusic.views;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0002\u0011\u0012B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007B!\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u0016\u0010\u000e\u001a\u00020\r2\f\u0010\u000f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0010H\u0016R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcode/name/monkey/retromusic/views/MetalRecyclerViewPager;", "Landroidx/recyclerview/widget/RecyclerView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "itemMargin", "init", "", "setAdapter", "adapter", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "MetalAdapter", "MetalViewHolder", "app_normalDebug"})
public final class MetalRecyclerViewPager extends androidx.recyclerview.widget.RecyclerView {
    private int itemMargin;
    private java.util.HashMap _$_findViewCache;
    
    public final void init(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
    }
    
    @java.lang.Override()
    public void setAdapter(@org.jetbrains.annotations.Nullable()
    androidx.recyclerview.widget.RecyclerView.Adapter<?> adapter) {
    }
    
    public MetalRecyclerViewPager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    public MetalRecyclerViewPager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public MetalRecyclerViewPager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u000f\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001d\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00028\u00002\u0006\u0010\u000f\u001a\u00020\nH\u0016\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u0012\u001a\u00020\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcode/name/monkey/retromusic/views/MetalRecyclerViewPager$MetalAdapter;", "VH", "Lcode/name/monkey/retromusic/views/MetalRecyclerViewPager$MetalViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "displayMetrics", "Landroid/util/DisplayMetrics;", "(Landroid/util/DisplayMetrics;)V", "getDisplayMetrics", "()Landroid/util/DisplayMetrics;", "itemMargin", "", "itemWidth", "onBindViewHolder", "", "holder", "position", "(Lcode/name/monkey/retromusic/views/MetalRecyclerViewPager$MetalViewHolder;I)V", "setItemMargin", "updateDisplayMetrics", "app_normalDebug"})
    public static abstract class MetalAdapter<VH extends code.name.monkey.retromusic.views.MetalRecyclerViewPager.MetalViewHolder> extends androidx.recyclerview.widget.RecyclerView.Adapter<VH> {
        private int itemMargin;
        private int itemWidth;
        @org.jetbrains.annotations.NotNull()
        private final android.util.DisplayMetrics displayMetrics = null;
        
        public final void setItemMargin(int itemMargin) {
        }
        
        public final void updateDisplayMetrics() {
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        VH holder, int position) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.util.DisplayMetrics getDisplayMetrics() {
            return null;
        }
        
        public MetalAdapter(@org.jetbrains.annotations.NotNull()
        @androidx.annotation.NonNull()
        android.util.DisplayMetrics displayMetrics) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcode/name/monkey/retromusic/views/MetalRecyclerViewPager$MetalViewHolder;", "Lcode/name/monkey/retromusic/ui/adapter/base/MediaEntryViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "rootLayout", "Landroid/view/ViewGroup;", "getRootLayout", "()Landroid/view/ViewGroup;", "setRootLayout", "(Landroid/view/ViewGroup;)V", "app_normalDebug"})
    public static abstract class MetalViewHolder extends code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.view.ViewGroup rootLayout;
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.ViewGroup getRootLayout() {
            return null;
        }
        
        public final void setRootLayout(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup p0) {
        }
        
        public MetalViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}