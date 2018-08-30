package code.name.monkey.retromusic.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import code.name.monkey.retromusic.model.Song;

import java.util.ArrayList;

import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.Observable;


public class ArtistSongLoader extends SongLoader {

    @NonNull
    public static Observable<ArrayList<Song>> getArtistSongList(@NonNull final Context context, final int artistId) {
        return getSongs(makeArtistSongCursor(context, artistId));
    }

    public static Cursor makeArtistSongCursor(@NonNull final Context context, final int artistId) {
        try {
            return makeSongCursor(
                    context,
                    MediaStore.Audio.AudioColumns.ARTIST_ID + "=?",
                    new String[]{
                            String.valueOf(artistId)
                    },
                    PreferenceUtil.getInstance(context).getArtistSongSortOrder()
            );
        } catch (SecurityException e) {
            return null;
        }
    }
}