package code.name.monkey.retromusic.providers;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 62\u00020\u0001:\u00016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010&\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010\'\u001a\u00020(H\u0016J\u0016\u0010)\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\u0006\u0010*\u001a\u00020+H\u0016J\u001c\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00070\u00062\u0006\u0010-\u001a\u00020(H\u0016J\u001c\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00070\u00062\u0006\u0010/\u001a\u00020\u0012H\u0016J\u0016\u00100\u001a\b\u0012\u0004\u0012\u00020\u00150\u00062\u0006\u00101\u001a\u00020(H\u0016J\u001e\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030\u00070\u00062\b\u00104\u001a\u0004\u0018\u000105H\u0016R \u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR \u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\nR \u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\nR \u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\nR \u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\nR \u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\nR \u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\nR \u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\nR \u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\nR \u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010\nR \u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b%\u0010\n\u00a8\u00067"}, d2 = {"Lcode/name/monkey/retromusic/providers/RepositoryImpl;", "Lcode/name/monkey/retromusic/providers/interfaces/Repository;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "allAlbums", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Album;", "getAllAlbums", "()Lio/reactivex/Observable;", "allArtists", "Lcode/name/monkey/retromusic/model/Artist;", "getAllArtists", "allGenres", "Lcode/name/monkey/retromusic/model/Genre;", "getAllGenres", "allPlaylists", "Lcode/name/monkey/retromusic/model/Playlist;", "getAllPlaylists", "allSongs", "Lcode/name/monkey/retromusic/model/Song;", "getAllSongs", "allThings", "Lcode/name/monkey/retromusic/model/smartplaylist/AbsSmartPlaylist;", "getAllThings", "homeList", "getHomeList", "recentAlbums", "getRecentAlbums", "recentArtists", "getRecentArtists", "suggestionSongs", "getSuggestionSongs", "topAlbums", "getTopAlbums", "topArtists", "getTopArtists", "getAlbum", "albumId", "", "getArtistById", "artistId", "", "getGenre", "genreId", "getPlaylistSongs", "playlist", "getSong", "id", "search", "", "query", "", "Companion", "app_normalDebug"})
public final class RepositoryImpl implements code.name.monkey.retromusic.providers.interfaces.Repository {
    private final android.content.Context context = null;
    private static code.name.monkey.retromusic.providers.RepositoryImpl INSTANCE;
    public static final code.name.monkey.retromusic.providers.RepositoryImpl.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getAllSongs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getSuggestionSongs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getAllAlbums() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getRecentAlbums() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getTopAlbums() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getAllArtists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getRecentArtists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getTopArtists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Playlist>> getAllPlaylists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Playlist>> getHomeList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist>> getAllThings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Genre>> getAllGenres() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<code.name.monkey.retromusic.model.Song> getSong(int id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<code.name.monkey.retromusic.model.Album> getAlbum(int albumId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<code.name.monkey.retromusic.model.Artist> getArtistById(long artistId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<java.lang.Object>> search(@org.jetbrains.annotations.Nullable()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getPlaylistSongs(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist playlist) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getGenre(int genreId) {
        return null;
    }
    
    public RepositoryImpl(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/providers/RepositoryImpl$Companion;", "", "()V", "INSTANCE", "Lcode/name/monkey/retromusic/providers/RepositoryImpl;", "instance", "getInstance", "()Lcode/name/monkey/retromusic/providers/RepositoryImpl;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final synchronized code.name.monkey.retromusic.providers.RepositoryImpl getInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}