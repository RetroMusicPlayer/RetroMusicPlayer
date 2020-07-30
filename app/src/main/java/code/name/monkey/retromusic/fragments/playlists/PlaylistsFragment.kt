package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.playlist.PlaylistAdapter
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewFragment
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks

class PlaylistsFragment :
    AbsRecyclerViewFragment<PlaylistAdapter, GridLayoutManager>() ,
    MainActivityFragmentCallbacks {

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.playlisitsLiveData.observe(viewLifecycleOwner, Observer { playlists ->
            if (playlists.isNotEmpty()) {
                adapter?.swapDataSet(playlists)
            } else {
                adapter?.swapDataSet(listOf())
            }
        })
    }

    override val emptyMessage: Int
        get() = R.string.no_playlists

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
            return PlaylistsFragment()
        }
    }
}
