package code.name.monkey.retromusic.mvp.contract;

import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

import java.util.ArrayList;


/**
 * Created by hemanths on 20/08/17.
 */

public interface SearchContract {
    interface SearchView extends BaseView<ArrayList<Object>> {

    }

    interface SearchPresenter extends BasePresenter<SearchView> {
        void search(String query);
    }
}
