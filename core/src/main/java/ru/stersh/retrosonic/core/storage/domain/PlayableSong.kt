package ru.stersh.retrosonic.core.storage.domain

data class PlayableSong(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val coverArtUrl: String,
    val isFavorite: Boolean
)