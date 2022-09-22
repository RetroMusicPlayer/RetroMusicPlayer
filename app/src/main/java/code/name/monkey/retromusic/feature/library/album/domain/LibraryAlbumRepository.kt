package code.name.monkey.retromusic.feature.library.album.domain

import kotlinx.coroutines.flow.Flow

interface LibraryAlbumRepository {
    fun getAlbums(): Flow<List<LibraryAlbum>>
}