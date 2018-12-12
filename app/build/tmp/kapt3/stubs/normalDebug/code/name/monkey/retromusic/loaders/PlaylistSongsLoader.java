package code.name.monkey.retromusic.loaders;

import java.lang.System;

/**
 * * Created by hemanths on 16/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0003J&\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\b\b\u0001\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J&\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\b\b\u0001\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u00062\b\b\u0001\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a8\u0006\u0012"}, d2 = {"Lcode/name/monkey/retromusic/loaders/PlaylistSongsLoader;", "", "()V", "getPlaylistSongFromCursorImpl", "Lcode/name/monkey/retromusic/model/PlaylistSong;", "cursor", "Landroid/database/Cursor;", "playlistId", "", "getPlaylistSongList", "Lio/reactivex/Observable;", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "context", "Landroid/content/Context;", "playlist", "Lcode/name/monkey/retromusic/model/Playlist;", "makePlaylistSongCursor", "app_normalDebug"})
public final class PlaylistSongsLoader {
    public static final code.name.monkey.retromusic.loaders.PlaylistSongsLoader INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getPlaylistSongList(@org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist playlist) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getPlaylistSongList(@org.jetbrains.annotations.NotNull()
    @io.reactivex.annotations.NonNull()
    android.content.Context context, int playlistId) {
        return null;
    }
    
    @io.reactivex.annotations.NonNull()
    private final code.name.monkey.retromusic.model.PlaylistSong getPlaylistSongFromCursorImpl(@io.reactivex.annotations.NonNull()
    android.database.Cursor cursor, int playlistId) {
        return null;
    }
    
    private final android.database.Cursor makePlaylistSongCursor(@io.reactivex.annotations.NonNull()
    android.content.Context context, int playlistId) {
        return null;
    }
    
    private PlaylistSongsLoader() {
        super();
    }
}