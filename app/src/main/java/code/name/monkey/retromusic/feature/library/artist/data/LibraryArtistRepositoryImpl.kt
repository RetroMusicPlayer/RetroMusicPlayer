package code.name.monkey.retromusic.feature.library.artist.data

import code.name.monkey.retromusic.feature.library.artist.domain.LibraryArtist
import code.name.monkey.retromusic.feature.library.artist.domain.LibraryArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.stersh.apisonic.ApiSonic
import ru.stersh.apisonic.models.Artist

class LibraryArtistRepositoryImpl(private val apiSonic: ApiSonic) : LibraryArtistRepository {
    override fun getArtists(): Flow<List<LibraryArtist>> = flow {
        apiSonic
            .getArtists()
            .asArtistList()
            .map { it.toDomain() }
            .also { emit(it) }
    }

    private fun Artist.toDomain(): LibraryArtist {
        return LibraryArtist(
            id = id,
            title = name,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt)
        )
    }
}