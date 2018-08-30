package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import code.name.monkey.retromusic.helper.ShuffleHelper;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.providers.BlacklistStore;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;

/**
 * Created by hemanths on 10/08/17.
 */

public class SongLoader {

    protected static final String BASE_SELECTION =
            AudioColumns.IS_MUSIC + "=1" + " AND " + AudioColumns.TITLE + " != ''";
    protected static final String[] BASE_PROJECTION = new String[]{
            BaseColumns._ID,// 0
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
    };

    @NonNull
    public static Observable<ArrayList<Song>> getAllSongs(@NonNull Context context) {
        Cursor cursor = makeSongCursor(context, null, null);
        return getSongs(cursor);
    }

    @NonNull
    public static Observable<ArrayList<Song>> getSongs(@NonNull final Context context,
                                                       final String query) {
        Cursor cursor = makeSongCursor(context, AudioColumns.TITLE + " LIKE ?",
                new String[]{"%" + query + "%"});
        return getSongs(cursor);
    }

    @NonNull
    public static Observable<ArrayList<Song>> getSongs(@Nullable final Cursor cursor) {
        return Observable.create(e -> {
            ArrayList<Song> songs = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    songs.add(getSongFromCursorImpl(cursor));
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }
            e.onNext(songs);
            e.onComplete();
        });
    }

    @NonNull
    private static Song getSongFromCursorImpl(@NonNull Cursor cursor) {
        final int id = cursor.getInt(0);
        final String title = cursor.getString(1);
        final int trackNumber = cursor.getInt(2);
        final int year = cursor.getInt(3);
        final long duration = cursor.getLong(4);
        final String data = cursor.getString(5);
        final long dateModified = cursor.getLong(6);
        final int albumId = cursor.getInt(7);
        final String albumName = cursor.getString(8);
        final int artistId = cursor.getInt(9);
        final String artistName = cursor.getString(10);

        return new Song(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName,
                artistId, artistName);
    }

    @Nullable
    public static Cursor makeSongCursor(@NonNull final Context context,
                                        @Nullable final String selection, final String[] selectionValues) {
        return makeSongCursor(context, selection, selectionValues,
                PreferenceUtil.getInstance(context).getSongSortOrder());
    }

    @Nullable
    public static Cursor makeSongCursor(@NonNull final Context context, @Nullable String selection,
                                        String[] selectionValues, final String sortOrder) {
        if (selection != null && !selection.trim().equals("")) {
            selection = BASE_SELECTION + " AND " + selection;
        } else {
            selection = BASE_SELECTION;
        }

        // Blacklist
        ArrayList<String> paths = BlacklistStore.getInstance(context).getPaths();
        if (!paths.isEmpty()) {
            selection = generateBlacklistSelection(selection, paths.size());
            selectionValues = addBlacklistSelectionValues(selectionValues, paths);
        }

        try {
            return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    BASE_PROJECTION, selection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }

    private static String generateBlacklistSelection(String selection, int pathCount) {
        StringBuilder newSelection = new StringBuilder(
                selection != null && !selection.trim().equals("") ? selection + " AND " : "");
        newSelection.append(AudioColumns.DATA + " NOT LIKE ?");
        for (int i = 0; i < pathCount - 1; i++) {
            newSelection.append(" AND " + AudioColumns.DATA + " NOT LIKE ?");
        }
        return newSelection.toString();
    }

    private static String[] addBlacklistSelectionValues(String[] selectionValues,
                                                        ArrayList<String> paths) {
        if (selectionValues == null) {
            selectionValues = new String[0];
        }
        String[] newSelectionValues = new String[selectionValues.length + paths.size()];
        System.arraycopy(selectionValues, 0, newSelectionValues, 0, selectionValues.length);
        for (int i = selectionValues.length; i < newSelectionValues.length; i++) {
            newSelectionValues[i] = paths.get(i - selectionValues.length) + "%";
        }
        return newSelectionValues;
    }

    @NonNull
    public static Observable<Song> getSong(@Nullable Cursor cursor) {
        return Observable.create(e -> {
            Song song;
            if (cursor != null && cursor.moveToFirst()) {
                song = getSongFromCursorImpl(cursor);
            } else {
                song = Song.EMPTY_SONG;
            }
            if (cursor != null) {
                cursor.close();
            }
            e.onNext(song);
            e.onComplete();
        });
    }

    @NonNull
    public static Observable<Song> getSong(@NonNull final Context context, final int queryId) {
        Cursor cursor = makeSongCursor(context, AudioColumns._ID + "=?",
                new String[]{String.valueOf(queryId)});
        return getSong(cursor);
    }

    public static Observable<ArrayList<Song>> suggestSongs(@NonNull Context context) {
        return Observable.create(observer -> {
            SongLoader.getAllSongs(context)
                    .subscribe(songs -> {
                        ArrayList<Song> list = new ArrayList<>();
                        if (songs.isEmpty()) {
                            observer.onNext(new ArrayList<>());
                            observer.onComplete();
                            return;
                        }
                        ShuffleHelper.makeShuffleList(songs, -1);
                        if (songs.size() > 10) {
                            list.addAll(songs.subList(0, 10));
                        } else {
                            list.addAll(songs);
                        }
                        observer.onNext(list);
                        observer.onComplete();
                    });
        });
    }

}
