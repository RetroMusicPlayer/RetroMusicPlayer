package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.contract.AlbumContract
import code.name.monkey.retromusic.mvp.presenter.AlbumPresenter
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.util.PreferenceUtil

open class AlbumsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<AlbumAdapter, GridLayoutManager>(), AlbumContract.AlbumView {

    private lateinit var presenter: AlbumPresenter


    override val emptyMessage: Int
        get() = R.string.no_albums

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): AlbumAdapter {
        var itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        if (itemLayoutRes != R.layout.item_list) {

            itemLayoutRes = PreferenceUtil.getInstance().getAlbumGridStyle(context!!)
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
        layoutManager!!.spanCount = gridSize
        adapter!!.notifyDataSetChanged()
    }

    override fun setSortOrder(sortOrder: String) {
        presenter.loadAlbums()
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

    override fun onMediaStoreChanged() {
        presenter.loadAlbums()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AlbumPresenter(this)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {

            libraryFragment.setTitle(
                    if (PreferenceUtil.getInstance().tabTitles())
                        R.string.library
                    else
                        R.string.albums)
        }
    }

    override fun onResume() {
        super.onResume()

        libraryFragment.setTitle(
                if (PreferenceUtil.getInstance().tabTitles()) R.string.library else R.string.albums)
        if (adapter!!.dataSet.isEmpty()) {
            presenter.subscribe()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun loading() {}

    override fun showEmptyView() {
        adapter!!.swapDataSet(ArrayList())
    }

    override fun completed() {}

    override fun showData(list: ArrayList<Album>) {
        adapter!!.swapDataSet(list)
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
