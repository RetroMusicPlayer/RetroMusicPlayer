package code.name.monkey.retromusic.ui.fragments.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b&\u0018\u0000*\f\b\u0000\u0010\u0001*\u0006\u0012\u0002\b\u00030\u0002*\b\b\u0001\u0010\u0003*\u00020\u00042\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00030\u0005B\u0005\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\b\u0001\u0010\u001c\u001a\u00020\bH\u0002J\u0006\u0010\u001d\u001a\u00020\u000bJ\u0006\u0010\u001e\u001a\u00020\bJ\b\u0010\u001f\u001a\u0004\u0018\u00010\u0015J\b\u0010 \u001a\u00020\bH$J\b\u0010!\u001a\u00020\bH$J\b\u0010\"\u001a\u00020\u0015H$J\b\u0010#\u001a\u00020\u000bH$J\u0012\u0010$\u001a\u00020\u00192\b\b\u0001\u0010\u001c\u001a\u00020\bH\u0004J\u001a\u0010%\u001a\u00020\u00192\u0006\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\u0010\u0010*\u001a\u00020\u00192\u0006\u0010+\u001a\u00020\bH$J\u0010\u0010,\u001a\u00020\u00192\u0006\u0010+\u001a\u00020\bH$J\u0010\u0010-\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u0015H$J\u0010\u0010.\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u000bH$J\u000e\u0010/\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\bJ\u000e\u00100\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u00101\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u000bJ\u0010\u00102\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\bH$J\u0010\u00103\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u0015H$J\u0010\u00104\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u000bH$J\u0006\u0010\u0016\u001a\u00020\u000bR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\fR\u0014\u0010\r\u001a\u00020\b8EX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u0014\u0010\u0012\u001a\u00020\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u000fR\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/base/AbsLibraryPagerRecyclerViewCustomGridSizeFragment;", "A", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "LM", "Landroidx/recyclerview/widget/RecyclerView$LayoutManager;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsLibraryPagerRecyclerViewFragment;", "()V", "currentLayoutRes", "", "gridSize", "isLandscape", "", "()Z", "itemLayoutRes", "getItemLayoutRes", "()I", "maxGridSize", "getMaxGridSize", "maxGridSizeForList", "getMaxGridSizeForList", "sortOrder", "", "usePalette", "usePaletteInitialized", "applyRecyclerViewPaddingForLayoutRes", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "res", "canUsePalette", "getGridSize", "getSortOrder", "loadGridSize", "loadGridSizeLand", "loadSortOrder", "loadUsePalette", "notifyLayoutResChanged", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "saveGridSize", "gridColumns", "saveGridSizeLand", "saveSortOrder", "saveUsePalette", "setAndSaveGridSize", "setAndSaveSortOrder", "setAndSaveUsePalette", "setGridSize", "setSortOrder", "setUsePalette", "app_normalDebug"})
public abstract class AbsLibraryPagerRecyclerViewCustomGridSizeFragment<A extends androidx.recyclerview.widget.RecyclerView.Adapter<?>, LM extends androidx.recyclerview.widget.RecyclerView.LayoutManager> extends code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewFragment<A, LM> {
    private int gridSize;
    private java.lang.String sortOrder;
    private boolean usePaletteInitialized;
    private boolean usePalette;
    private int currentLayoutRes;
    private java.util.HashMap _$_findViewCache;
    
    public final int getMaxGridSize() {
        return 0;
    }
    
    @androidx.annotation.LayoutRes()
    protected final int getItemLayoutRes() {
        return 0;
    }
    
    protected final int getMaxGridSizeForList() {
        return 0;
    }
    
    private final boolean isLandscape() {
        return false;
    }
    
    public final int getGridSize() {
        return 0;
    }
    
    protected abstract void setGridSize(int gridSize);
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSortOrder() {
        return null;
    }
    
    protected abstract void setSortOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String sortOrder);
    
    public final void setAndSaveSortOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String sortOrder) {
    }
    
    /**
     * * @return whether the palette should be used at all or not
     */
    public final boolean usePalette() {
        return false;
    }
    
    public final void setAndSaveGridSize(int gridSize) {
    }
    
    public final void setAndSaveUsePalette(boolean usePalette) {
    }
    
    /**
     * * @return whether the palette option should be available for the current item layout or not
     */
    public final boolean canUsePalette() {
        return false;
    }
    
    protected final void notifyLayoutResChanged(@androidx.annotation.LayoutRes()
    int res) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void applyRecyclerViewPaddingForLayoutRes(androidx.recyclerview.widget.RecyclerView recyclerView, @androidx.annotation.LayoutRes()
    int res) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected abstract java.lang.String loadSortOrder();
    
    protected abstract void saveSortOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String sortOrder);
    
    protected abstract int loadGridSize();
    
    protected abstract void saveGridSize(int gridColumns);
    
    protected abstract int loadGridSizeLand();
    
    protected abstract void saveGridSizeLand(int gridColumns);
    
    protected abstract void saveUsePalette(boolean usePalette);
    
    protected abstract boolean loadUsePalette();
    
    protected abstract void setUsePalette(boolean usePalette);
    
    public AbsLibraryPagerRecyclerViewCustomGridSizeFragment() {
        super();
    }
}