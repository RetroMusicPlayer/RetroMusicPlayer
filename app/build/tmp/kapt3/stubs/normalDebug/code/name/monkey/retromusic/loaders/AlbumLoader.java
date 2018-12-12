package code.name.monkey.retromusic.loaders;

import java.lang.System;

/**
 * * Created by hemanths on 11/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0016\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcode/name/monkey/retromusic/loaders/AlbumLoader;", "", "()V", "Companion", "app_normalDebug"})
public class AlbumLoader {
    public static final code.name.monkey.retromusic.loaders.AlbumLoader.Companion Companion = null;
    
    public AlbumLoader() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\"\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\rJ\u001a\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0006\u0010\u0006\u001a\u00020\u0007J$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\u0006\u0010\b\u001a\u00020\tH\u0002J\u0006\u0010\u0011\u001a\u00020\rJ(\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0014\u0010\u0013\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u000b\u0018\u00010\u0004J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcode/name/monkey/retromusic/loaders/AlbumLoader$Companion;", "", "()V", "getAlbum", "Lio/reactivex/Observable;", "Lcode/name/monkey/retromusic/model/Album;", "context", "Landroid/content/Context;", "albumId", "", "getAlbums", "Ljava/util/ArrayList;", "query", "", "getAllAlbums", "getOrCreateAlbum", "albums", "getSongLoaderSortOrder", "splitIntoAlbums", "songs", "Lcode/name/monkey/retromusic/model/Song;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getAllAlbums(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> getAlbums(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String query) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final io.reactivex.Observable<code.name.monkey.retromusic.model.Album> getAlbum(@org.jetbrains.annotations.NotNull()
        android.content.Context context, int albumId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Album>> splitIntoAlbums(@org.jetbrains.annotations.Nullable()
        io.reactivex.Observable<java.util.ArrayList<code.name.monkey.retromusic.model.Song>> songs) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.ArrayList<code.name.monkey.retromusic.model.Album> splitIntoAlbums(@org.jetbrains.annotations.Nullable()
        java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs) {
            return null;
        }
        
        private final io.reactivex.Observable<code.name.monkey.retromusic.model.Album> getOrCreateAlbum(java.util.ArrayList<code.name.monkey.retromusic.model.Album> albums, int albumId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSongLoaderSortOrder() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}