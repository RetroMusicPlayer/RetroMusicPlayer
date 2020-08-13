package code.name.monkey.retromusic.model.smartplaylist

import android.content.Context
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song

class TopTracksPlaylist(
    context: Context
) : AbsSmartPlaylist(
    context.getString(R.string.my_top_tracks),
    R.drawable.ic_trending_up
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.topTracks()
    }
}