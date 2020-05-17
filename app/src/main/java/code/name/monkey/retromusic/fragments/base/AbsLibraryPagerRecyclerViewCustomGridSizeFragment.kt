package code.name.monkey.retromusic.fragments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.RetroUtil

abstract class AbsLibraryPagerRecyclerViewCustomGridSizeFragment<A : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> :
    AbsLibraryPagerRecyclerViewFragment<A, LM>() {

    private var gridSize: Int = 0
    private var sortOrder: String? = null
    private var currentLayoutRes: Int = 0
    private val isLandscape: Boolean
        get() = RetroUtil.isLandscape()

    val maxGridSize: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.max_columns_land)
        } else {
            resources.getInteger(R.integer.max_columns)
        }

    fun itemLayoutRes(): Int {
        return if (getGridSize() > maxGridSizeForList) {
            loadLayoutRes()
        } else R.layout.item_list
    }


    fun setAndSaveLayoutRes(layoutRes: Int) {
        saveLayoutRes(layoutRes)
        invalidateAdapter()
    }

    private val maxGridSizeForList: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.default_list_columns_land)
        } else resources.getInteger(R.integer.default_list_columns)

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


    fun getSortOrder(): String? {
        if (sortOrder == null) {
            sortOrder = loadSortOrder()
        }
        return sortOrder
    }


    fun setAndSaveSortOrder(sortOrder: String) {
        this.sortOrder = sortOrder
        saveSortOrder(sortOrder)
        setSortOrder(sortOrder)
    }

    fun setAndSaveGridSize(gridSize: Int) {
        val oldLayoutRes = itemLayoutRes()
        this.gridSize = gridSize
        if (isLandscape) {
            saveGridSizeLand(gridSize)
        } else {
            saveGridSize(gridSize)
        }
        invalidateLayoutManager()
        // only recreate the adapter and layout manager if the layout currentLayoutRes has changed
        if (oldLayoutRes != itemLayoutRes()) {
            invalidateAdapter()
        } else {
            setGridSize(gridSize)
        }
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

    private fun applyRecyclerViewPaddingForLayoutRes(recyclerView: RecyclerView, res: Int) {
        val padding: Int = if (res == R.layout.item_grid) {
            (resources.displayMetrics.density * 2).toInt()
        } else {
            0
        }
        recyclerView.setPadding(padding, padding, padding, padding)
    }

    protected abstract fun setGridSize(gridSize: Int)

    protected abstract fun setSortOrder(sortOrder: String)

    protected abstract fun loadSortOrder(): String

    protected abstract fun saveSortOrder(sortOrder: String)

    protected abstract fun loadGridSize(): Int

    protected abstract fun saveGridSize(gridColumns: Int)

    protected abstract fun loadGridSizeLand(): Int

    protected abstract fun saveGridSizeLand(gridColumns: Int)

    protected abstract fun loadLayoutRes(): Int

    protected abstract fun saveLayoutRes(layoutRes: Int)
}
