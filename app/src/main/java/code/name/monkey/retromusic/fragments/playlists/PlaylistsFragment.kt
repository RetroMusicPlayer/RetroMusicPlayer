package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.playlist.PlaylistAdapter
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewFragment
import kotlinx.android.synthetic.main.fragment_library.*

class PlaylistsFragment : AbsRecyclerViewFragment<PlaylistAdapter, GridLayoutManager>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getPlaylists().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
                adapter?.swapDataSet(it)
            else
                adapter?.swapDataSet(listOf())
        })
    }

    override val emptyMessage: Int
        get() = R.string.no_playlists

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireContext(), 1)
    }

    override fun createAdapter(): PlaylistAdapter {
        return PlaylistAdapter(
            requireActivity(),
            ArrayList(),
            R.layout.item_list,
            null
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.removeItem(R.id.action_sort_order)
        menu.add(0, R.id.action_add_to_playlist, 0, R.string.new_playlist_title)
            .setIcon(R.drawable.ic_playlist_add)
        menu.findItem(R.id.action_settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
