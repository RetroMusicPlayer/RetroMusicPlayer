package code.name.monkey.retromusic.providers;

import android.content.Context;

import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist;
import java.io.File;
import java.util.ArrayList;

import code.name.monkey.retromusic.Injection;
import code.name.monkey.retromusic.loaders.AlbumLoader;
import code.name.monkey.retromusic.loaders.ArtistLoader;
import code.name.monkey.retromusic.loaders.GenreLoader;
import code.name.monkey.retromusic.loaders.HomeLoader;
import code.name.monkey.retromusic.loaders.LastAddedSongsLoader;
import code.name.monkey.retromusic.loaders.PlaylistLoader;
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.loaders.SearchLoader;
import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.loaders.TopAndRecentlyPlayedTracksLoader;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.providers.interfaces.Repository;
import code.name.monkey.retromusic.rest.model.KuGouRawLyric;
import code.name.monkey.retromusic.rest.model.KuGouSearchLyricResult;
import code.name.monkey.retromusic.rest.service.KuGouApiService;
import code.name.monkey.retromusic.util.LyricUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RepositoryImpl implements Repository {
    private static RepositoryImpl INSTANCE;
    private Context context;

    public RepositoryImpl(Context context) {
        this.context = context;
    }

    public static synchronized RepositoryImpl getInstance( ) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryImpl(RetroApplication.getInstance());
        }
        return INSTANCE;
    }

    @Override
    public Observable<ArrayList<Song>> getAllSongs() {
        return SongLoader.getAllSongs(context);
    }

    @Override
    public Observable<ArrayList<AbsSmartPlaylist>> getSuggestionSongs() {
        return HomeLoader.getRecentAndTopThings(context);
    }

    @Override
    public Observable<Song> getSong(int id) {
        return SongLoader.getSong(context, id);
    }

    @Override
    public Observable<ArrayList<Album>> getAllAlbums() {
        return AlbumLoader.getAllAlbums(context);
    }

    @Override
    public Observable<ArrayList<Album>> getRecentAlbums() {
        return LastAddedSongsLoader.getLastAddedAlbums(context);
    }

    @Override
    public Observable<ArrayList<Album>> getTopAlbums() {
        return TopAndRecentlyPlayedTracksLoader.getTopAlbums(context);
    }

    @Override
    public Observable<Album> getAlbum(int albumId) {
        return AlbumLoader.getAlbum(context, albumId);
    }

    @Override
    public Observable<ArrayList<Artist>> getAllArtists() {
        return ArtistLoader.getAllArtists(context);
    }

    @Override
    public Observable<ArrayList<Artist>> getRecentArtists() {
        return LastAddedSongsLoader.getLastAddedArtists(context);
    }

    @Override
    public Observable<ArrayList<Artist>> getTopArtists() {
        return TopAndRecentlyPlayedTracksLoader.getTopArtists(context);
    }

    @Override
    public Observable<Artist> getArtistById(long artistId) {
        return ArtistLoader.getArtist(context, (int) artistId);
    }

    @Override
    public Observable<ArrayList<Playlist>> getAllPlaylists() {
        return PlaylistLoader.getAllPlaylists(context);
    }

    @Override
    public Observable<ArrayList<Song>> getFavoriteSongs() {
        return null;
    }

    @Override
    public Observable<ArrayList<Object>> search(String query) {
        return SearchLoader.searchAll(context, query);
    }

    @Override
    public Observable<ArrayList<Song>> getPlaylistSongs(Playlist playlist) {
        return PlaylistSongsLoader.getPlaylistSongList(context, playlist);
    }

    @Override
    public Observable<ArrayList<Playlist>> getHomeList() {
        return HomeLoader.getHomeLoader(context);
    }

    @Override
    public Observable<ArrayList<AbsSmartPlaylist>> getAllThings() {
        return HomeLoader.getRecentAndTopThings(context);
    }

    @Override
    public Observable<ArrayList<Genre>> getAllGenres() {
        return GenreLoader.getAllGenres(context);
    }

    @Override
    public Observable<ArrayList<Song>> getGenre(int genreId) {
        return GenreLoader.getSongs(context, genreId);
    }

    @Override
    public Observable<File> downloadLrcFile(String title, String artist, long duration) {
        KuGouApiService service = Injection.provideKuGouApiService();
        return service.searchLyric(title, String.valueOf(duration))
                .subscribeOn(Schedulers.io())
                .flatMap(kuGouSearchLyricResult -> {
                    if (kuGouSearchLyricResult.status == 200
                            && kuGouSearchLyricResult.candidates != null
                            && kuGouSearchLyricResult.candidates.size() != 0) {
                        KuGouSearchLyricResult.Candidates candidates = kuGouSearchLyricResult.candidates.get(0);
                        return service.getRawLyric(candidates.id, candidates.accesskey);
                    } else {
                        return Observable.just(new KuGouRawLyric());
                    }
                }).map(kuGouRawLyric -> {
                    if (kuGouRawLyric == null) {
                        return null;
                    }
                    String rawLyric = LyricUtil.decryptBASE64(kuGouRawLyric.content);
                    if (rawLyric != null && rawLyric.isEmpty()) {
                        return null;
                    }
                    return LyricUtil.writeLrcToLoc(title, artist, rawLyric);
                });
    }
}
