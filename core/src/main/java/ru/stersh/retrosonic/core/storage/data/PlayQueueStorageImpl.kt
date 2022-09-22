package ru.stersh.retrosonic.core.storage.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.apisonic.ApiSonic
import ru.stersh.apisonic.models.Song
import ru.stersh.retrosonic.core.storage.domain.AudioSource
import ru.stersh.retrosonic.core.storage.domain.PlayQueueStorage
import ru.stersh.retrosonic.core.storage.domain.PlayableSong

internal class PlayQueueStorageImpl(
    private val scope: CoroutineScope,
    private val apiSonic: ApiSonic
) : PlayQueueStorage, CoroutineScope by scope {

    private val currentSong = MutableStateFlow<PlayableSong?>(null)
    private val queue = MutableStateFlow<List<PlayableSong>>(emptyList())
    private val mutex = Mutex()

    override fun getCurrentSong(): Flow<PlayableSong?> = currentSong

    override fun playSource(source: AudioSource) {
        launch {
            when (source) {
                is AudioSource.Song -> {
                    val song = getSong(source.id)
                    mutex.withLock {
                        queue.value = listOf(song)
                        currentSong.value = song
                    }
                }
                is AudioSource.Album -> {

                }
                is AudioSource.Artist -> {

                }
            }
        }
    }

    override fun addSource(source: AudioSource) {
        launch {
            when (source) {
                is AudioSource.Song -> {
                    val song = getSong(source.id)
                    mutex.withLock {
                        val queueSongs = queue.value
                        queue.value = queueSongs + song
                    }
                }
                is AudioSource.Album -> {

                }
                is AudioSource.Artist -> {

                }
            }
        }
    }

    private suspend fun getSong(id: String): PlayableSong {
        return apiSonic
            .getSong(id)
            .toDomain()
    }

    private fun Song.toDomain(): PlayableSong {
        return PlayableSong(
            id = id,
            title = title,
            artist = artist,
            album = album,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt)
        )
    }
}