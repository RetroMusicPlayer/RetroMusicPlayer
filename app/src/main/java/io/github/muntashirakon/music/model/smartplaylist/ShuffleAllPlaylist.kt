package io.github.muntashirakon.music.model.smartplaylist

import io.github.muntashirakon.music.App
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.model.Song
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