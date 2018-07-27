package code.name.monkey.retromusic.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class M3UWriter implements M3UConstants {
    public static final String TAG = M3UWriter.class.getSimpleName();

    public static Observable<File> write(@NonNull Context context,
                                         @NonNull File dir, @NonNull Playlist playlist) {
        if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        File file = new File(dir, playlist.name.concat("." + EXTENSION));

        if (playlist instanceof AbsCustomPlaylist) {
            return Observable.create(e -> {
                ((AbsCustomPlaylist) playlist).getSongs(context).subscribe(songs -> {
                    saveSongsToFile(file, e, songs);
                });
            });
        } else
            return Observable.create(e ->
                    PlaylistSongsLoader.getPlaylistSongList(context, playlist.id)
                            .subscribe(songs -> {
                                saveSongsToFile(file, e, songs);
                            }));
    }

    private static void saveSongsToFile(File file, ObservableEmitter<File> e, ArrayList<Song> songs) throws IOException {
        if (songs.size() > 0) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(HEADER);
            for (Song song : songs) {
                bw.newLine();
                bw.write(ENTRY + song.duration + DURATION_SEPARATOR + song.artistName + " - " + song.title);
                bw.newLine();
                bw.write(song.data);
            }

            bw.close();
        }
        e.onNext(file);
        e.onComplete();
    }
}