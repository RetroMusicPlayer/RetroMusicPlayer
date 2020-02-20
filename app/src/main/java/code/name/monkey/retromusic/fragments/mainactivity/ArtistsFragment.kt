package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.presenter.ArtistsPresenter
import code.name.monkey.retromusic.mvp.presenter.ArtistsView
import code.name.monkey.retromusic.util.PreferenceUtil
import javax.inject.Inject

class ArtistsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(),
    ArtistsView, MainActivityFragmentCallbacks {

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun artists(artists: List<Artist>) {
        adapter?.swapDataSet(artists)
    }

    @Inject
    lateinit var artistsPresenter: ArtistsPresenter

    override val emptyMessage: Int
        get() = R.string.no_artists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistsPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isNullOrEmpty()) {
            artistsPresenter.loadArtists()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        artistsPresenter.detachView()
    }

    override fun onMediaStoreChanged() {
        artistsPresenter.loadArtists()
    }

    override fun setSortOrder(sortOrder: String) {
        artistsPresenter.loadArtists()
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
        return PreferenceUtil.getInstance(requireContext()).getArtistGridSize(requireActivity())
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setArtistGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireContext()).getArtistGridSizeLand(requireActivity())
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setArtistGridSizeLand(gridColumns)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance(requireContext()).artistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance(requireContext()).artistSortOrder = sortOrder
    }

    override fun showEmptyView() {
        adapter?.swapDataSet(ArrayList())
    }

    companion object {
        @JvmField
        val TAG: String = ArtistsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): ArtistsFragment {
            val args = Bundle()
            val fragment = ArtistsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setLayoutRes(layoutRes: Int) {
    }

    override fun loadLayoutRes(): Int {
        return PreferenceUtil.getInstance(requireContext()).artistGridStyle
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtil.getInstance(requireContext()).artistGridStyle = layoutRes
    }
}