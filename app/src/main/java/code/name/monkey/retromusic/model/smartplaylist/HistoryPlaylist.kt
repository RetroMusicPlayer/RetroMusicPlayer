package code.name.monkey.retromusic.model.smartplaylist

import android.content.Context
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import org.koin.core.KoinComponent

class HistoryPlaylist(
    context: Context
) : AbsSmartPlaylist(context.getString(R.string.history), R.drawable.ic_history), KoinComponent {

    override fun songs(): List<Song> {
        return topPlayedRepository.recentlyPlayedTracks()
    }
}