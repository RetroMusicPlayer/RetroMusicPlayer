package code.name.monkey.retromusic.fragments.artists

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.EXTRA_ARTIST_ID
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.extensions.findActivityNavController
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.util.PreferenceUtil

class ArtistsFragment : AbsRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(),
    ArtistClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.artistsLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
                adapter?.swapDataSet(it)
            else
                adapter?.swapDataSet(listOf())
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
            requireActivity(),
            dataSet,
            R.layout.item_grid_circle,
            null,
            this
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

        fun newInstance(): ArtistsFragment {
            return ArtistsFragment()
        }
    }

    override fun onArtist(artistId: Int, imageView: ImageView) {
        val controller = findActivityNavController(R.id.fragment_container)
        controller.navigate(R.id.artistDetailsFragment, bundleOf(EXTRA_ARTIST_ID to artistId))
    }
}

interface ArtistClickListener {
    fun onArtist(artistId: Int, imageView: ImageView)
}