package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import code.name.monkey.retromusic.model.Song;
import io.reactivex.Observable;

/**
 * @author Hemanth S (h4h13).
 */

public class GenreSongsLoader {

    public static Observable<ArrayList<Song>> getGenreSongsList(@NonNull Context context, int genreId) {
        return Observable.create(e -> {
            ArrayList<Song> list = new ArrayList<>();
            Cursor cursor = makeGenreSongCursor(context, genreId);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(getGenreSongFromCursorImpl(cursor));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
            e.onNext((ArrayList<Song>) (List) list);
            e.onComplete();
        });
    }

    @NonNull
    private static Song getGenreSongFromCursorImpl(@NonNull Cursor cursor) {
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
        return new Song(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName);
    }

    private static Cursor makeGenreSongCursor(Context context, long genreId) {
        try {
            return context.getContentResolver().query(
                    MediaStore.Audio.Genres.Members.getContentUri("external", genreId),
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
                    }, SongLoader.BASE_SELECTION, null,
                    MediaStore.Audio.Genres.Members.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            return null;
        }
    }
}
