package code.name.monkey.retromusic.model

import code.name.monkey.retromusic.repository.LastAddedRepository
import code.name.monkey.retromusic.repository.SongRepository
import code.name.monkey.retromusic.repository.TopPlayedRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class AbsCustomPlaylist(
    id: Long,
    name: String
) : Playlist(id, name), KoinComponent {

    abstract fun songs(): List<Song>

    protected val songRepository by inject<SongRepository>()

    protected val topPlayedRepository by inject<TopPlayedRepository>()

    protected val lastAddedRepository by inject<LastAddedRepository>()
}