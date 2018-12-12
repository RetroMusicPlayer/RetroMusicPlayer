package code.name.monkey.retromusic.loaders;

import java.lang.System;

/**
 * * Created by hemanths on 10/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J3\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00042\u000e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u0002\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\n\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\rH\u0002J\u001a\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\b0\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\rJ\u0018\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\"\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\b0\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0005J\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\b0\u000f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016J;\u0010\u001a\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u000e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00042\b\b\u0002\u0010\u001b\u001a\u00020\u0005H\u0007\u00a2\u0006\u0002\u0010\u001cJ\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\b0\u000f2\u0006\u0010\u0011\u001a\u00020\u0012\u00a8\u0006\u001e"}, d2 = {"Lcode/name/monkey/retromusic/loaders/SongLoader;", "", "()V", "addBlacklistSelectionValues", "", "", "selectionValues", "paths", "Ljava/util/ArrayList;", "([Ljava/lang/String;Ljava/util/ArrayList;)[Ljava/lang/String;", "generateBlacklistSelection", "selection", "pathCount", "", "getAllSongs", "Lio/reactivex/Observable;", "Lcode/name/monkey/retromusic/model/Song;", "context", "Landroid/content/Context;", "getSong", "queryId", "cursor", "Landroid/database/Cursor;", "getSongFromCursorImpl", "getSongs", "query", "makeSongCursor", "sortOrder", "(Landroid/content/Context;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "suggestSongs", "app_normalDebug"})
public final class SongLoader {
    public static final code.name.monkey.retromusic.loaders.SongLoader INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getAllSongs(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getSongs(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> getSongs(@org.jetbrains.annotations.Nullable()
    android.database.Cursor cursor) {
        return null;
    }
    
    private final code.name.monkey.retromusic.model.Song getSongFromCursorImpl(android.database.Cursor cursor) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.database.Cursor makeSongCursor(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String selection, @org.jetbrains.annotations.Nullable()
    java.lang.String[] selectionValues, @org.jetbrains.annotations.NotNull()
    java.lang.String sortOrder) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.database.Cursor makeSongCursor(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String selection, @org.jetbrains.annotations.Nullable()
    java.lang.String[] selectionValues) {
        return null;
    }
    
    private final java.lang.String generateBlacklistSelection(java.lang.String selection, int pathCount) {
        return null;
    }
    
    private final java.lang.String[] addBlacklistSelectionValues(java.lang.String[] selectionValues, java.util.ArrayList<java.lang.String> paths) {
        return null;
    }
    
    private final io.reactivex.Observable<code.name.monkey.retromusic.model.Song> getSong(android.database.Cursor cursor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<code.name.monkey.retromusic.model.Song> getSong(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int queryId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> suggestSongs(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private SongLoader() {
        super();
    }
}