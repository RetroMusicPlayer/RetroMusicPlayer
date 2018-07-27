package code.name.monkey.retromusic.mvp.contract;

import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

import java.util.ArrayList;

/**
 * @author Hemanth S (h4h13).
 */

public interface GenreContract {
    interface GenreView extends BaseView<ArrayList<Genre>> {

    }

    interface Presenter extends BasePresenter<GenreView> {
        void loadGenre();
    }
}
