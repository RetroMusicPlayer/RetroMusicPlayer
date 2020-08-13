package code.name.monkey.retromusic.model.smartplaylist

import android.content.Context
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song

class NotPlayedPlaylist(
    context: Context
) : AbsSmartPlaylist(context.getString(R.string.not_recently_played), R.drawable.ic_watch_later) {
    override fun songs(): List<Song> {
        return topPlayedRepository.notRecentlyPlayedTracks()
    }
}