package code.name.monkey.retromusic.ui.fragments.mainactivity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.contract.ArtistContract
import code.name.monkey.retromusic.mvp.presenter.ArtistPresenter
import code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import java.util.*

class ArtistsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(), ArtistContract.ArtistView {
    private var presenter: ArtistPresenter? = null

    override val emptyMessage: Int
        get() = R.string.no_artists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ArtistPresenter(this)
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): ArtistAdapter {
        var itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        if (itemLayoutRes != R.layout.item_list) {
            itemLayoutRes = PreferenceUtil.getInstance().getArtistGridStyle(context!!)
        }
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return ArtistAdapter(libraryFragment.mainActivity, dataSet, itemLayoutRes, loadUsePalette(), libraryFragment)
    }

    override fun onMediaStoreChanged() {
        presenter!!.loadArtists()
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.getInstance().getArtistGridSize(activity!!)
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.getInstance().setArtistGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance().getArtistGridSizeLand(activity!!)
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.getInstance().setArtistGridSizeLand(gridColumns)
    }

    override fun saveUsePalette(usePalette: Boolean) {
        PreferenceUtil.getInstance().setArtistColoredFooters(usePalette)
    }

    public override fun loadUsePalette(): Boolean {
        return PreferenceUtil.getInstance().artistColoredFooters()
    }

    override fun setUsePalette(usePalette: Boolean) {
        adapter!!.usePalette(usePalette)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager!!.spanCount = gridSize
        adapter!!.notifyDataSetChanged()
    }


    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance().artistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance().artistSortOrder = sortOrder
    }

    override fun setSortOrder(sortOrder: String) {
        presenter!!.loadArtists()
    }


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            libraryFragment.setTitle(
                    if (PreferenceUtil.getInstance().tabTitles())
                        R.string.library
                    else
                        R.string.artists)
        }
    }

    override fun onResume() {
        super.onResume()
        libraryFragment.setTitle(
                if (PreferenceUtil.getInstance().tabTitles()) R.string.library else R.string.artists)
        if (adapter!!.dataSet.isEmpty()) {
            presenter!!.subscribe()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.unsubscribe()
    }

    override fun loading() {}

    override fun showEmptyView() {
        adapter!!.swapDataSet(ArrayList())
    }

    override fun completed() {

    }

    override fun showData(list: ArrayList<Artist>) {
        adapter!!.swapDataSet(list)
    }

    companion object {

        val TAG = ArtistsFragment::class.java.simpleName

        fun newInstance(): ArtistsFragment {

            val args = Bundle()

            val fragment = ArtistsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
