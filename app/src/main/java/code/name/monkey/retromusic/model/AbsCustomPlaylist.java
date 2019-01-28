package code.name.monkey.retromusic.model;

import android.content.Context;
import android.os.Parcel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import code.name.monkey.models.Song;
import io.reactivex.Observable;


public abstract class AbsCustomPlaylist extends Playlist {
    public AbsCustomPlaylist(int id, String name) {
        super(id, name);
    }

    public AbsCustomPlaylist() {
    }

    public AbsCustomPlaylist(Parcel in) {
        super(in);
    }

    @NonNull
    public abstract Observable<ArrayList<Song>> getSongs(Context context);
}

