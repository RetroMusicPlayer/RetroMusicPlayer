package code.name.monkey.retromusic.loaders;

import java.lang.System;

/**
 * * Created by hemanths on 16/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00042\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0007J\u001e\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00050\u00042\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0007J\u001e\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00050\u00042\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0007J\u0014\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0002\u00a8\u0006\u000f"}, d2 = {"Lcode/name/monkey/retromusic/loaders/LastAddedSongsLoader;", "", "()V", "getLastAddedAlbums", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Album;", "context", "Landroid/content/Context;", "getLastAddedArtists", "Lcode/name/monkey/retromusic/model/Artist;", "getLastAddedSongs", "Lcode/name/monkey/retromusic/model/Song;", "makeLastAddedCursor", "Landroid/database/Cursor;", "app_normalDebug"})
public final class LastAddedSongsLoader {
    public static final code.name.monkey.retromusic.loaders.LastAddedSongsLoader INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getLastAddedSongs(@org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    android.content.Context context) {
        return null;
    }
    
    private final android.database.Cursor makeLastAddedCursor(@io.reactivex.annotations.NonNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getLastAddedAlbums(@org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Artist>> getLastAddedArtists(@org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    android.content.Context context) {
        return null;
    }
    
    private LastAddedSongsLoader() {
        super();
    }
}