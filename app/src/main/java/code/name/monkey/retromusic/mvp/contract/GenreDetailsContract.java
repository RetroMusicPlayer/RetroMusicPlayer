package code.name.monkey.retromusic.mvp.contract;

import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

import java.util.ArrayList;

/**
 * @author Hemanth S (h4h13).
 */

public interface GenreDetailsContract {
    interface GenreDetailsView extends BaseView<ArrayList<Song>> {
    }

    interface Presenter extends BasePresenter<GenreDetailsView> {
        void loadGenre(int genreId);
    }
}
