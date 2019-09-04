package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.presenter.AlbumsPresenter
import code.name.monkey.retromusic.mvp.presenter.AlbumsView
import code.name.monkey.retromusic.util.PreferenceUtil
import javax.inject.Inject

open class AlbumsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<AlbumAdapter, GridLayoutManager>(), AlbumsView {
    override fun albums(albums: java.util.ArrayList<Album>) {
        adapter?.swapDataSet(albums)
    }

    @Inject
    lateinit var albumsPresenter: AlbumsPresenter

    override val emptyMessage: Int
        get() = R.string.no_albums

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): AlbumAdapter {
        var itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        if (itemLayoutRes != R.layout.item_list) {
            itemLayoutRes = PreferenceUtil.getInstance().getAlbumGridStyle(requireContext())
        }
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return AlbumAdapter(libraryFragment.mainActivity, dataSet, itemLayoutRes, loadUsePalette(), libraryFragment)
    }

    public override fun loadUsePalette(): Boolean {
        return PreferenceUtil.getInstance().albumColoredFooters()
    }

    override fun setUsePalette(usePalette: Boolean) {
        adapter!!.usePalette(usePalette)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }


    override fun loadSortOrder(): String {

        return PreferenceUtil.getInstance().albumSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {

        PreferenceUtil.getInstance().albumSortOrder = sortOrder
    }

    override fun loadGridSize(): Int {

        return PreferenceUtil.getInstance().getAlbumGridSize(activity!!)
    }

    override fun saveGridSize(gridColumns: Int) {

        PreferenceUtil.getInstance().setAlbumGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {

        return PreferenceUtil.getInstance().getAlbumGridSizeLand(activity!!)
    }

    override fun saveGridSizeLand(gridColumns: Int) {

        PreferenceUtil.getInstance().setAlbumGridSizeLand(gridColumns)
    }

    override fun saveUsePalette(usePalette: Boolean) {

        PreferenceUtil.getInstance().setAlbumColoredFooters(usePalette)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.musicComponent.inject(this)
        albumsPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isEmpty()) {
            albumsPresenter.loadAlbums()
        }
    }

    override fun onMediaStoreChanged() {
        albumsPresenter.loadAlbums()
    }

    override fun setSortOrder(sortOrder: String) {
        albumsPresenter.loadAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        albumsPresenter.detachView()
    }


    override fun showEmptyView() {
        adapter?.swapDataSet(ArrayList())
    }

    companion object {

        val TAG: String = AlbumsFragment::class.java.simpleName

        fun newInstance(): AlbumsFragment {
            val args = Bundle()
            val fragment = AlbumsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
