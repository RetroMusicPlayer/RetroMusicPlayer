package code.name.monkey.retromusic.feature.details.album.domain

import kotlinx.coroutines.flow.Flow

interface AlbumDetailsRepository {
    fun getAlbumDetails(id: String): Flow<AlbumDetails>
}