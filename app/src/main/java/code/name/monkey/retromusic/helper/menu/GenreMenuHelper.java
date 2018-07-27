package code.name.monkey.retromusic.helper.menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import code.name.monkey.retromusic.loaders.GenreLoader;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;

/**
 * @author Hemanth S (h4h13).
 */

public class GenreMenuHelper {
    public static boolean handleMenuClick(@NonNull AppCompatActivity activity,
                                          @NonNull Genre genre,
                                          @NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                MusicPlayerRemote.openQueue(getGenreSongs(activity, genre), 0, true);
                return true;
            case R.id.action_play_next:
                MusicPlayerRemote.playNext(getGenreSongs(activity, genre));
                return true;
            case R.id.action_add_to_playlist:
                AddToPlaylistDialog.create(getGenreSongs(activity, genre))
                        .show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            case R.id.action_add_to_current_playing:
                MusicPlayerRemote.enqueue(getGenreSongs(activity, genre));
                return true;
        }
        return false;
    }

    @NonNull
    private static ArrayList<Song> getGenreSongs(@NonNull Activity activity,
                                                 @NonNull Genre genre) {
        ArrayList<Song> songs;
        songs = GenreLoader.getSongs(activity, genre.id).blockingFirst();
        return songs;
    }
}
