package code.name.monkey.retromusic.fragments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.RetroUtil


abstract class AbsLibraryPagerRecyclerViewCustomGridSizeFragment<A : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> : AbsLibraryPagerRecyclerViewFragment<A, LM>() {

    private var gridSize: Int = 0
    private var sortOrder: String? = null

    private var usePaletteInitialized: Boolean = false
    private var usePalette: Boolean = false
    private var currentLayoutRes: Int = 0

    val maxGridSize: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.max_columns_land)
        } else {
            resources.getInteger(R.integer.max_columns)
        }

    /**
     * Override to customize which item layout currentLayoutRes should be used. You might also want to
     * override [.canUsePalette] then.
     *
     * @see .getGridSize
     */
    protected val itemLayoutRes: Int
        @LayoutRes
        get() = if (getGridSize() > maxGridSizeForList) {
            R.layout.item_grid
        } else R.layout.item_list

    protected val maxGridSizeForList: Int
        get() = if (isLandscape) {
            activity!!.resources.getInteger(R.integer.default_list_columns_land)
        } else activity!!.resources.getInteger(R.integer.default_list_columns)

    private val isLandscape: Boolean
        get() = RetroUtil.isLandscape()

    fun getGridSize(): Int {
        if (gridSize == 0) {
            gridSize = if (isLandscape) {
                loadGridSizeLand()
            } else {
                loadGridSize()
            }
        }
        return gridSize
    }

    protected abstract fun setGridSize(gridSize: Int)

    fun getSortOrder(): String? {
        if (sortOrder == null) {
            sortOrder = loadSortOrder()
        }
        return sortOrder
    }

    protected abstract fun setSortOrder(sortOrder: String)

    fun setAndSaveSortOrder(sortOrder: String) {
        this.sortOrder = sortOrder
        saveSortOrder(sortOrder)
        setSortOrder(sortOrder)
    }

    /**
     * @return whether the palette should be used at all or not
     */
    fun usePalette(): Boolean {
        if (!usePaletteInitialized) {
            usePalette = loadUsePalette()
            usePaletteInitialized = true
        }
        return usePalette
    }

    fun setAndSaveGridSize(gridSize: Int) {
        val oldLayoutRes = itemLayoutRes
        this.gridSize = gridSize
        if (isLandscape) {
            saveGridSizeLand(gridSize)
        } else {
            saveGridSize(gridSize)
        }
        // only recreate the adapter and layout manager if the layout currentLayoutRes has changed
        if (oldLayoutRes != itemLayoutRes) {
            invalidateLayoutManager()
            invalidateAdapter()
        } else {
            setGridSize(gridSize)
        }
    }

    fun setAndSaveUsePalette(usePalette: Boolean) {
        this.usePalette = usePalette
        saveUsePalette(usePalette)
        setUsePalette(usePalette)
    }

    /**
     * @return whether the palette option should be available for the current item layout or not
     */
    fun canUsePalette(): Boolean {
        return itemLayoutRes == R.layout.item_card_color
    }

    protected fun notifyLayoutResChanged(@LayoutRes res: Int) {
        this.currentLayoutRes = res
        val recyclerView = recyclerView()
        applyRecyclerViewPaddingForLayoutRes(recyclerView, currentLayoutRes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyRecyclerViewPaddingForLayoutRes(recyclerView(), currentLayoutRes)
    }

    private fun applyRecyclerViewPaddingForLayoutRes(recyclerView: RecyclerView,
                                                     @LayoutRes res: Int) {
        val padding: Int
        if (res == R.layout.item_grid) {
            padding = (resources.displayMetrics.density * 2).toInt()
        } else {
            padding = 0
        }
        recyclerView.setPadding(padding, padding, padding, padding)
    }

    protected abstract fun loadSortOrder(): String

    protected abstract fun saveSortOrder(sortOrder: String)

    protected abstract fun loadGridSize(): Int

    protected abstract fun saveGridSize(gridColumns: Int)

    protected abstract fun loadGridSizeLand(): Int

    protected abstract fun saveGridSizeLand(gridColumns: Int)

    protected abstract fun saveUsePalette(usePalette: Boolean)

    protected abstract fun loadUsePalette(): Boolean

    protected abstract fun setUsePalette(usePalette: Boolean)


}
