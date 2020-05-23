package code.name.monkey.retromusic.fragments.albums

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.util.PreferenceUtilKT

class AlbumsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<AlbumAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    private lateinit var albumViewModel: AlbumViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumViewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)
        albumViewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
            if (albums.isNotEmpty())
                adapter?.swapDataSet(albums)
            else
                adapter?.swapDataSet(listOf())
        })
    }

    override val emptyMessage: Int
        get() = R.string.no_albums

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireActivity(), getGridSize())
    }

    override fun createAdapter(): AlbumAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return AlbumAdapter(
            mainActivity,
            dataSet,
            itemLayoutRes(),
            mainActivity
        )
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {
        return PreferenceUtilKT.albumSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtilKT.albumSortOrder = sortOrder
    }

    override fun loadGridSize(): Int {
        return PreferenceUtilKT.albumGridSize
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtilKT.albumGridSize = gridColumns
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtilKT.albumGridSizeLand
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtilKT.albumGridSizeLand = gridColumns
    }

    override fun onMediaStoreChanged() {
        albumViewModel.getAlbums()
    }

    override fun setSortOrder(sortOrder: String) {
        albumViewModel.getAlbums()
    }


    override fun loadLayoutRes(): Int {
        return PreferenceUtilKT.albumGridStyle
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtilKT.albumGridStyle = layoutRes
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    companion object {
        @JvmField
        var TAG: String = AlbumsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): AlbumsFragment {
            return AlbumsFragment()
        }
    }
}
