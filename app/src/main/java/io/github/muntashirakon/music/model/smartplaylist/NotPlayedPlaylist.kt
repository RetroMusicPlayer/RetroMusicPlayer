package io.github.muntashirakon.music.model.smartplaylist

import io.github.muntashirakon.music.App
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.model.Song
import kotlinx.android.parcel.Parcelize

@Parcelize
class NotPlayedPlaylist : AbsSmartPlaylist(
    App.getContext().getString(R.string.not_recently_played),
    R.drawable.ic_watch_later
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.notRecentlyPlayedTracks()
    }
}