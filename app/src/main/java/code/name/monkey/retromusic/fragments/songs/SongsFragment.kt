package code.name.monkey.retromusic.fragments.songs

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.ShuffleButtonSongAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.presenter.SongView
import code.name.monkey.retromusic.util.PreferenceUtilKT
import java.util.*

class SongsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager>(),
    SongView, MainActivityFragmentCallbacks {
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

    override fun songs(songs: List<Song>) {
        adapter?.swapDataSet(songs)
    }

    override fun loadGridSize(): Int {
        return PreferenceUtilKT.songGridSize
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtilKT.songGridSize = gridColumns
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtilKT.songGridSizeLand
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtilKT.songGridSizeLand = gridColumns
    }

    override fun setGridSize(gridSize: Int) {
        adapter?.notifyDataSetChanged()
    }

    override fun showEmptyView() {
        adapter?.swapDataSet(ArrayList())
    }

    override fun loadSortOrder(): String {
        return PreferenceUtilKT.songSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtilKT.songSortOrder = sortOrder
    }

    @LayoutRes
    override fun loadLayoutRes(): Int {
        return PreferenceUtilKT.songGridStyle
    }

    override fun saveLayoutRes(@LayoutRes layoutRes: Int) {
        PreferenceUtilKT.songGridStyle = layoutRes
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
