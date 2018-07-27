package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.Genres;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;

public class GenreLoader {

    @NonNull
    public static Observable<ArrayList<Genre>> getAllGenres(@NonNull final Context context) {
        return getGenresFromCursor(context, makeGenreCursor(context));
    }

    @NonNull
    public static Observable<ArrayList<Song>> getSongs(@NonNull final Context context, final int genreId) {
        // The genres table only stores songs that have a genre specified,
        // so we need to get songs without a genre a different way.
        if (genreId == -1) {
            return getSongsWithNoGenre(context);
        }

        return SongLoader.getSongs(makeGenreSongCursor(context, genreId));
    }

    @NonNull
    private static Genre getGenreFromCursor(@NonNull Context context, @NonNull final Cursor cursor) {
        final int id = cursor.getInt(0);
        final String name = cursor.getString(1);
        final int songCount = getSongs(context, id).blockingFirst().size();
        return new Genre(id, name, songCount);

    }

    @NonNull
    private static Observable<ArrayList<Song>> getSongsWithNoGenre(@NonNull final Context context) {
        String selection = BaseColumns._ID + " NOT IN " +
                "(SELECT " + Genres.Members.AUDIO_ID + " FROM audio_genres_map)";
        return SongLoader.getSongs(SongLoader.makeSongCursor(context, selection, null));
    }

    private static boolean hasSongsWithNoGenre(@NonNull final Context context) {
        final Cursor allSongsCursor = SongLoader.makeSongCursor(context, null, null);
        final Cursor allSongsWithGenreCursor = makeAllSongsWithGenreCursor(context);

        if (allSongsCursor == null || allSongsWithGenreCursor == null) {
            return false;
        }

        final boolean hasSongsWithNoGenre = allSongsCursor.getCount() > allSongsWithGenreCursor.getCount();
        allSongsCursor.close();
        allSongsWithGenreCursor.close();
        return hasSongsWithNoGenre;
    }

    @Nullable
    private static Cursor makeAllSongsWithGenreCursor(@NonNull final Context context) {
        try {
            return context.getContentResolver().query(
                    Uri.parse("content://media/external/audio/genres/all/members"),
                    new String[]{Genres.Members.AUDIO_ID}, null, null, null);
        } catch (SecurityException e) {
            return null;
        }
    }

    @Nullable
    private static Cursor makeGenreSongCursor(@NonNull final Context context, int genreId) {
        try {
            return context.getContentResolver().query(
                    Genres.Members.getContentUri("external", genreId),
                    SongLoader.BASE_PROJECTION, SongLoader.BASE_SELECTION, null, PreferenceUtil.getInstance(context).getSongSortOrder());
        } catch (SecurityException e) {
            return null;
        }
    }

    @NonNull
    private static Observable<ArrayList<Genre>> getGenresFromCursor(@NonNull final Context context, @Nullable final Cursor cursor) {
        return Observable.create(e -> {
            final ArrayList<Genre> genres = new ArrayList<>();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Genre genre = getGenreFromCursor(context, cursor);
                        if (genre.songCount > 0) {
                            genres.add(genre);
                        } else {
                            // try to remove the empty genre from the media store
                            try {
                                context.getContentResolver().delete(Genres.EXTERNAL_CONTENT_URI, Genres._ID + " == " + genre.id, null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                // nothing we can do then
                            }
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            e.onNext(genres);
            e.onComplete();
        });
    }


    @Nullable
    private static Cursor makeGenreCursor(@NonNull final Context context) {
        final String[] projection = new String[]{
                Genres._ID,
                Genres.NAME
        };

        try {
            return context.getContentResolver().query(
                    Genres.EXTERNAL_CONTENT_URI,
                    projection, null, null, PreferenceUtil.getInstance(context).getGenreSortOrder());
        } catch (SecurityException e) {
            return null;
        }
    }

}
