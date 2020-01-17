package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.ShuffleButtonSongAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.presenter.SongPresenter
import code.name.monkey.retromusic.mvp.presenter.SongView
import code.name.monkey.retromusic.util.PreferenceUtil
import java.util.ArrayList
import javax.inject.Inject

class SongsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, LinearLayoutManager>(),
    SongView {

    @Inject
    lateinit var songPresenter: SongPresenter

    override val emptyMessage: Int
        get() = R.string.no_songs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songPresenter.attachView(this)
    }

    override fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    override fun createAdapter(): SongAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return ShuffleButtonSongAdapter(
            libraryFragment.mainActivity,
            dataSet,
            R.layout.item_list,
            libraryFragment
        )
    }

    override fun songs(songs: ArrayList<Song>) {
        adapter?.swapDataSet(songs)
    }

    override fun onMediaStoreChanged() {
        songPresenter.loadSongs()
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.getInstance(requireContext()).getSongGridSize(activity!!)
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setSongGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireContext()).getSongGridSizeLand(activity!!)
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setSongGridSizeLand(gridColumns)
    }

    public override fun saveUsePalette(usePalette: Boolean) {
        PreferenceUtil.getInstance(requireContext()).setSongColoredFooters(usePalette)
    }

    public override fun loadUsePalette(): Boolean {
        return PreferenceUtil.getInstance(requireContext()).songColoredFooters()
    }

    public override fun setUsePalette(usePalette: Boolean) {
    }

    override fun setGridSize(gridSize: Int) {
        adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isEmpty()) {
            songPresenter.loadSongs()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        songPresenter.detachView()
    }

    override fun showEmptyView() {
        adapter?.swapDataSet(ArrayList())
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance(requireContext()).songSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance(requireContext()).songSortOrder = sortOrder
    }

    override fun setSortOrder(sortOrder: String) {
        songPresenter.loadSongs()
    }

    companion object {

        @JvmField
        var TAG: String = SongsFragment::class.java.simpleName

        fun newInstance(): SongsFragment {
            val args = Bundle()
            val fragment = SongsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setLayoutRes(@LayoutRes layoutRes: Int) {
    }

    @LayoutRes
    override fun loadLayoutRes(): Int {
        return R.layout.item_list
    }

    override fun saveLayoutRes(@LayoutRes layoutRes: Int) {
    }
}
