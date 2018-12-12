package code.name.monkey.retromusic.providers.interfaces;

import java.lang.System;

/**
 * * Created by hemanths on 11/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0016\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050\u00032\u0006\u0010$\u001a\u00020%H&J\u0016\u0010&\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\u0006\u0010\'\u001a\u00020(H&J\u001c\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00040\u00032\u0006\u0010*\u001a\u00020%H&J\u001c\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00040\u00032\u0006\u0010,\u001a\u00020\u000fH&J\u0016\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00120\u00032\u0006\u0010.\u001a\u00020%H&J\u001e\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u00040\u00032\b\u00100\u001a\u0004\u0018\u000101H&R\u001e\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u0007R\u001e\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0007R\u001e\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0007R\u001e\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0007R\u001e\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0007R\u001e\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0007R\u001e\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u0007R\u001e\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u0007R\u001e\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u0007R\u001e\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010\u0007R\u001e\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\"\u0010\u0007\u00a8\u00062"}, d2 = {"Lcode/name/monkey/retromusic/providers/interfaces/Repository;", "", "allAlbums", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Album;", "getAllAlbums", "()Lio/reactivex/Observable;", "allArtists", "Lcode/name/monkey/retromusic/model/Artist;", "getAllArtists", "allGenres", "Lcode/name/monkey/retromusic/model/Genre;", "getAllGenres", "allPlaylists", "Lcode/name/monkey/retromusic/model/Playlist;", "getAllPlaylists", "allSongs", "Lcode/name/monkey/retromusic/model/Song;", "getAllSongs", "allThings", "Lcode/name/monkey/retromusic/model/smartplaylist/AbsSmartPlaylist;", "getAllThings", "homeList", "getHomeList", "recentAlbums", "getRecentAlbums", "recentArtists", "getRecentArtists", "suggestionSongs", "getSuggestionSongs", "topAlbums", "getTopAlbums", "topArtists", "getTopArtists", "getAlbum", "albumId", "", "getArtistById", "artistId", "", "getGenre", "genreId", "getPlaylistSongs", "playlist", "getSong", "id", "search", "query", "", "app_normalDebug"})
public abstract interface Repository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getAllSongs();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getSuggestionSongs();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getAllAlbums();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getRecentAlbums();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getTopAlbums();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getAllArtists();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getRecentArtists();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getTopArtists();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Playlist>> getAllPlaylists();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Playlist>> getHomeList();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist>> getAllThings();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Genre>> getAllGenres();
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<code.name.monkey.retromusic.model.Song> getSong(int id);
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<code.name.monkey.retromusic.model.Album> getAlbum(int albumId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<code.name.monkey.retromusic.model.Artist> getArtistById(long artistId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<java.lang.Object>> search(@org.jetbrains.annotations.Nullable()
    java.lang.String query);
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getPlaylistSongs(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist playlist);
    
    @org.jetbrains.annotations.NotNull()
    public abstract io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getGenre(int genreId);
}