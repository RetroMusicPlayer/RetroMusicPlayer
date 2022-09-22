package ru.stersh.retrosonic.core.storage.domain

import kotlinx.coroutines.flow.Flow

interface PlayQueueStorage {
    fun getCurrentSong(): Flow<PlayableSong?>
    fun playSource(source: AudioSource)
    fun addSource(source: AudioSource)
}