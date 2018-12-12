package code.name.monkey.retromusic.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0016\u0018\u0000 \'2\u00020\u0001:\u0001\'B_\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\t\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0010B\u000f\b\u0014\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\u0002\u0010\u0013J\b\u0010\"\u001a\u00020\u0003H\u0016J\u0018\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u00122\u0006\u0010&\u001a\u00020\u0003H\u0016R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017R\u0011\u0010\u000b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0015R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0015R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0015\u00a8\u0006("}, d2 = {"Lcode/name/monkey/retromusic/model/Song;", "Landroid/os/Parcelable;", "id", "", "title", "", "trackNumber", "year", "duration", "", "data", "dateModified", "albumId", "albumName", "artistId", "artistName", "(ILjava/lang/String;IIJLjava/lang/String;JILjava/lang/String;ILjava/lang/String;)V", "in", "Landroid/os/Parcel;", "(Landroid/os/Parcel;)V", "getAlbumId", "()I", "getAlbumName", "()Ljava/lang/String;", "getArtistId", "getArtistName", "getData", "getDateModified", "()J", "getDuration", "getId", "getTitle", "getTrackNumber", "getYear", "describeContents", "writeToParcel", "", "dest", "flags", "Companion", "app_normalDebug"})
public class Song implements android.os.Parcelable {
    private final int id = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String title = null;
    private final int trackNumber = 0;
    private final int year = 0;
    private final long duration = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String data = null;
    private final long dateModified = 0L;
    private final int albumId = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String albumName = null;
    private final int artistId = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String artistName = null;
    @org.jetbrains.annotations.NotNull()
    private static code.name.monkey.retromusic.model.Song emptySong;
    @org.jetbrains.annotations.NotNull()
    public static final android.os.Parcelable.Creator<code.name.monkey.retromusic.model.Song> CREATOR = null;
    public static final code.name.monkey.retromusic.model.Song.Companion Companion = null;
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final int getTrackNumber() {
        return 0;
    }
    
    public final int getYear() {
        return 0;
    }
    
    public final long getDuration() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getData() {
        return null;
    }
    
    public final long getDateModified() {
        return 0L;
    }
    
    public final int getAlbumId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAlbumName() {
        return null;
    }
    
    public final int getArtistId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getArtistName() {
        return null;
    }
    
    @java.lang.Override()
    public int describeContents() {
        return 0;
    }
    
    @java.lang.Override()
    public void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel dest, int flags) {
    }
    
    public Song(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, int trackNumber, int year, long duration, @org.jetbrains.annotations.NotNull()
    java.lang.String data, long dateModified, int albumId, @org.jetbrains.annotations.NotNull()
    java.lang.String albumName, int artistId, @org.jetbrains.annotations.NotNull()
    java.lang.String artistName) {
        super();
    }
    
    protected Song(@org.jetbrains.annotations.NotNull()
    android.os.Parcel in) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcode/name/monkey/retromusic/model/Song$Companion;", "", "()V", "CREATOR", "Landroid/os/Parcelable$Creator;", "Lcode/name/monkey/retromusic/model/Song;", "emptySong", "getEmptySong", "()Lcode/name/monkey/retromusic/model/Song;", "setEmptySong", "(Lcode/name/monkey/retromusic/model/Song;)V", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.model.Song getEmptySong() {
            return null;
        }
        
        public final void setEmptySong(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.model.Song p0) {
        }
        
        private Companion() {
            super();
        }
    }
}