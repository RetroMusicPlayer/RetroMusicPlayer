package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.presenter.ArtistsPresenter
import code.name.monkey.retromusic.mvp.presenter.ArtistsView
import code.name.monkey.retromusic.util.PreferenceUtil
import javax.inject.Inject

class ArtistsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(), ArtistsView {
    override fun artists(artists: ArrayList<Artist>) {
        adapter?.swapDataSet(artists)
    }

    @Inject
    lateinit var artistsPresenter: ArtistsPresenter

    override val emptyMessage: Int
        get() = R.string.no_artists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
        artistsPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isEmpty()) {
            artistsPresenter.loadArtists()
        }
    }

    override fun onMediaStoreChanged() {
        artistsPresenter.loadArtists()
    }

    override fun setSortOrder(sortOrder: String) {
        artistsPresenter.loadArtists()
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): ArtistAdapter {
        var itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        if (itemLayoutRes != R.layout.item_list) {
            itemLayoutRes = PreferenceUtil.getInstance().getArtistGridStyle(requireContext())
        }
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return ArtistAdapter(libraryFragment.mainActivity, dataSet, itemLayoutRes, loadUsePalette(), libraryFragment)
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
        adapter?.usePalette(usePalette)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }


    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance().artistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance().artistSortOrder = sortOrder
    }

    override fun onDestroyView() {
        super.onDestroyView()
        artistsPresenter.detachView()
    }

    override fun showEmptyView() {
        adapter?.swapDataSet(ArrayList())
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
