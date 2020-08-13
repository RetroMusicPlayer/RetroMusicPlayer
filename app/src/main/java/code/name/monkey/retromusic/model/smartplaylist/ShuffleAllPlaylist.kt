package code.name.monkey.retromusic.model.smartplaylist

import android.content.Context
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song

class ShuffleAllPlaylist(
    context: Context
) : AbsSmartPlaylist(context.getString(R.string.action_shuffle_all), R.drawable.ic_shuffle) {
    override fun songs(): List<Song> {
        return songRepository.songs()
    }
}