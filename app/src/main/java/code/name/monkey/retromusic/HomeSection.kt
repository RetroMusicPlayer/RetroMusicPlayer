package code.name.monkey.retromusic

import androidx.annotation.IntDef

@IntDef(
    RECENT_ALBUMS,
    TOP_ALBUMS,
    RECENT_ARTISTS,
    TOP_ARTISTS,
    SUGGESTIONS,
    FAVOURITES,
    GENRES,
    PLAYLISTS
)
@Retention(AnnotationRetention.SOURCE)
annotation class HomeSection

const val RECENT_ALBUMS = 3
const val TOP_ALBUMS = 1
const val RECENT_ARTISTS = 2
const val TOP_ARTISTS = 0
const val SUGGESTIONS = 5
const val FAVOURITES = 4
const val GENRES = 6
const val PLAYLISTS = 7
const val HISTORY_PLAYLIST = 8
const val LAST_ADDED_PLAYLIST = 9
const val TOP_PLAYED_PLAYLIST = 10
