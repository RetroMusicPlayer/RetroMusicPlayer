package io.github.muntashirakon.music.model

import io.github.muntashirakon.music.repository.LastAddedRepository
import io.github.muntashirakon.music.repository.SongRepository
import io.github.muntashirakon.music.repository.TopPlayedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class AbsCustomPlaylist(
    id: Long,
    name: String
) : Playlist(id, name), KoinComponent {

    abstract fun songs(): List<Song>

    protected val songRepository by inject<SongRepository>()

    protected val topPlayedRepository by inject<TopPlayedRepository>()

    protected val lastAddedRepository by inject<LastAddedRepository>()
}