package code.name.monkey.retromusic.loaders;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0002J&\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002J\"\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00050\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010J\u001c\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002\u00a8\u0006\u0017"}, d2 = {"Lcode/name/monkey/retromusic/loaders/GenreLoader;", "", "()V", "getAllGenres", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Genre;", "context", "Landroid/content/Context;", "getGenreFromCursor", "cursor", "Landroid/database/Cursor;", "getGenresFromCursor", "getSongs", "Lcode/name/monkey/retromusic/model/Song;", "genreId", "", "getSongsWithNoGenre", "hasSongsWithNoGenre", "", "makeAllSongsWithGenreCursor", "makeGenreCursor", "makeGenreSongCursor", "app_normalDebug"})
public final class GenreLoader {
    public static final code.name.monkey.retromusic.loaders.GenreLoader INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Genre>> getAllGenres(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getSongs(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int genreId) {
        return null;
    }
    
    private final code.name.monkey.retromusic.model.Genre getGenreFromCursor(android.content.Context context, android.database.Cursor cursor) {
        return null;
    }
    
    private final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getSongsWithNoGenre(android.content.Context context) {
        return null;
    }
    
    private final boolean hasSongsWithNoGenre(android.content.Context context) {
        return false;
    }
    
    private final android.database.Cursor makeAllSongsWithGenreCursor(android.content.Context context) {
        return null;
    }
    
    private final android.database.Cursor makeGenreSongCursor(android.content.Context context, int genreId) {
        return null;
    }
    
    private final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Genre>> getGenresFromCursor(android.content.Context context, android.database.Cursor cursor) {
        return null;
    }
    
    private final android.database.Cursor makeGenreCursor(android.content.Context context) {
        return null;
    }
    
    private GenreLoader() {
        super();
    }
}