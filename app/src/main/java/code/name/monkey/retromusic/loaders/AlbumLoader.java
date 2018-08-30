package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.provider.MediaStore.Audio.AudioColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;
import java.util.ArrayList;

/**
 * Created by hemanths on 11/08/17.
 */

public class AlbumLoader {

  public static Observable<ArrayList<Album>> getAllAlbums(@NonNull Context context) {
    Observable<ArrayList<Song>> songs = SongLoader.getSongs(SongLoader.makeSongCursor(
        context,
        null,
        null,
        getSongLoaderSortOrder(context))
    );

    return splitIntoAlbums(songs);
  }

  @NonNull
  public static Observable<ArrayList<Album>> getAlbums(@NonNull final Context context,
      String query) {
    Observable<ArrayList<Song>> songs = SongLoader.getSongs(SongLoader.makeSongCursor(
        context,
        AudioColumns.ALBUM + " LIKE ?",
        new String[]{"%" + query + "%"},
        getSongLoaderSortOrder(context))
    );
    return splitIntoAlbums(songs);
  }

  @NonNull
  public static Observable<Album> getAlbum(@NonNull final Context context, int albumId) {
    return Observable.create(e -> {
      Observable<ArrayList<Song>> songs = SongLoader.getSongs(SongLoader
          .makeSongCursor(context, AudioColumns.ALBUM_ID + "=?",
              new String[]{String.valueOf(albumId)}, getSongLoaderSortOrder(context)));
      songs.subscribe(songs1 -> {
        e.onNext(new Album(songs1));
        e.onComplete();
      });
    });
  }

  @NonNull
  public static Observable<ArrayList<Album>> splitIntoAlbums(
      @Nullable final Observable<ArrayList<Song>> songs) {
    return Observable.create(e -> {
      ArrayList<Album> albums = new ArrayList<>();
      if (songs != null) {
        songs.subscribe(songs1 -> {
          for (Song song : songs1) {
            getOrCreateAlbum(albums, song.albumId).subscribe(album -> album.songs.add(song));
          }
        });
      }
      e.onNext(albums);
      e.onComplete();
    });
  }

  @NonNull
  public static ArrayList<Album> splitIntoAlbums(@Nullable final ArrayList<Song> songs) {
    ArrayList<Album> albums = new ArrayList<>();
    if (songs != null) {
      for (Song song : songs) {
        getOrCreateAlbum(albums, song.albumId).subscribe(album -> album.songs.add(song));
      }
    }
    return albums;
  }

  private static Observable<Album> getOrCreateAlbum(ArrayList<Album> albums, int albumId) {
    return Observable.create(e -> {
      for (Album album : albums) {
        if (!album.songs.isEmpty() && album.songs.get(0).albumId == albumId) {
          e.onNext(album);
          e.onComplete();
          return;
        }
      }
      Album album = new Album();
      albums.add(album);
      e.onNext(album);
      e.onComplete();
    });
  }

  public static String getSongLoaderSortOrder(Context context) {
    return PreferenceUtil.getInstance(context).getAlbumSortOrder() + ", " +
        //PreferenceUtil.getInstance(context).getAlbumSongSortOrder() + "," +
        PreferenceUtil.getInstance(context).getAlbumDetailSongSortOrder();
  }
}
