package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.PlaylistsColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import code.name.monkey.retromusic.model.Playlist;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by hemanths on 16/08/17.
 */

public class PlaylistLoader {
    @Nullable
    public static Cursor makePlaylistCursor(@NonNull final Context context, final String selection, final String[] values) {
        try {
            return context.getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                    new String[]{
                        /* 0 */
                            BaseColumns._ID,
                        /* 1 */
                            PlaylistsColumns.NAME
                    }, selection, values, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            return null;
        }
    }

    @NonNull
    public static Observable<Playlist> getPlaylist(@Nullable final Cursor cursor) {
        return Observable.create(e -> {
            Playlist playlist = new Playlist();

            if (cursor != null && cursor.moveToFirst()) {
                playlist = getPlaylistFromCursorImpl(cursor);
            }
            if (cursor != null)
                cursor.close();

            e.onNext(playlist);
            e.onComplete();
        });


    }

    @NonNull
    public static Observable<Playlist> getPlaylist(@NonNull final Context context, final String playlistName) {
        return getPlaylist(makePlaylistCursor(
                context,
                PlaylistsColumns.NAME + "=?",
                new String[]{
                        playlistName
                }
        ));
    }

    @NonNull
    public static Observable<Playlist> getPlaylist(@NonNull final Context context, final int playlistId) {
        return getPlaylist(makePlaylistCursor(
                context,
                BaseColumns._ID + "=?",
                new String[]{
                        String.valueOf(playlistId)
                }
        ));
    }

    @NonNull
    private static Playlist getPlaylistFromCursorImpl(@NonNull final Cursor cursor) {

        final int id = cursor.getInt(0);
        final String name = cursor.getString(1);
        return new Playlist(id, name);
    }


    @NonNull
    public static Observable<ArrayList<Playlist>> getAllPlaylists(@Nullable final Cursor cursor) {
        return Observable.create(e -> {
            ArrayList<Playlist> playlists = new ArrayList<>();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    playlists.add(getPlaylistFromCursorImpl(cursor));
                } while (cursor.moveToNext());
            }
            if (cursor != null)
                cursor.close();

            e.onNext(playlists);
            e.onComplete();
        });
    }

    @NonNull
    public static Observable<ArrayList<Playlist>> getAllPlaylists(@NonNull final Context context) {
        return getAllPlaylists(makePlaylistCursor(context, null, null));
    }

    public static void deletePlaylists(Context context, long playlistId) {
        Uri localUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("_id IN (");
        localStringBuilder.append((playlistId));
        localStringBuilder.append(")");
        context.getContentResolver().delete(localUri, localStringBuilder.toString(), null);
    }
}
