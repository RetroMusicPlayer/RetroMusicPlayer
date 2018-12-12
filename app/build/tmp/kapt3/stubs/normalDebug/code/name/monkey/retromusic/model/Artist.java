package code.name.monkey.retromusic.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0015\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0018\u001a\u00020\u0004R\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0019\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\nR\u0011\u0010\u000f\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\nR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\f\u00a8\u0006\u001a"}, d2 = {"Lcode/name/monkey/retromusic/model/Artist;", "", "albums", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Album;", "(Ljava/util/ArrayList;)V", "()V", "albumCount", "", "getAlbumCount", "()I", "getAlbums", "()Ljava/util/ArrayList;", "id", "getId", "name", "", "getName", "()Ljava/lang/String;", "songCount", "getSongCount", "songs", "Lcode/name/monkey/retromusic/model/Song;", "getSongs", "safeGetFirstAlbum", "Companion", "app_normalDebug"})
public final class Artist {
    @org.jetbrains.annotations.Nullable()
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Album> albums = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UNKNOWN_ARTIST_DISPLAY_NAME = "Unknown Artist";
    public static final code.name.monkey.retromusic.model.Artist.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Album> getAlbums() {
        return null;
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    public final int getSongCount() {
        return 0;
    }
    
    public final int getAlbumCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final code.name.monkey.retromusic.model.Album safeGetFirstAlbum() {
        return null;
    }
    
    public Artist(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Album> albums) {
        super();
    }
    
    public Artist() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/model/Artist$Companion;", "", "()V", "UNKNOWN_ARTIST_DISPLAY_NAME", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}