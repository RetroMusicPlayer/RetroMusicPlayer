package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.GenreContract;

/**
 * @author Hemanth S (h4h13).
 */

public class GenrePresenter extends Presenter
        implements GenreContract.Presenter {
    @NonNull
    private GenreContract.GenreView view;

    public GenrePresenter(
            @NonNull GenreContract.GenreView view) {
        this.view = view;
    }

    @Override
    public void subscribe() {
        loadGenre();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void loadGenre() {
        disposable.add(repository.getAllGenres()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(this::showList,
                        throwable -> view.showEmptyView(),
                        () -> view.completed()));
    }

    private void showList(@NonNull ArrayList<Genre> genres) {
        if (genres.isEmpty()) {
            view.showEmptyView();
        } else {
            view.showData(genres);
        }
    }
}
