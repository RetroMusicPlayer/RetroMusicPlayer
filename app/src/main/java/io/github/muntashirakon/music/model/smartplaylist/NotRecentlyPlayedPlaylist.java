package io.github.muntashirakon.music.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import io.github.muntashirakon.music.R;
import io.github.muntashirakon.music.loaders.TopAndRecentlyPlayedTracksLoader;
import io.github.muntashirakon.music.model.Song;
import io.github.muntashirakon.music.util.MusicUtil;
import io.github.muntashirakon.music.util.PreferenceUtil;

/**
 * @author SC (soncaokim)
 */
public class NotRecentlyPlayedPlaylist extends AbsSmartPlaylist {

    public static final Creator<NotRecentlyPlayedPlaylist> CREATOR = new Creator<NotRecentlyPlayedPlaylist>() {
        public NotRecentlyPlayedPlaylist createFromParcel(Parcel source) {
            return new NotRecentlyPlayedPlaylist(source);
        }

        public NotRecentlyPlayedPlaylist[] newArray(int size) {
            return new NotRecentlyPlayedPlaylist[size];
        }
    };

    public NotRecentlyPlayedPlaylist(@NonNull Context context) {
        super(context.getString(R.string.not_recently_played), R.drawable.ic_watch_later_white_24dp);
    }

    protected NotRecentlyPlayedPlaylist(Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    public String getInfoString(@NonNull Context context) {
        String cutoff = PreferenceUtil.INSTANCE.getRecentlyPlayedCutoffText(context);

        return MusicUtil.buildInfoString(
                cutoff,
                super.getInfoString(context)
        );
    }

    @NonNull
    @Override
    public ArrayList<Song> getSongs(@NonNull Context context) {
        return TopAndRecentlyPlayedTracksLoader.INSTANCE.getNotRecentlyPlayedTracks(context);
    }

    @Override
    public void clear(@NonNull Context context) {
    }

    @Override
    public boolean isClearable() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}