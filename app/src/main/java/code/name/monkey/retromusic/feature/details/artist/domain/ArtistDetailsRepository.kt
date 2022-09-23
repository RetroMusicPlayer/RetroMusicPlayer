package code.name.monkey.retromusic.feature.details.artist.domain

import kotlinx.coroutines.flow.Flow

interface ArtistDetailsRepository {
    fun getArtistDetails(id: String): Flow<ArtistDetails>
    fun getArtistAlbums(id: String): Flow<List<ArtistDetailsAlbum>>
    fun getArtistSongs(id: String): Flow<List<ArtistDetailsSong>>
}