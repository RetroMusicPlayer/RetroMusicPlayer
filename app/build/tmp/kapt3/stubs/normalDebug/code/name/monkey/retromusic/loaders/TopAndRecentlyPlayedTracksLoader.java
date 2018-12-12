package code.name.monkey.retromusic.loaders;

import java.lang.System;

/**
 * * Created by hemanths on 16/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0007\u001a\u00020\bH\u0002J$\u0010\u0012\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a8\u0006\u0018"}, d2 = {"Lcode/name/monkey/retromusic/loaders/TopAndRecentlyPlayedTracksLoader;", "", "()V", "getRecentlyPlayedTracks", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "context", "Landroid/content/Context;", "getTopAlbums", "Lcode/name/monkey/retromusic/model/Album;", "getTopArtists", "Lcode/name/monkey/retromusic/model/Artist;", "getTopTracks", "makeRecentTracksCursorAndClearUpDatabase", "Landroid/database/Cursor;", "makeRecentTracksCursorImpl", "Lcode/name/monkey/retromusic/loaders/SortedLongCursor;", "makeSortedCursor", "cursor", "idColumn", "", "makeTopTracksCursorAndClearUpDatabase", "makeTopTracksCursorImpl", "app_normalDebug"})
public final class TopAndRecentlyPlayedTracksLoader {
    public static final code.name.monkey.retromusic.loaders.TopAndRecentlyPlayedTracksLoader INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getRecentlyPlayedTracks(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getTopTracks(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private final android.database.Cursor makeRecentTracksCursorAndClearUpDatabase(android.content.Context context) {
        return null;
    }
    
    private final android.database.Cursor makeTopTracksCursorAndClearUpDatabase(android.content.Context context) {
        return null;
    }
    
    private final code.name.monkey.retromusic.loaders.SortedLongCursor makeRecentTracksCursorImpl(android.content.Context context) {
        return null;
    }
    
    private final code.name.monkey.retromusic.loaders.SortedLongCursor makeTopTracksCursorImpl(android.content.Context context) {
        return null;
    }
    
    private final code.name.monkey.retromusic.loaders.SortedLongCursor makeSortedCursor(android.content.Context context, android.database.Cursor cursor, int idColumn) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getTopAlbums(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getTopArtists(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private TopAndRecentlyPlayedTracksLoader() {
        super();
    }
}