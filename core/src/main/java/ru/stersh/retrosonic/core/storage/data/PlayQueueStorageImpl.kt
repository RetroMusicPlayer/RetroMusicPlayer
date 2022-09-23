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
            val newQueue = when (source) {
                is AudioSource.Song -> listOf(getSong(source))
                is AudioSource.Album -> getSongs(source)
                is AudioSource.Artist -> getSongs(source)
            }
            if (newQueue.isNotEmpty()) {
                mutex.withLock {
                    queue.value = newQueue
                    currentSong.value = newQueue[0]
                }
            }
        }
    }

    override fun addSource(source: AudioSource) {
        launch {
            val newSongs = when (source) {
                is AudioSource.Song -> listOf(getSong(source))
                is AudioSource.Album -> getSongs(source)
                is AudioSource.Artist -> getSongs(source)
            }
            if (newSongs.isNotEmpty()) {
                mutex.withLock {
                    val queueSongs = queue.value + newSongs
                    queue.value = queueSongs
                    if (currentSong.value == null) {
                        currentSong.value = queueSongs[0]
                    }
                }
            }
        }
    }

    private suspend fun getSong(source: AudioSource.Song): PlayableSong {
        val starred = getStarredSongIds()
        return apiSonic
            .getSong(source.id)
            .toDomain(source.id in starred)
    }

    private suspend fun getSongs(source: AudioSource.Album): List<PlayableSong> {
        val songs = apiSonic
            .getAlbum(source.id)
            .song
            ?.takeIf { it.isNotEmpty() }
            ?: return emptyList()

        val starred = getStarredSongIds()
        return songs.map { song ->
            song.toDomain(song.id in starred)
        }
    }

    private suspend fun getSongs(source: AudioSource.Artist): List<PlayableSong> {
        val albums = apiSonic
            .getArtist(source.id)
            .albums
            ?.takeIf { it.isNotEmpty() }
            ?: return emptyList()

        val songs = albums.flatMap { it.song ?: emptyList() }

        return if (songs.isNotEmpty()) {
            val starred = getStarredSongIds()

            songs.map { it.toDomain(it.id in starred) }
        } else {
            emptyList()
        }
    }

    private suspend fun getStarredSongIds(): List<String> {
        return apiSonic
            .getStarred2()
            .song
            ?.map { it.id }
            ?: emptyList()
    }

    private fun Song.toDomain(isFavorite: Boolean): PlayableSong {
        return PlayableSong(
            id = id,
            title = title,
            artist = artist,
            album = album,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt),
            isFavorite = isFavorite
        )
    }
}