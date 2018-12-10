package code.name.monkey.retromusic.providers

import android.content.Context
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.loaders.*
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.Observable
import java.util.*

class RepositoryImpl(private val context: Context) : Repository {

    override val allSongs: Observable<ArrayList<Song>>
        get() = SongLoader.getAllSongs(context)

    override val suggestionSongs: Observable<ArrayList<Song>>
        get() = SongLoader.suggestSongs(context)

    override val allAlbums: Observable<ArrayList<Album>>
        get() = AlbumLoader.getAllAlbums(context)

    override val recentAlbums: Observable<ArrayList<Album>>
        get() = LastAddedSongsLoader.getLastAddedAlbums(context)

    override val topAlbums: Observable<ArrayList<Album>>
        get() = TopAndRecentlyPlayedTracksLoader.getTopAlbums(context)

    override val allArtists: Observable<ArrayList<Artist>>
        get() = ArtistLoader.getAllArtists(context)

    override val recentArtists: Observable<ArrayList<Artist>>
        get() = LastAddedSongsLoader.getLastAddedArtists(context)

    override val topArtists: Observable<ArrayList<Artist>>
        get() = TopAndRecentlyPlayedTracksLoader.getTopArtists(context)

    override val allPlaylists: Observable<ArrayList<Playlist>>
        get() = PlaylistLoader.getAllPlaylists(context)

    override val homeList: Observable<ArrayList<Playlist>>
        get() = HomeLoader.getHomeLoader(context)

    override val allThings: Observable<ArrayList<AbsSmartPlaylist>>
        get() = HomeLoader.getRecentAndTopThings(context)

    override val allGenres: Observable<ArrayList<Genre>>
        get() = GenreLoader.getAllGenres(context)

    override fun getSong(id: Int): Observable<Song> {
        return SongLoader.getSong(context, id)
    }

    override fun getAlbum(albumId: Int): Observable<Album> {
        return AlbumLoader.getAlbum(context, albumId)
    }

    override fun getArtistById(artistId: Long): Observable<Artist> {
        return ArtistLoader.getArtist(context, artistId.toInt())
    }

    override fun search(query: String?): Observable<ArrayList<Any>> {
        return SearchLoader.searchAll(context, query)
    }

    override fun getPlaylistSongs(playlist: Playlist): Observable<ArrayList<Song>> {
        return PlaylistSongsLoader.getPlaylistSongList(context, playlist)
    }

    override fun getGenre(genreId: Int): Observable<ArrayList<Song>> {
        return GenreLoader.getSongs(context, genreId)
    }


    companion object {
        private var INSTANCE: RepositoryImpl? = null

        val instance: RepositoryImpl
            @Synchronized get() {
                if (INSTANCE == null) {
                    INSTANCE = RepositoryImpl(App.instance)
                }
                return INSTANCE!!
            }
    }
}
