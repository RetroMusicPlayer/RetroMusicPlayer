package code.name.monkey.retromusic.loaders;


import android.content.Context;
import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist;
import code.name.monkey.retromusic.model.smartplaylist.HistoryPlaylist;
import code.name.monkey.retromusic.model.smartplaylist.LastAddedPlaylist;
import code.name.monkey.retromusic.model.smartplaylist.MyTopTracksPlaylist;
import io.reactivex.Observable;

public class HomeLoader {

    public static Observable<ArrayList<AbsSmartPlaylist>> getRecentAndTopThings(
            @NonNull Context context) {
        ArrayList<AbsSmartPlaylist> objects = new ArrayList<>();
        return Observable.create(e -> {

            new HistoryPlaylist(context).getSongs(context).subscribe(songs -> {
                if (!songs.isEmpty()) {
                    objects.add(new HistoryPlaylist(context));
                }
            });
            new LastAddedPlaylist(context).getSongs(context).subscribe(songs -> {
                if (!songs.isEmpty()) {
                    objects.add(new LastAddedPlaylist(context));
                }
            });
            new MyTopTracksPlaylist(context).getSongs(context).subscribe(songs -> {
                if (!songs.isEmpty()) {
                    objects.add(new MyTopTracksPlaylist(context));
                }
            });

            e.onNext(objects);
            e.onComplete();
        });
    }

    public static Observable<ArrayList<Playlist>> getHomeLoader(@NonNull Context context) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        PlaylistLoader.getAllPlaylists(context)
                .subscribe(playlists1 -> {
                    if (playlists1.size() > 0) {
                        for (Playlist playlist : playlists1) {
                            PlaylistSongsLoader.getPlaylistSongList(context, playlist)
                                    .subscribe(songs -> {
                                        if (songs.size() > 0) {
                                            playlists.add(playlist);
                                        }
                                    });
                        }
                    }
                });
        return Observable.just(playlists);
    }


}
