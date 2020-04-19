package code.name.monkey.retromusic.fragments.mainactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.playlist.PlaylistAdapter
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.mvp.presenter.PlaylistView
import code.name.monkey.retromusic.mvp.presenter.PlaylistsPresenter
import javax.inject.Inject

class PlaylistsFragment :
    AbsLibraryPagerRecyclerViewFragment<PlaylistAdapter, GridLayoutManager>(), PlaylistView,
    MainActivityFragmentCallbacks {

    override fun handleBackPress(): Boolean {
        return false
    }

    @Inject
    lateinit var playlistsPresenter: PlaylistsPresenter

    override val emptyMessage: Int
        get() = R.string.no_playlists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsPresenter.attachView(this)
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireContext(), 1)
    }

    override fun createAdapter(): PlaylistAdapter {
        return PlaylistAdapter(
            mainActivity,
            ArrayList(),
            R.layout.item_list,
            mainActivity
        )
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isNullOrEmpty()) {
            playlistsPresenter.playlists()
        }
    }

    override fun onDestroyView() {
        playlistsPresenter.detachView()
        super.onDestroyView()
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        playlistsPresenter.playlists()
    }

    override fun showEmptyView() {
        adapter?.swapDataSet(ArrayList())
    }

    override fun playlists(playlists: List<Playlist>) {
        adapter?.swapDataSet(playlists)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            removeItem(R.id.action_sort_order)
            removeItem(R.id.action_grid_size)
        }
    }

    companion object {
        @JvmField
        val TAG: String = PlaylistsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): PlaylistsFragment {
            val args = Bundle()
            val fragment = PlaylistsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
