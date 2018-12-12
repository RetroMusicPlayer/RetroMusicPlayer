package code.name.monkey.retromusic.loaders;

import java.lang.System;

/**
 * * Created by hemanths on 16/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\u0006\u0010\u0005\u001a\u00020\u0006J\u001c\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u001c\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0010J\u001c\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u0010\u0010\u0013\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J/\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0015\u001a\u0004\u0018\u00010\u00122\u000e\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u0017\u00a2\u0006\u0002\u0010\u0018\u00a8\u0006\u0019"}, d2 = {"Lcode/name/monkey/retromusic/loaders/PlaylistLoader;", "", "()V", "deletePlaylists", "", "context", "Landroid/content/Context;", "playlistId", "", "getAllPlaylists", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Playlist;", "cursor", "Landroid/database/Cursor;", "getPlaylist", "", "playlistName", "", "getPlaylistFromCursorImpl", "makePlaylistCursor", "selection", "values", "", "(Landroid/content/Context;Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;", "app_normalDebug"})
public final class PlaylistLoader {
    public static final code.name.monkey.retromusic.loaders.PlaylistLoader INSTANCE = null;
    
    @org.jetbrains.annotations.Nullable()
    public final android.database.Cursor makePlaylistCursor(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String selection, @org.jetbrains.annotations.Nullable()
    java.lang.String[] values) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<code.name.monkey.retromusic.model.Playlist> getPlaylist(@org.jetbrains.annotations.Nullable()
    android.database.Cursor cursor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<code.name.monkey.retromusic.model.Playlist> getPlaylist(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String playlistName) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<code.name.monkey.retromusic.model.Playlist> getPlaylist(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int playlistId) {
        return null;
    }
    
    private final code.name.monkey.retromusic.model.Playlist getPlaylistFromCursorImpl(android.database.Cursor cursor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Playlist>> getAllPlaylists(@org.jetbrains.annotations.Nullable()
    android.database.Cursor cursor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Playlist>> getAllPlaylists(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final void deletePlaylists(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long playlistId) {
    }
    
    private PlaylistLoader() {
        super();
    }
}