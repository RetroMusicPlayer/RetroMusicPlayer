package code.name.monkey.retromusic.fragments.songs

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.ShuffleButtonSongAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.util.PreferenceUtil

class SongsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    lateinit var songViewModel: SongsViewModel

    override val emptyMessage: Int
        get() = R.string.no_songs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel = ViewModelProvider(this).get(SongsViewModel::class.java)
        songViewModel.songs.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { songs ->
                if (songs.isNotEmpty())
                    adapter?.swapDataSet(songs)
                else
                    adapter?.swapDataSet(listOf())
            })
    }

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

    override fun onMediaStoreChanged() {
        //songPresenter.loadSongs()
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.getInstance(requireContext()).getSongGridSize(requireContext())
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setSongGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireContext()).getSongGridSizeLand(requireContext())
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setSongGridSizeLand(gridColumns)
    }

    override fun setGridSize(gridSize: Int) {
        adapter?.notifyDataSetChanged()
    }


    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance(requireContext()).songSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance(requireContext()).songSortOrder = sortOrder
    }

    override fun setSortOrder(sortOrder: String) {
        songViewModel.loadSongs()
    }

    companion object {

        @JvmField
        var TAG: String = SongsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): SongsFragment {
            val args = Bundle()
            val fragment =
                SongsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setLayoutRes(@LayoutRes layoutRes: Int) {
    }

    @LayoutRes
    override fun loadLayoutRes(): Int {
        return PreferenceUtil.getInstance(requireContext()).songGridStyle
    }

    override fun saveLayoutRes(@LayoutRes layoutRes: Int) {
        PreferenceUtil.getInstance(requireContext()).songGridStyle = layoutRes
    }

    override fun handleBackPress(): Boolean {
        return false
    }
}
