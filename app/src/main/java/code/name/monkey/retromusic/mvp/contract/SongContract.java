package code.name.monkey.retromusic.mvp.contract;


import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

import java.util.ArrayList;


/**
 * Created by hemanths on 10/08/17.
 */

public interface SongContract {

    interface SongView extends BaseView<ArrayList<Song>> {

    }

    interface Presenter extends BasePresenter<SongView> {
        void loadSongs();
    }
}
