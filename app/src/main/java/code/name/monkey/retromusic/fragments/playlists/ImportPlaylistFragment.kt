package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.playlist.LegacyPlaylistAdapter
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.fragments.ReloadType.Playlists
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewFragment
import code.name.monkey.retromusic.model.Playlist
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ImportPlaylistFragment :
    AbsRecyclerViewFragment<LegacyPlaylistAdapter, LinearLayoutManager>(),
    LegacyPlaylistAdapter.PlaylistClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getLegacyPlaylist().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
                adapter?.swapData(it)
            else
                adapter?.swapData(listOf())
        })
    }

    override fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(requireContext())
    }

    override fun createAdapter(): LegacyPlaylistAdapter {
        return LegacyPlaylistAdapter(
            requireActivity(),
            ArrayList(),
            R.layout.item_list_no_image,
            this
        )
    }

    override fun onPlaylistClick(playlist: Playlist) {
        Toast.makeText(requireContext(), "Importing ${playlist.name}", Toast.LENGTH_LONG).show()
        lifecycleScope.launch(IO) {
            if (playlist.name.isNotEmpty()) {
                if (libraryViewModel.checkPlaylistExists(playlist.name).isEmpty()) {
                    val playlistId: Long =
                        libraryViewModel.createPlaylist(PlaylistEntity(playlistName = playlist.name))
                    libraryViewModel.insertSongs(playlist.getSongs().map {
                        it.toSongEntity(playlistId)
                    })
                    libraryViewModel.forceReload(Playlists)
                } else {
                    Toast.makeText(requireContext(), "Playlist exists", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.removeItem(R.id.action_sort_order)
        super.onCreateOptionsMenu(menu, inflater)
    }
}