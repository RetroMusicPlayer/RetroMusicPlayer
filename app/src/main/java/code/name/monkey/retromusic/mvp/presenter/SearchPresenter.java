package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.SearchContract;

/**
 * Created by hemanths on 20/08/17.
 */

public class SearchPresenter extends Presenter implements SearchContract.SearchPresenter {
    @NonNull
    private SearchContract.SearchView mView;

    public SearchPresenter(@NonNull SearchContract.SearchView view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        search("");
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    private void showList(@NonNull ArrayList<Object> albums) {
        if (albums.isEmpty()) {
            mView.showEmptyView();
        } else {
            mView.showData(albums);
        }
    }

    @Override
    public void search(String query) {
        disposable.add(repository.search(query)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> mView.loading())
                .subscribe(this::showList,
                        throwable -> mView.showEmptyView(),
                        () -> mView.completed()));
    }
}
