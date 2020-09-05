package code.name.monkey.retromusic.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.fragments.ReloadType.Playlists
import code.name.monkey.retromusic.fragments.base.AbsRecyclerViewFragment
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.util.MusicUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ImportPlaylistFragment :
    AbsRecyclerViewFragment<LegacyPlaylistAdapter, LinearLayoutManager>(), PlaylistClickListener {

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
                        libraryViewModel.createPlaylist(PlaylistEntity(playlist.name))
                    libraryViewModel.insertSongs(playlist.getSongs().map {
                        it.toSongEntity(playlistId.toInt())
                    })
                    libraryViewModel.forceReload(Playlists)
                } else {
                    Toast.makeText(requireContext(), "Playlist exists", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

class LegacyPlaylistAdapter(
    private val activity: FragmentActivity,
    private var list: List<Playlist>,
    private val layoutRes: Int,
    private val playlistClickListener: PlaylistClickListener
) :
    RecyclerView.Adapter<LegacyPlaylistAdapter.ViewHolder>() {

    fun swapData(list: List<Playlist>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist: Playlist = list[position]
        holder.title?.text = playlist.name
        holder.text?.text = MusicUtil.getPlaylistInfoString(activity, playlist.getSongs())
        holder.itemView.setOnClickListener {
            playlistClickListener.onPlaylistClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface PlaylistClickListener {
    fun onPlaylistClick(playlist: Playlist)
}