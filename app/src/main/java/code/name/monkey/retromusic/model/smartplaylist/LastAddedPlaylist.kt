package code.name.monkey.retromusic.model.smartplaylist

import android.content.Context
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song

class LastAddedPlaylist(context: Context) :
    AbsSmartPlaylist(context.getString(R.string.last_added), R.drawable.ic_library_add) {
    override fun songs(): List<Song> {
        return lastAddedRepository.recentSongs()
    }
}