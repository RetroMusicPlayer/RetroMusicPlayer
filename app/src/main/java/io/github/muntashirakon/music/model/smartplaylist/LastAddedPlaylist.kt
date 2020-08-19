package io.github.muntashirakon.music.model.smartplaylist

import io.github.muntashirakon.music.App
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.model.Song
import kotlinx.android.parcel.Parcelize

@Parcelize
class LastAddedPlaylist :
    AbsSmartPlaylist(App.getContext().getString(R.string.last_added), R.drawable.ic_library_add) {
    override fun songs(): List<Song> {
        return lastAddedRepository.recentSongs()
    }
}