package code.name.monkey.retromusic.ui.fragments.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000*\f\b\u0000\u0010\u0001*\u0006\u0012\u0002\b\u00030\u0002*\b\b\u0001\u0010\u0003*\u00020\u00042\u00020\u00052\u00020\u0006B\u0005\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0019H\u0002J\r\u0010\u001b\u001a\u00028\u0000H%\u00a2\u0006\u0002\u0010\nJ\r\u0010\u001c\u001a\u00028\u0001H$\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u001d\u001a\u00020\u0019H\u0002J\b\u0010\u001e\u001a\u00020\u0019H\u0002J\b\u0010\u001f\u001a\u00020\u0019H\u0004J\b\u0010 \u001a\u00020\u0019H\u0004J&\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\b\u0010)\u001a\u00020\u0019H\u0016J\u001a\u0010*\u001a\u00020\u00192\b\u0010+\u001a\u0004\u0018\u00010,2\u0006\u0010-\u001a\u00020\u000fH\u0016J\b\u0010.\u001a\u00020\u0019H\u0016J\b\u0010/\u001a\u00020\u0019H\u0016J\u001a\u00100\u001a\u00020\u00192\u0006\u00101\u001a\u00020\"2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\u0006\u00102\u001a\u000203J\b\u00104\u001a\u00020\u0019H\u0002R\u001e\u0010\b\u001a\u0004\u0018\u00018\u0000X\u0084\u000e\u00a2\u0006\u0010\n\u0002\u0010\r\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0014\u0010\u000e\u001a\u00020\u000f8UX\u0094\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u001e\u0010\u0012\u001a\u0004\u0018\u00018\u0001X\u0084\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0017\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016\u00a8\u00065"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/base/AbsLibraryPagerRecyclerViewFragment;", "A", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "LM", "Landroidx/recyclerview/widget/RecyclerView$LayoutManager;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsLibraryPagerFragment;", "Lcom/google/android/material/appbar/AppBarLayout$OnOffsetChangedListener;", "()V", "adapter", "getAdapter", "()Landroidx/recyclerview/widget/RecyclerView$Adapter;", "setAdapter", "(Landroidx/recyclerview/widget/RecyclerView$Adapter;)V", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "emptyMessage", "", "getEmptyMessage", "()I", "layoutManager", "getLayoutManager", "()Landroidx/recyclerview/widget/RecyclerView$LayoutManager;", "setLayoutManager", "(Landroidx/recyclerview/widget/RecyclerView$LayoutManager;)V", "Landroidx/recyclerview/widget/RecyclerView$LayoutManager;", "checkForPadding", "", "checkIsEmpty", "createAdapter", "createLayoutManager", "initAdapter", "initLayoutManager", "invalidateAdapter", "invalidateLayoutManager", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onOffsetChanged", "p0", "Lcom/google/android/material/appbar/AppBarLayout;", "i", "onQueueChanged", "onServiceConnected", "onViewCreated", "view", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "setUpRecyclerView", "app_normalDebug"})
public abstract class AbsLibraryPagerRecyclerViewFragment<A extends androidx.recyclerview.widget.RecyclerView.Adapter<?>, LM extends androidx.recyclerview.widget.RecyclerView.LayoutManager> extends code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerFragment implements com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener {
    @org.jetbrains.annotations.Nullable()
    private A adapter;
    @org.jetbrains.annotations.Nullable()
    private LM layoutManager;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    protected final A getAdapter() {
        return null;
    }
    
    protected final void setAdapter(@org.jetbrains.annotations.Nullable()
    A p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final LM getLayoutManager() {
        return null;
    }
    
    protected final void setLayoutManager(@org.jetbrains.annotations.Nullable()
    LM p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpRecyclerView() {
    }
    
    private final void initAdapter() {
    }
    
    @androidx.annotation.StringRes()
    protected int getEmptyMessage() {
        return 0;
    }
    
    private final void checkIsEmpty() {
    }
    
    private final void checkForPadding() {
    }
    
    private final void initLayoutManager() {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected abstract LM createLayoutManager();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.annotation.NonNull()
    protected abstract A createAdapter();
    
    @java.lang.Override()
    public void onOffsetChanged(@org.jetbrains.annotations.Nullable()
    com.google.android.material.appbar.AppBarLayout p0, int i) {
    }
    
    @java.lang.Override()
    public void onQueueChanged() {
    }
    
    @java.lang.Override()
    public void onServiceConnected() {
    }
    
    protected final void invalidateLayoutManager() {
    }
    
    protected final void invalidateAdapter() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.recyclerview.widget.RecyclerView recyclerView() {
        return null;
    }
    
    public AbsLibraryPagerRecyclerViewFragment() {
        super();
    }
}