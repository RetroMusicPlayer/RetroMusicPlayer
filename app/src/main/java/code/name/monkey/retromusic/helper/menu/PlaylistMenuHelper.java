package code.name.monkey.retromusic.helper.menu;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog;
import code.name.monkey.retromusic.dialogs.DeletePlaylistDialog;
import code.name.monkey.retromusic.dialogs.RenamePlaylistDialog;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.misc.WeakContextAsyncTask;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.PlaylistsUtil;


public class PlaylistMenuHelper {

    public static boolean handleMenuClick(@NonNull AppCompatActivity activity,
                                          @NonNull final Playlist playlist, @NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                MusicPlayerRemote.openQueue(getPlaylistSongs(activity, playlist), 9, true);
                return true;
            case R.id.action_play_next:
                MusicPlayerRemote.playNext(getPlaylistSongs(activity, playlist));
                return true;
            case R.id.action_add_to_playlist:
                AddToPlaylistDialog.create(getPlaylistSongs(activity, playlist))
                        .show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            case R.id.action_add_to_current_playing:
                MusicPlayerRemote.enqueue(getPlaylistSongs(activity, playlist));
                return true;
            case R.id.action_rename_playlist:
                RenamePlaylistDialog.create(playlist.id)
                        .show(activity.getSupportFragmentManager(), "RENAME_PLAYLIST");
                return true;
            case R.id.action_delete_playlist:
                DeletePlaylistDialog.create(playlist)
                        .show(activity.getSupportFragmentManager(), "DELETE_PLAYLIST");
                return true;
            case R.id.action_save_playlist:
                new SavePlaylistAsyncTask(activity).execute(playlist);
                return true;
        }
        return false;
    }

    @NonNull
    private static ArrayList<Song> getPlaylistSongs(@NonNull Activity activity,
                                                    @NonNull Playlist playlist) {
        ArrayList<Song> songs;
        if (playlist instanceof AbsCustomPlaylist) {
            songs = ((AbsCustomPlaylist) playlist).getSongs(activity).blockingFirst();
        } else {
            songs = PlaylistSongsLoader.getPlaylistSongList(activity, playlist).blockingFirst();
        }
        return songs;
    }

    private static class SavePlaylistAsyncTask extends WeakContextAsyncTask<Playlist, String, String> {
        SavePlaylistAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Playlist... params) {
            return String.format(RetroApplication.getInstance().getApplicationContext().getString(R.string
                    .saved_playlist_to), PlaylistsUtil.savePlaylist(RetroApplication.getInstance().getApplicationContext(), params[0]).blockingFirst());
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, string, Toast.LENGTH_LONG).show();
            }
        }
    }
}
