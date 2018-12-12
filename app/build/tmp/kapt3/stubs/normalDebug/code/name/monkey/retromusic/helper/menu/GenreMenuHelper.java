package code.name.monkey.retromusic.helper.menu;

import java.lang.System;

/**
 * * @author Hemanth S (h4h13).
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J\u001e\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e\u00a8\u0006\u000f"}, d2 = {"Lcode/name/monkey/retromusic/helper/menu/GenreMenuHelper;", "", "()V", "getGenreSongs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "activity", "Landroid/app/Activity;", "genre", "Lcode/name/monkey/retromusic/model/Genre;", "handleMenuClick", "", "Landroidx/appcompat/app/AppCompatActivity;", "item", "Landroid/view/MenuItem;", "app_normalDebug"})
public final class GenreMenuHelper {
    public static final code.name.monkey.retromusic.helper.menu.GenreMenuHelper INSTANCE = null;
    
    public final boolean handleMenuClick(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Genre genre, @org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getGenreSongs(android.app.Activity activity, code.name.monkey.retromusic.model.Genre genre) {
        return null;
    }
    
    private GenreMenuHelper() {
        super();
    }
}