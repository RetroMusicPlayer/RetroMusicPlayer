package code.name.monkey.retromusic.model.smartplaylist

import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShuffleAllPlaylist : AbsSmartPlaylist(
    App.getContext().getString(R.string.action_shuffle_all),
    R.drawable.ic_shuffle
) {
    override fun songs(): List<Song> {
        return songRepository.songs()
    }
}