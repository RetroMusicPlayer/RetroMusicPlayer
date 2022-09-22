package code.name.monkey.retromusic.feature.library.album.data

import code.name.monkey.retromusic.feature.library.album.domain.LibraryAlbum
import code.name.monkey.retromusic.feature.library.album.domain.LibraryAlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.stersh.apisonic.ApiSonic
import ru.stersh.apisonic.models.AlbumList2

class LibraryAlbumRepositoryImpl(private val apiSonic: ApiSonic) : LibraryAlbumRepository {
    override fun getAlbums(): Flow<List<LibraryAlbum>> = flow {
        apiSonic
            .getAlbumList2(ApiSonic.ListType.ALPHABETICAL_BY_ARTIST, size = 500)
            .map { it.toLibraryAlbum() }
            .also { emit(it) }
    }

    private fun AlbumList2.Album.toLibraryAlbum(): LibraryAlbum {
        return LibraryAlbum(
            id = id,
            title = name,
            artist = artist,
            coverUrl = apiSonic.getCoverArtUrl(coverArt),
            year = year
        )
    }
}