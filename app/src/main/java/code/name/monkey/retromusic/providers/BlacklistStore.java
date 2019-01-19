package code.name.monkey.retromusic.providers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Home;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.FileUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Function7;

import static code.name.monkey.retromusic.Constants.MEDIA_STORE_CHANGED;
import static code.name.monkey.retromusic.ui.adapter.HomeAdapter.RECENT_ALBUMS;
import static code.name.monkey.retromusic.ui.adapter.HomeAdapter.SUGGESTIONS;

public class BlacklistStore extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "blacklist.db";
    private static final int VERSION = 2;
    private static BlacklistStore sInstance = null;
    private Context context;

    public BlacklistStore(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @NonNull
    public static synchronized BlacklistStore getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new BlacklistStore(context.getApplicationContext());
            if (!PreferenceUtil.getInstance().initializedBlacklist()) {
                // blacklisted by default
                sInstance.addPathImpl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
                sInstance.addPathImpl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
                sInstance.addPathImpl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));

                PreferenceUtil.getInstance().setInitializedBlacklist();
            }
        }
        return sInstance;
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BlacklistStoreColumns.NAME + " (" + BlacklistStoreColumns.PATH + " STRING NOT NULL);");
    }

    @Override
    public void onUpgrade(@NonNull final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BlacklistStoreColumns.NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BlacklistStoreColumns.NAME);
        onCreate(db);
    }

    public void addPath(File file) {
        addPathImpl(file);
        notifyMediaStoreChanged();
    }

    private void addPathImpl(File file) {
        if (file == null || contains(file)) {
            return;
        }
        String path = FileUtil.safeGetCanonicalPath(file);

        final SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            // add the entry
            final ContentValues values = new ContentValues(1);
            values.put(BlacklistStoreColumns.PATH, path);
            database.insert(BlacklistStoreColumns.NAME, null, values);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public boolean contains(File file) {
        if (file == null) {
            return false;
        }
        String path = FileUtil.safeGetCanonicalPath(file);

        final SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(BlacklistStoreColumns.NAME,
                new String[]{BlacklistStoreColumns.PATH},
                BlacklistStoreColumns.PATH + "=?",
                new String[]{path},
                null, null, null, null);

        boolean containsPath = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return containsPath;
    }

    public void removePath(File file) {
        final SQLiteDatabase database = getWritableDatabase();
        String path = FileUtil.safeGetCanonicalPath(file);

        database.delete(BlacklistStoreColumns.NAME,
                BlacklistStoreColumns.PATH + "=?",
                new String[]{path});

        notifyMediaStoreChanged();
    }

    public void clear() {
        final SQLiteDatabase database = getWritableDatabase();
        database.delete(BlacklistStoreColumns.NAME, null, null);

        notifyMediaStoreChanged();
    }

    private void notifyMediaStoreChanged() {
        context.sendBroadcast(new Intent(MEDIA_STORE_CHANGED));
    }

    @NonNull
    public ArrayList<String> getPaths() {
        Cursor cursor = getReadableDatabase().query(BlacklistStoreColumns.NAME,
                new String[]{BlacklistStoreColumns.PATH},
                null, null, null, null, null);

        ArrayList<String> paths = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                paths.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        if (cursor != null)
            cursor.close();
        return paths;
    }

    private void hmm() {
        RepositoryImpl repository = new RepositoryImpl(App.Companion.getContext());


        Observable.combineLatest(repository.getSuggestionSongs(), repository.getRecentAlbums(),
                repository.getTopAlbums(), repository.getRecentArtists(), repository.getTopArtists(),
                repository.getAllGenres(), repository.getAllPlaylists(), new Function7<ArrayList<Song>, ArrayList<Album>, ArrayList<Album>, ArrayList<Artist>, ArrayList<Artist>, ArrayList<Genre>, ArrayList<Playlist>, List<Home>>() {
                    @Override
                    public List<Home> apply(ArrayList<Song> songs, ArrayList<Album> albums, ArrayList<Album> albums2, ArrayList<Artist> artists, ArrayList<Artist> artists2, ArrayList<Genre> genres, ArrayList<Playlist> playlists) throws Exception {
                        List<Home> homes = new ArrayList<>();
                        homes.add(new Home(0, 0, songs, SUGGESTIONS));
                        homes.add(new Home(0, 0, albums, RECENT_ALBUMS));
                        return homes;

                    }
                }).subscribe(homes -> {
            if (homes.isEmpty()) {

            }
        });


        Observable.combineLatest(
                repository.getSuggestionSongs(),
                repository.getRecentAlbums(),
                repository.getTopAlbums(),
                repository.getRecentArtists(),
                repository.getTopArtists(),
                repository.getAllGenres(),
                repository.getAllPlaylists(),
                (ArrayList<Song> suggestions, ArrayList<Album> recentAlbums, ArrayList<Album> topAlbums,
                 ArrayList<Artist> recentArtists, ArrayList<Artist> topArtists,
                 ArrayList<Genre> genres, ArrayList<Playlist> playlists) -> {
                    List<Home> homes = new ArrayList<>();

                    return homes;
                }).subscribe();
    }

    public interface BlacklistStoreColumns {
        String NAME = "blacklist";

        String PATH = "path";
    }
}