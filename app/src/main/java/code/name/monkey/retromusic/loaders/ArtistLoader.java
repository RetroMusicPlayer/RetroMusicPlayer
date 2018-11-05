package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.provider.MediaStore.Audio.AudioColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;

public class ArtistLoader {
    private static String getSongLoaderSortOrder(Context context) {
        return PreferenceUtil.getInstance().getArtistSortOrder() + ", " +
                PreferenceUtil.getInstance().getArtistAlbumSortOrder() + ", " +
                PreferenceUtil.getInstance().getAlbumDetailSongSortOrder() + ", " +
                PreferenceUtil.getInstance().getArtistDetailSongSortOrder();
    }

    @NonNull
    public static Observable<Artist> getArtist(@NonNull final Context context, int artistId) {
        return Observable.create(e -> SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                AudioColumns.ARTIST_ID + "=?",
                new String[]{String.valueOf(artistId)},
                getSongLoaderSortOrder(context)))
                .subscribe(songs -> {
                    Artist artist = new Artist(AlbumLoader.splitIntoAlbums(songs));
                    e.onNext(artist);
                    e.onComplete();
                }));
    }

    @NonNull
    public static Observable<ArrayList<Artist>> getAllArtists(@NonNull final Context context) {
        return Observable.create(e -> SongLoader
                .getSongs(SongLoader.makeSongCursor(
                        context,
                        null,
                        null,
                        getSongLoaderSortOrder(context))
                ).subscribe(songs -> {
                    e.onNext(splitIntoArtists(AlbumLoader.splitIntoAlbums(songs)));
                    e.onComplete();
                }));

    }

    @NonNull
    public static Observable<ArrayList<Artist>> getArtists(@NonNull final Context context, String query) {
        return Observable.create(e -> SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                AudioColumns.ARTIST + " LIKE ?",
                new String[]{"%" + query + "%"},
                getSongLoaderSortOrder(context))
        ).subscribe(songs -> {
            e.onNext(splitIntoArtists(AlbumLoader.splitIntoAlbums(songs)));
            e.onComplete();
        }));
    }

    @NonNull
    public static ArrayList<Artist> splitIntoArtists(@Nullable final ArrayList<Album> albums) {
        ArrayList<Artist> artists = new ArrayList<>();
        if (albums != null) {
            for (Album album : albums) {
                getOrCreateArtist(artists, album.getArtistId()).albums.add(album);
            }
        }
        return artists;
    }

    private static Artist getOrCreateArtist(ArrayList<Artist> artists, int artistId) {
        for (Artist artist : artists) {
            if (!artist.albums.isEmpty() && !artist.albums.get(0).songs.isEmpty() && artist.albums.get(0).songs.get(0).artistId == artistId) {
                return artist;
            }
        }
        Artist album = new Artist();
        artists.add(album);
        return album;
    }

    public static Observable<ArrayList<Artist>> splitIntoArtists(Observable<ArrayList<Album>> albums) {
        return Observable.create(e -> {
            ArrayList<Artist> artists = new ArrayList<>();
            albums.subscribe(localAlbums -> {
                if (localAlbums != null) {
                    for (Album album : localAlbums) {
                        getOrCreateArtist(artists, album.getArtistId()).albums.add(album);
                    }
                }
                e.onNext(artists);
                e.onComplete();
            });
        });
    }
}
