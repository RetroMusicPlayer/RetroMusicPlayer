package code.name.monkey.retromusic.util;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\r\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcode/name/monkey/retromusic/util/CustomArtistImageUtil;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mPreferences", "Landroid/content/SharedPreferences;", "hasCustomArtistImage", "", "artist", "Lcode/name/monkey/retromusic/model/Artist;", "resetCustomArtistImage", "", "setCustomArtistImage", "uri", "Landroid/net/Uri;", "Companion", "app_normalDebug"})
public final class CustomArtistImageUtil {
    private final android.content.SharedPreferences mPreferences = null;
    private static final java.lang.String CUSTOM_ARTIST_IMAGE_PREFS = "custom_artist_image";
    private static final java.lang.String FOLDER_NAME = "/custom_artist_images/";
    private static code.name.monkey.retromusic.util.CustomArtistImageUtil sInstance;
    public static final code.name.monkey.retromusic.util.CustomArtistImageUtil.Companion Companion = null;
    
    public final void setCustomArtistImage(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Artist artist, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void resetCustomArtistImage(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Artist artist) {
    }
    
    public final boolean hasCustomArtistImage(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Artist artist) {
        return false;
    }
    
    private CustomArtistImageUtil(android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.io.File getFile(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Artist artist) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcode/name/monkey/retromusic/util/CustomArtistImageUtil$Companion;", "", "()V", "CUSTOM_ARTIST_IMAGE_PREFS", "", "FOLDER_NAME", "sInstance", "Lcode/name/monkey/retromusic/util/CustomArtistImageUtil;", "getFile", "Ljava/io/File;", "artist", "Lcode/name/monkey/retromusic/model/Artist;", "getFileName", "getInstance", "context", "Landroid/content/Context;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.util.CustomArtistImageUtil getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getFileName(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.model.Artist artist) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.io.File getFile(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.model.Artist artist) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}