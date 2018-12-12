package code.name.monkey.retromusic.helper;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/helper/M3UWriter;", "Lcode/name/monkey/retromusic/helper/M3UConstants;", "()V", "Companion", "app_normalDebug"})
public final class M3UWriter implements code.name.monkey.retromusic.helper.M3UConstants {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.helper.M3UWriter.Companion Companion = null;
    
    public M3UWriter() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J,\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J$\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0016R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0017"}, d2 = {"Lcode/name/monkey/retromusic/helper/M3UWriter$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "saveSongsToFile", "", "file", "Ljava/io/File;", "e", "Lio/reactivex/ObservableEmitter;", "songs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "write", "Lio/reactivex/Observable;", "context", "Landroid/content/Context;", "dir", "playlist", "Lcode/name/monkey/retromusic/model/Playlist;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTAG() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final io.reactivex.Observable<java.io.File> write(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.io.File dir, @org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.model.Playlist playlist) {
            return null;
        }
        
        private final void saveSongsToFile(java.io.File file, io.reactivex.ObservableEmitter<java.io.File> e, java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs) {
        }
        
        private Companion() {
            super();
        }
    }
}