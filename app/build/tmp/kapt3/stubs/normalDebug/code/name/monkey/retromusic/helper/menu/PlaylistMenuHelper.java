package code.name.monkey.retromusic.helper.menu;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u000fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J\u001e\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e\u00a8\u0006\u0010"}, d2 = {"Lcode/name/monkey/retromusic/helper/menu/PlaylistMenuHelper;", "", "()V", "getPlaylistSongs", "Ljava/util/ArrayList;", "Lcode/name/monkey/retromusic/model/Song;", "activity", "Landroid/app/Activity;", "playlist", "Lcode/name/monkey/retromusic/model/Playlist;", "handleMenuClick", "", "Landroidx/appcompat/app/AppCompatActivity;", "item", "Landroid/view/MenuItem;", "SavePlaylistAsyncTask", "app_normalDebug"})
public final class PlaylistMenuHelper {
    public static final code.name.monkey.retromusic.helper.menu.PlaylistMenuHelper INSTANCE = null;
    
    public final boolean handleMenuClick(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity activity, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.model.Playlist playlist, @org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final java.util.ArrayList<code.name.monkey.retromusic.model.Song> getPlaylistSongs(android.app.Activity activity, code.name.monkey.retromusic.model.Playlist playlist) {
        return null;
    }
    
    private PlaylistMenuHelper() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J!\u0010\u0007\u001a\u00020\u00032\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\t\"\u00020\u0002H\u0014\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u0014\u00a8\u0006\u000e"}, d2 = {"Lcode/name/monkey/retromusic/helper/menu/PlaylistMenuHelper$SavePlaylistAsyncTask;", "Lcode/name/monkey/retromusic/misc/WeakContextAsyncTask;", "Lcode/name/monkey/retromusic/model/Playlist;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "doInBackground", "params", "", "([Lcode/name/monkey/retromusic/model/Playlist;)Ljava/lang/String;", "onPostExecute", "", "string", "app_normalDebug"})
    static final class SavePlaylistAsyncTask extends code.name.monkey.retromusic.misc.WeakContextAsyncTask<code.name.monkey.retromusic.model.Playlist, java.lang.String, java.lang.String> {
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        protected java.lang.String doInBackground(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.model.Playlist... params) {
            return null;
        }
        
        @java.lang.Override()
        protected void onPostExecute(@org.jetbrains.annotations.NotNull()
        java.lang.String string) {
        }
        
        public SavePlaylistAsyncTask(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            super(null);
        }
    }
}