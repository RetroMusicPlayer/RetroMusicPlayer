package code.name.monkey.retromusic.mvp.contract;

import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

import java.util.ArrayList;


/**
 * Created by hemanths on 20/08/17.
 */

public interface PlaylistSongsContract {
    interface PlaylistSongsView extends BaseView<ArrayList<Song>> {

    }

    interface Presenter extends BasePresenter<PlaylistSongsView> {
        void loadSongs(Playlist playlist);
    }
}
