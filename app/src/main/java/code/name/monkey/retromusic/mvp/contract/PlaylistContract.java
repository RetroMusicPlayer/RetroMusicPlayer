package code.name.monkey.retromusic.mvp.contract;

import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by hemanths on 19/08/17.
 */

public interface PlaylistContract {
    interface PlaylistView extends BaseView<ArrayList<Playlist> > {

    }

    interface Presenter extends BasePresenter<PlaylistView> {
        void loadPlaylists();
    }
}
