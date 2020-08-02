package io.github.muntashirakon.music.fragments.artists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.adapter.artist.ArtistAdapter
import io.github.muntashirakon.music.fragments.ReloadType
import io.github.muntashirakon.music.fragments.base.AbsRecyclerViewCustomGridSizeFragment
import io.github.muntashirakon.music.interfaces.MainActivityFragmentCallbacks
import io.github.muntashirakon.music.util.PreferenceUtil

class ArtistsFragment :
    AbsRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    override fun handleBackPress(): Boolean {
        return false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.artistsLiveData
            .observe(viewLifecycleOwner, Observer { artists ->
                if (artists.isNotEmpty()) {
                    adapter?.swapDataSet(artists)
                } else {
                    adapter?.swapDataSet(listOf())
                }
            })
    }

    override val emptyMessage: Int
        get() = R.string.no_artists

    override fun setSortOrder(sortOrder: String) {
        libraryViewModel.forceReload(ReloadType.Artists)
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