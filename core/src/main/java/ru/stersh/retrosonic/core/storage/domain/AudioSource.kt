package ru.stersh.retrosonic.core.storage.domain

sealed class AudioSource(open val id: String) {
    data class Song(override val id: String) : AudioSource(id)
    data class Album(override val id: String) : AudioSource(id)
    data class Artist(override val id: String) : AudioSource(id)
}
