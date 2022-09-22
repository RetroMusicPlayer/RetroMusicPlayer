package code.name.monkey.retromusic.feature.details.album.data

import code.name.monkey.retromusic.feature.details.album.domain.AlbumDetails
import code.name.monkey.retromusic.feature.details.album.domain.AlbumDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.stersh.apisonic.ApiSonic
import ru.stersh.apisonic.models.Album
import ru.stersh.apisonic.models.Artist
import ru.stersh.apisonic.models.Song

class AlbumDetailsRepositoryImpl(private val apiSonic: ApiSonic) : AlbumDetailsRepository {
    override fun getAlbumDetails(id: String): Flow<AlbumDetails> = flow {
        val album = apiSonic.getAlbum(id)
        val artist = apiSonic.getArtist(album.artistId)

        album
            .toDomain(artist.toDomain())
            .also { emit(it) }
    }

    private fun Album.toDomain(artist: AlbumDetails.Artist): AlbumDetails {
        return AlbumDetails(
            id = id,
            title = name,
            artist = artist,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt),
            year = year,
            songs = song?.map { it.toDomain() } ?: emptyList()
        )
    }

    private fun Artist.toDomain(): AlbumDetails.Artist {
        return AlbumDetails.Artist(
            id = id,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt),
            title = name
        )
    }

    private fun Song.toDomain(): AlbumDetails.Song {
        return AlbumDetails.Song(
            id = id,
            albumId = albumId,
            coverArtUrl = apiSonic.getCoverArtUrl(coverArt),
            year = year,
            title = title,
            artist = artist,
            album = album
        )
    }
}