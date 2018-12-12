package code.name.monkey.retromusic.helper;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R \u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcode/name/monkey/retromusic/helper/SearchQueryHelper;", "", "()V", "ALBUM_SELECTION", "", "AND", "ARTIST_SELECTION", "TITLE_SELECTION", "songs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "getSongs", "()Ljava/util/ArrayList;", "setSongs", "(Ljava/util/ArrayList;)V", "context", "Landroid/content/Context;", "extras", "Landroid/os/Bundle;", "app_normalDebug"})
public final class SearchQueryHelper {
    private static final java.lang.String TITLE_SELECTION = "lower(title) = ?";
    private static final java.lang.String ALBUM_SELECTION = "lower(album) = ?";
    private static final java.lang.String ARTIST_SELECTION = "lower(artist) = ?";
    private static final java.lang.String AND = " AND ";
    @org.jetbrains.annotations.NotNull()
    private static java.util.ArrayList<code.name.monkey.retromusic.model.Song> songs;
    public static final code.name.monkey.retromusic.helper.SearchQueryHelper INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongs() {
        return null;
    }
    
    public final void setSongs(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<code.name.monkey.retromusic.model.Song> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getSongs(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.os.Bundle extras) {
        return null;
    }
    
    private SearchQueryHelper() {
        super();
    }
}