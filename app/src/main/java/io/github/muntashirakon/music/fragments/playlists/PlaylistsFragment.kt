package io.github.muntashirakon.music.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.adapter.playlist.PlaylistAdapter
import io.github.muntashirakon.music.fragments.base.AbsRecyclerViewFragment
import io.github.muntashirakon.music.interfaces.MainActivityFragmentCallbacks

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
