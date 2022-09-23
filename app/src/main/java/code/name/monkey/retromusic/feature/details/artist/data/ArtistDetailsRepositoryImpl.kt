package code.name.monkey.retromusic.feature.details.artist.data

import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetails
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsAlbum
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsRepository
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsSong
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.stersh.apisonic.ApiSonic
import ru.stersh.apisonic.models.Album
import ru.stersh.apisonic.models.Song

class ArtistDetailsRepositoryImpl(private val apiSonic: ApiSonic) : ArtistDetailsRepository {
    override fun getArtistDetails(id: String): Flow<ArtistDetails> = flow {
        val artist = apiSonic.getArtist(id)
        val artistInfo = apiSonic.getArtistInfo2(id)

        ArtistDetails(
            title = artist.name,
            coverArtUrl = apiSonic.getCoverArtUrl(artist.coverArt),
            biography = artistInfo.biography,
            albumCount = artist.albumCount,
            songCount = artist.albums?.sumOf { it.songCount } ?: 0,
            totalDuration = (artist.albums?.sumOf { it.duration } ?: 0) * 1000L
        ).also { emit(it) }
    }

    override fun getArtistAlbums(id: String): Flow<List<ArtistDetailsAlbum>> = flow {
        getAlbums(id)
            .map { it.toDomain() }
            .also { emit(it) }
    }

    override fun getArtistSongs(id: String): Flow<List<ArtistDetailsSong>> = flow {
        getSongs(id)
            .map { it.toDomain() }
            .also { emit(it) }
    }

    private suspend fun getSongs(artistId: String): List<Song> = coroutineScope {
        return@coroutineScope getAlbums(artistId).map {
            async { apiSonic.getAlbum(it.id).song ?: emptyList() }
        }
            .awaitAll()
            .flatten()
    }

    private suspend fun getAlbums(artistId: String): List<Album> {
        return apiSonic
            .getArtist(artistId)
            .albums ?: emptyList()
    }

    private fun Album.toDomain(): ArtistDetailsAlbum {
        return ArtistDetailsAlbum(
            id = id,
            title = name,
            artist = artist,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt),
            year = year,
        )
    }

    private fun Song.toDomain(): ArtistDetailsSong {
        return ArtistDetailsSong(
            id = id,
            title = title,
            artist = artist,
            album = album,
            albumId = albumId,
            track = track,
            duration = duration * 1000L,
            year = year,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt)
        )
    }
}