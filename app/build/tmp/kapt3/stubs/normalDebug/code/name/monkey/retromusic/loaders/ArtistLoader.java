package code.name.monkey.retromusic.loaders;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u001c\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bJ\"\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eJ\u001e\u0010\u000f\u001a\u00020\u00062\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\u0011\u001a\u00020\u000eH\u0002J&\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0012\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00050\u0004J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0005\u00a8\u0006\u0015"}, d2 = {"Lcode/name/monkey/retromusic/loaders/ArtistLoader;", "", "()V", "getAllArtists", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Artist;", "context", "Landroid/content/Context;", "getArtist", "artistId", "", "getArtists", "query", "", "getOrCreateArtist", "artists", "getSongLoaderSortOrder", "splitIntoArtists", "albums", "Lcode/name/monkey/retromusic/model/Album;", "app_normalDebug"})
public final class ArtistLoader {
    public static final code.name.monkey.retromusic.loaders.ArtistLoader INSTANCE = null;
    
    private final java.lang.String getSongLoaderSortOrder() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<code.name.monkey.retromusic.model.Artist> getArtist(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int artistId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getAllArtists(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getArtists(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Artist> splitIntoArtists(@org.jetbrains.annotations.Nullable()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> albums) {
        return null;
    }
    
    private final code.name.monkey.retromusic.model.Artist getOrCreateArtist(java.util.ArrayList<code.name.monkey.retromusic.model.Artist> artists, int artistId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> splitIntoArtists(@org.jetbrains.annotations.NotNull()
    io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> albums) {
        return null;
    }
    
    private ArtistLoader() {
        super();
    }
}