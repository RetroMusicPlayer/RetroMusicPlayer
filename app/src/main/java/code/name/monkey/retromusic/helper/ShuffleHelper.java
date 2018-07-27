package code.name.monkey.retromusic.helper;

import android.support.annotation.NonNull;

import code.name.monkey.retromusic.model.Song;

import java.util.Collections;
import java.util.List;


public class ShuffleHelper {

    public static void makeShuffleList(@NonNull List<Song> listToShuffle, final int current) {
        if (listToShuffle.isEmpty()) return;
        if (current >= 0) {
            Song song = listToShuffle.remove(current);
            Collections.shuffle(listToShuffle);
            listToShuffle.add(0, song);
        } else {
            Collections.shuffle(listToShuffle);
        }
    }


}
