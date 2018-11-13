package code.name.monkey.retromusic.ui.fragments.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.RetroUtil;


public abstract class AbsLibraryPagerRecyclerViewCustomGridSizeFragment<A extends RecyclerView.Adapter, LM extends RecyclerView.LayoutManager> extends
        AbsLibraryPagerRecyclerViewFragment<A, LM> {

    private int gridSize;
    private String sortOrder;

    private boolean usePaletteInitialized;
    private boolean usePalette;
    private int currentLayoutRes;

    public final int getGridSize() {
        if (gridSize == 0) {
            if (isLandscape()) {
                gridSize = loadGridSizeLand();
            } else {
                gridSize = loadGridSize();
            }
        }
        return gridSize;
    }

    protected abstract void setGridSize(int gridSize);

    public final String getSortOrder() {
        if (sortOrder == null) {
            sortOrder = loadSortOrder();
        }
        return sortOrder;
    }

    protected abstract void setSortOrder(String sortOrder);

    public void setAndSaveSortOrder(final String sortOrder) {
        this.sortOrder = sortOrder;
        saveSortOrder(sortOrder);
        setSortOrder(sortOrder);
    }

    public int getMaxGridSize() {
        if (isLandscape()) {
            return getResources().getInteger(R.integer.max_columns_land);
        } else {
            return getResources().getInteger(R.integer.max_columns);
        }
    }

    /**
     * @return whether the palette should be used at all or not
     */
    public final boolean usePalette() {
        if (!usePaletteInitialized) {
            usePalette = loadUsePalette();
            usePaletteInitialized = true;
        }
        return usePalette;
    }

    public void setAndSaveGridSize(final int gridSize) {
        int oldLayoutRes = getItemLayoutRes();
        this.gridSize = gridSize;
        if (isLandscape()) {
            saveGridSizeLand(gridSize);
        } else {
            saveGridSize(gridSize);
        }
        // only recreate the adapter and layout manager if the layout currentLayoutRes has changed
        if (oldLayoutRes != getItemLayoutRes()) {
            invalidateLayoutManager();
            invalidateAdapter();
        } else {
            setGridSize(gridSize);
        }
    }

    public void setAndSaveUsePalette(final boolean usePalette) {
        this.usePalette = usePalette;
        saveUsePalette(usePalette);
        setUsePalette(usePalette);
    }

    /**
     * @return whether the palette option should be available for the current item layout or not
     */
    public boolean canUsePalette() {
        return getItemLayoutRes() == R.layout.item_color;
    }

    /**
     * Override to customize which item layout currentLayoutRes should be used. You might also want to
     * override {@link #canUsePalette()} then.
     *
     * @see #getGridSize()
     */
    @LayoutRes
    protected int getItemLayoutRes() {
        if (getGridSize() > getMaxGridSizeForList()) {
            return R.layout.item_grid;
        }
        return R.layout.item_list;
    }

    protected final void notifyLayoutResChanged(@LayoutRes int res) {
        this.currentLayoutRes = res;
        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView != null) {
            applyRecyclerViewPaddingForLayoutRes(recyclerView, currentLayoutRes);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyRecyclerViewPaddingForLayoutRes(getRecyclerView(), currentLayoutRes);
    }

    protected void applyRecyclerViewPaddingForLayoutRes(@NonNull RecyclerView recyclerView,
                                                        @LayoutRes int res) {
        int padding;
        if (res == R.layout.item_grid) {
            padding = (int) (getResources().getDisplayMetrics().density * 2);
        } else {
            padding = 0;
        }
        recyclerView.setPadding(padding, padding, padding, padding);
    }

    protected abstract String loadSortOrder();

    protected abstract void saveSortOrder(String sortOrder);

    protected abstract int loadGridSize();

    protected abstract void saveGridSize(int gridColumns);

    protected abstract int loadGridSizeLand();

    protected abstract void saveGridSizeLand(int gridColumns);

    protected abstract void saveUsePalette(boolean usePalette);

    protected abstract boolean loadUsePalette();

    protected abstract void setUsePalette(boolean usePalette);

    protected int getMaxGridSizeForList() {
        if (isLandscape()) {
            return getActivity().getResources().getInteger(R.integer.default_list_columns_land);
        }
        return getActivity().getResources().getInteger(R.integer.default_list_columns);
    }

    protected final boolean isLandscape() {
        return RetroUtil.isLandscape( );
    }


}
