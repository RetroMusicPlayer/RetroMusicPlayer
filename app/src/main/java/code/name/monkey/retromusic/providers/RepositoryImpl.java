package code.name.monkey.retromusic.providers;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import code.name.monkey.retromusic.Injection;
import code.name.monkey.retromusic.App;
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
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist;
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

    public static synchronized RepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryImpl(App.Companion.getInstance());
        }
        return INSTANCE;
    }

    @Override
    public Observable<ArrayList<Song>> getAllSongs() {
        return SongLoader.INSTANCE.getAllSongs(context);
    }

    @Override
    public Observable<ArrayList<Song>> getSuggestionSongs() {
        return SongLoader.INSTANCE.suggestSongs(context);
    }

    @Override
    public Observable<Song> getSong(int id) {
        return SongLoader.INSTANCE.getSong(context, id);
    }

    @Override
    public Observable<ArrayList<Album>> getAllAlbums() {
        return AlbumLoader.Companion.getAllAlbums(context);
    }

    @Override
    public Observable<ArrayList<Album>> getRecentAlbums() {
        return LastAddedSongsLoader.INSTANCE.getLastAddedAlbums(context);
    }

    @Override
    public Observable<ArrayList<Album>> getTopAlbums() {
        return TopAndRecentlyPlayedTracksLoader.INSTANCE.getTopAlbums(context);
    }

    @Override
    public Observable<Album> getAlbum(int albumId) {
        return AlbumLoader.Companion.getAlbum(context, albumId);
    }

    @Override
    public Observable<ArrayList<Artist>> getAllArtists() {
        return ArtistLoader.INSTANCE.getAllArtists(context);
    }

    @Override
    public Observable<ArrayList<Artist>> getRecentArtists() {
        return LastAddedSongsLoader.INSTANCE.getLastAddedArtists(context);
    }

    @Override
    public Observable<ArrayList<Artist>> getTopArtists() {
        return TopAndRecentlyPlayedTracksLoader.INSTANCE.getTopArtists(context);
    }

    @Override
    public Observable<Artist> getArtistById(long artistId) {
        return ArtistLoader.INSTANCE.getArtist(context, (int) artistId);
    }

    @Override
    public Observable<ArrayList<Playlist>> getAllPlaylists() {
        return PlaylistLoader.INSTANCE.getAllPlaylists(context);
    }

    @Override
    public Observable<ArrayList<Song>> getFavoriteSongs() {
        return null;
    }

    @Override
    public Observable<ArrayList<Object>> search(String query) {
        return SearchLoader.INSTANCE.searchAll(context, query);
    }

    @Override
    public Observable<ArrayList<Song>> getPlaylistSongs(Playlist playlist) {
        return PlaylistSongsLoader.INSTANCE.getPlaylistSongList(context, playlist);
    }

    @Override
    public Observable<ArrayList<Playlist>> getHomeList() {
        return HomeLoader.INSTANCE.getHomeLoader(context);
    }

    @Override
    public Observable<ArrayList<AbsSmartPlaylist>> getAllThings() {
        return HomeLoader.INSTANCE.getRecentAndTopThings(context);
    }

    @Override
    public Observable<ArrayList<Genre>> getAllGenres() {
        return GenreLoader.INSTANCE.getAllGenres(context);
    }

    @Override
    public Observable<ArrayList<Song>> getGenre(int genreId) {
        return GenreLoader.INSTANCE.getSongs(context, genreId);
    }

    @Override
    public Observable<File> downloadLrcFile(String title, String artist, long duration) {
        KuGouApiService service = Injection.INSTANCE.provideKuGouApiService();
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
