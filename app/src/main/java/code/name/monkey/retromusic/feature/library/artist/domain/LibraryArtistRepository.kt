package code.name.monkey.retromusic.feature.library.artist.domain

import kotlinx.coroutines.flow.Flow

interface LibraryArtistRepository {
    fun getArtists(): Flow<List<LibraryArtist>>
}