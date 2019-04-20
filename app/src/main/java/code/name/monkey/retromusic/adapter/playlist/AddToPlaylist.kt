package code.name.monkey.retromusic.adapter.playlist

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.adapter.playlist.AddToPlaylist.ViewHolder
import code.name.monkey.retromusic.util.PlaylistsUtil

class AddToPlaylist(private val activity: Activity,
                    private val playlists: ArrayList<Playlist>, private val itemLayoutRes: Int,
                    private val songs: ArrayList<Song>, private val dialog: Dialog) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = playlists[position]
        if (holder.title != null) {
            holder.title!!.text = playlist.name
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        override fun onClick(v: View?) {
            super.onClick(v)
            PlaylistsUtil.addToPlaylist(activity, songs, playlists[adapterPosition].id, true)
            dialog.dismiss()
        }
    }
}
