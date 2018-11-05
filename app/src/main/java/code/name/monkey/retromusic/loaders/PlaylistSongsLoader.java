package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;

import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.PlaylistSong;
import code.name.monkey.retromusic.model.Song;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by hemanths on 16/08/17.
 */

public class PlaylistSongsLoader {

    @NonNull
    public static Observable<ArrayList<Song>> getPlaylistSongList(@NonNull Context context, Playlist playlist) {
        if (playlist instanceof AbsCustomPlaylist) {
            return ((AbsCustomPlaylist) playlist).getSongs(context);
        } else {
            //noinspection unchecked
            return getPlaylistSongList(context, playlist.id);
        }
    }

    @NonNull
    public static Observable<ArrayList<Song>> getPlaylistSongList(@NonNull Context context, final int playlistId) {
        return Observable.create(e -> {
            ArrayList<PlaylistSong> songs = new ArrayList<>();
            Cursor cursor = makePlaylistSongCursor(context, playlistId);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    songs.add(getPlaylistSongFromCursorImpl(cursor, playlistId));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
            e.onNext((ArrayList<Song>) (List) songs);
            e.onComplete();
        });
    }

    @NonNull
    private static PlaylistSong getPlaylistSongFromCursorImpl(@NonNull Cursor cursor, int playlistId) {
        final int id = cursor.getInt(0);
        final String title = cursor.getString(1);
        final int trackNumber = cursor.getInt(2);
        final int year = cursor.getInt(3);
        final long duration = cursor.getLong(4);
        final String data = cursor.getString(5);
        final int dateModified = cursor.getInt(6);
        final int albumId = cursor.getInt(7);
        final String albumName = cursor.getString(8);
        final int artistId = cursor.getInt(9);
        final String artistName = cursor.getString(10);
        final int idInPlaylist = cursor.getInt(11);
        final String composer = cursor.getString(12);

        return new PlaylistSong(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName, playlistId, idInPlaylist, composer);
    }

    private static Cursor makePlaylistSongCursor(@NonNull final Context context, final int playlistId) {
        try {
            return context.getContentResolver().query(
                    MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                    new String[]{
                            MediaStore.Audio.Playlists.Members.AUDIO_ID,// 0
                            AudioColumns.TITLE,// 1
                            AudioColumns.TRACK,// 2
                            AudioColumns.YEAR,// 3
                            AudioColumns.DURATION,// 4
                            AudioColumns.DATA,// 5
                            AudioColumns.DATE_MODIFIED,// 6
                            AudioColumns.ALBUM_ID,// 7
                            AudioColumns.ALBUM,// 8
                            AudioColumns.ARTIST_ID,// 9
                            AudioColumns.ARTIST,// 10
                            MediaStore.Audio.Playlists.Members._ID, // 11
                            AudioColumns.COMPOSER,// 12
                    }, SongLoader.BASE_SELECTION, null,
                    MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            return null;
        }
    }
}
