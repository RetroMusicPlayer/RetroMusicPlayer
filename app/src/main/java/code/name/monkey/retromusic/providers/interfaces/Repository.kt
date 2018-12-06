package code.name.monkey.retromusic.providers.interfaces

import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist
import io.reactivex.Observable
import java.io.File
import java.util.ArrayList

/**
 * Created by hemanths on 11/08/17.
 */

interface Repository {

    val allSongs: Observable<ArrayList<Song>>

    val suggestionSongs: Observable<ArrayList<Song>>

    val allAlbums: Observable<ArrayList<Album>>

    val recentAlbums: Observable<ArrayList<Album>>

    val topAlbums: Observable<ArrayList<Album>>

    val allArtists: Observable<ArrayList<Artist>>

    val recentArtists: Observable<ArrayList<Artist>>

    val topArtists: Observable<ArrayList<Artist>>

    val allPlaylists: Observable<ArrayList<Playlist>>

    val favoriteSongs: Observable<ArrayList<Song>>

    val homeList: Observable<ArrayList<Playlist>>

    val allThings: Observable<ArrayList<AbsSmartPlaylist>>

    val allGenres: Observable<ArrayList<Genre>>

    fun getSong(id: Int): Observable<Song>

    fun getAlbum(albumId: Int): Observable<Album>


    fun getArtistById(artistId: Long): Observable<Artist>

    fun search(query: String): Observable<ArrayList<Any>>

    fun getPlaylistSongs(playlist: Playlist): Observable<ArrayList<Song>>

    fun getGenre(genreId: Int): Observable<ArrayList<Song>>

    fun downloadLrcFile(title: String, artist: String, duration: Long): Observable<File>

}