package code.name.monkey.retromusic.helper;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.model.Song;


public class SearchQueryHelper {
    private static final String TITLE_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.TITLE + ") = ?";
    private static final String ALBUM_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ALBUM + ") = ?";
    private static final String ARTIST_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ARTIST + ") = ?";
    private static final String AND = " AND ";
    private static ArrayList<Song> songs = new ArrayList<>();

    public static ArrayList<Song> getSongs() {
        return songs;
    }

    public static void setSongs(ArrayList<Song> songs) {
        SearchQueryHelper.songs = songs;
    }

    @NonNull
    public static ArrayList<Song> getSongs(@NonNull final Context context, @NonNull final Bundle extras) {
        final String query = extras.getString(SearchManager.QUERY, null);
        final String artistName = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST, null);
        final String albumName = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM, null);
        final String titleName = extras.getString(MediaStore.EXTRA_MEDIA_TITLE, null);

        ArrayList<Song> songs = new ArrayList<>();
        if (artistName != null && albumName != null && titleName != null) {
            songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ARTIST_SELECTION + AND + ALBUM_SELECTION + AND + TITLE_SELECTION, new String[]{artistName.toLowerCase(), albumName.toLowerCase(), titleName.toLowerCase()})).blockingFirst();
        }
        if (!songs.isEmpty()) {
            return songs;
        }
        if (artistName != null && titleName != null) {
            songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ARTIST_SELECTION + AND + TITLE_SELECTION, new String[]{artistName.toLowerCase(), titleName.toLowerCase()})).blockingFirst();
        }
        if (!songs.isEmpty()) {
            return songs;
        }
        if (albumName != null && titleName != null) {
            songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ALBUM_SELECTION + AND + TITLE_SELECTION, new String[]{albumName.toLowerCase(), titleName.toLowerCase()})).blockingFirst();
        }
        if (!songs.isEmpty()) {
            return songs;
        }
        if (artistName != null) {
            songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ARTIST_SELECTION, new String[]{artistName.toLowerCase()})).blockingFirst();
        }
        if (!songs.isEmpty()) {
            return songs;
        }
        if (albumName != null) {
            songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ALBUM_SELECTION, new String[]{albumName.toLowerCase()})).blockingFirst();
        }
        if (!songs.isEmpty()) {
            return songs;
        }
        if (titleName != null) {
            songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, TITLE_SELECTION, new String[]{titleName.toLowerCase()})).blockingFirst();
        }
        if (!songs.isEmpty()) {
            return songs;
        }
        songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ARTIST_SELECTION, new String[]{query.toLowerCase()})).blockingFirst();

        if (!songs.isEmpty()) {
            return songs;
        }
        songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, ALBUM_SELECTION, new String[]{query.toLowerCase()})).blockingFirst();
        if (!songs.isEmpty()) {
            return songs;
        }
        songs = SongLoader.Companion.getSongs(SongLoader.Companion.makeSongCursor(context, TITLE_SELECTION, new String[]{query.toLowerCase()})).blockingFirst();
        if (!songs.isEmpty()) {
            return songs;
        }
        return new ArrayList<Song>();
    }


}
