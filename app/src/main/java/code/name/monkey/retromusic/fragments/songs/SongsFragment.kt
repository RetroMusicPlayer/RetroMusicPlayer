package code.name.monkey.retromusic.fragments.songs

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.ShuffleButtonSongAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.util.PreferenceUtil

class SongsFragment :
    AbsRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.songsLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter?.swapDataSet(it)
            } else {
                adapter?.swapDataSet(listOf())
            }
        })
    }

    override val emptyMessage: Int
        get() = R.string.no_songs

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireActivity(), getGridSize()).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) {
                        getGridSize()
                    } else {
                        1
                    }
                }
            }
        }
    }

    override fun createAdapter(): SongAdapter {
        val dataSet = if (adapter == null) mutableListOf() else adapter!!.dataSet
        return ShuffleButtonSongAdapter(
            mainActivity,
            dataSet,
            itemLayoutRes(),
            mainActivity
        )
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.songGridSize
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.songGridSize = gridColumns
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.songGridSizeLand
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.songGridSizeLand = gridColumns
    }

    override fun setGridSize(gridSize: Int) {
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.songSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.songSortOrder = sortOrder
    }

    @LayoutRes
    override fun loadLayoutRes(): Int {
        return PreferenceUtil.songGridStyle
    }

    override fun saveLayoutRes(@LayoutRes layoutRes: Int) {
        PreferenceUtil.songGridStyle = layoutRes
    }

    override fun setSortOrder(sortOrder: String) {
        libraryViewModel.forceReload(ReloadType.Songs)
    }

    companion object {
        @JvmField
        var TAG: String = SongsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): SongsFragment {
            return SongsFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setUpGridSizeMenu(menu.findItem(R.id.action_grid_size).subMenu)
    }

    private fun setUpGridSizeMenu(

        gridSizeMenu: SubMenu
    ) {
        println(getGridSize())
        when (getGridSize()) {
            1 -> gridSizeMenu.findItem(R.id.action_grid_size_1).isChecked = true
            2 -> gridSizeMenu.findItem(R.id.action_grid_size_2).isChecked = true
            3 -> gridSizeMenu.findItem(R.id.action_grid_size_3).isChecked = true
            4 -> gridSizeMenu.findItem(R.id.action_grid_size_4).isChecked = true
            5 -> gridSizeMenu.findItem(R.id.action_grid_size_5).isChecked = true
            6 -> gridSizeMenu.findItem(R.id.action_grid_size_6).isChecked = true
            7 -> gridSizeMenu.findItem(R.id.action_grid_size_7).isChecked = true
            8 -> gridSizeMenu.findItem(R.id.action_grid_size_8).isChecked = true
        }
        val maxGridSize = maxGridSize
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(R.id.action_grid_size_8).isVisible = false
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(R.id.action_grid_size_7).isVisible = false
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(R.id.action_grid_size_6).isVisible = false
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(R.id.action_grid_size_5).isVisible = false
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(R.id.action_grid_size_4).isVisible = false
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(R.id.action_grid_size_3).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (handleGridSizeMenuItem(item)) return true
        return super.onOptionsItemSelected(item)
    }

    fun handleGridSizeMenuItem(
        item: MenuItem
    ): Boolean {
        var gridSize = 0
        when (item.itemId) {
            R.id.action_grid_size_1 -> gridSize = 1
            R.id.action_grid_size_2 -> gridSize = 2
            R.id.action_grid_size_3 -> gridSize = 3
            R.id.action_grid_size_4 -> gridSize = 4
            R.id.action_grid_size_5 -> gridSize = 5
            R.id.action_grid_size_6 -> gridSize = 6
            R.id.action_grid_size_7 -> gridSize = 7
            R.id.action_grid_size_8 -> gridSize = 8
        }
        if (gridSize > 0) {
            item.isChecked = true
            setAndSaveGridSize(gridSize)
            return true
        }
        return false
    }
}
