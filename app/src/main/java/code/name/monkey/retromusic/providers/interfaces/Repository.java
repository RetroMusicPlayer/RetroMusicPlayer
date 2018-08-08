package code.name.monkey.retromusic.providers.interfaces;

import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist;
import io.reactivex.Observable;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by hemanths on 11/08/17.
 */

public interface Repository {

    Observable<ArrayList<Song>> getAllSongs();

    Observable<ArrayList<AbsSmartPlaylist>> getSuggestionSongs();

    Observable<Song> getSong(int id);

    Observable<ArrayList<Album>> getAllAlbums();

    Observable<ArrayList<Album>> getRecentAlbums();

    Observable<ArrayList<Album>> getTopAlbums();

    Observable<Album> getAlbum(int albumId);

    Observable<ArrayList<Artist>> getAllArtists();

    Observable<ArrayList<Artist>> getRecentArtists();

    Observable<ArrayList<Artist>> getTopArtists();


    Observable<Artist> getArtistById(long artistId);

    Observable<ArrayList<Playlist>> getAllPlaylists();

    Observable<ArrayList<Song>> getFavoriteSongs();

    Observable<ArrayList<Object>> search(String query);

    Observable<ArrayList<Song>> getPlaylistSongs(Playlist playlist);

    Observable<ArrayList<Playlist>> getHomeList();

    Observable<ArrayList<AbsSmartPlaylist>> getAllThings();

    Observable<ArrayList<Genre>> getAllGenres();

    Observable<ArrayList<Song>> getGenre(int genreId);

    Observable<File> downloadLrcFile(final String title, final String artist, final long duration);

}