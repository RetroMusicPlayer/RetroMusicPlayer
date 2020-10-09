package code.name.monkey.retromusic.interfaces

import android.view.View
import code.name.monkey.retromusic.db.PlaylistWithSongs

interface IPlaylistClickListener {
    fun onPlaylistClick(playlistWithSongs: PlaylistWithSongs, view: View)
}