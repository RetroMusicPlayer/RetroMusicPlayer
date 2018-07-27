package code.name.monkey.retromusic.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.loaders.TopAndRecentlyPlayedTracksLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.providers.HistoryStore;
import io.reactivex.Observable;


public class HistoryPlaylist extends AbsSmartPlaylist {

    public static final Parcelable.Creator<HistoryPlaylist> CREATOR = new Parcelable.Creator<HistoryPlaylist>() {
        public HistoryPlaylist createFromParcel(Parcel source) {
            return new HistoryPlaylist(source);
        }

        public HistoryPlaylist[] newArray(int size) {
            return new HistoryPlaylist[size];
        }
    };

    public HistoryPlaylist(@NonNull Context context) {
        super(context.getString(R.string.history), R.drawable.ic_access_time_white_24dp);
    }

    protected HistoryPlaylist(Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    public Observable<ArrayList<Song>> getSongs(@NonNull Context context) {
        return TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
    }

    @Override
    public void clear(@NonNull Context context) {
        HistoryStore.getInstance(context).clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
