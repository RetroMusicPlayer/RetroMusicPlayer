package io.github.muntashirakon.music.fragments.songs

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.adapter.song.ShuffleButtonSongAdapter
import io.github.muntashirakon.music.adapter.song.SongAdapter
import io.github.muntashirakon.music.fragments.ReloadType
import io.github.muntashirakon.music.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import io.github.muntashirakon.music.interfaces.MainActivityFragmentCallbacks
import io.github.muntashirakon.music.util.PreferenceUtil

class SongsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.libraryViewModel.allSongs()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
        mainActivity.libraryViewModel.forceReload(ReloadType.Songs)
    }

    companion object {
        @JvmField
        var TAG: String = SongsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): SongsFragment {
            return SongsFragment()
        }
    }


    override fun handleBackPress(): Boolean {
        return false
    }
}
