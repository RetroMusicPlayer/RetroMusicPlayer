package code.name.monkey.retromusic.fragments.artists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.util.PreferenceUtil

class ArtistsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.libraryViewModel.allArtists().observe(
            viewLifecycleOwner, Observer { artists ->
                if (artists.isNotEmpty()) {
                    adapter?.swapDataSet(artists)
                } else {
                    adapter?.swapDataSet(listOf())
                }
            })
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override val emptyMessage: Int
        get() = R.string.no_artists

    override fun setSortOrder(sortOrder: String) {
        mainActivity.libraryViewModel.forceReload(ReloadType.Artists)
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireActivity(), getGridSize())
    }

    override fun createAdapter(): ArtistAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return ArtistAdapter(
            mainActivity,
            dataSet,
            itemLayoutRes(),
            mainActivity
        )
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.artistGridSize
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.artistGridSize = gridColumns
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.artistGridSizeLand
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.artistGridSizeLand = gridColumns
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.artistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.artistSortOrder = sortOrder
    }

    override fun loadLayoutRes(): Int {
        return PreferenceUtil.artistGridStyle
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtil.artistGridStyle = layoutRes
    }

    companion object {
        @JvmField
        val TAG: String = ArtistsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): ArtistsFragment {
            return ArtistsFragment()
        }
    }
}