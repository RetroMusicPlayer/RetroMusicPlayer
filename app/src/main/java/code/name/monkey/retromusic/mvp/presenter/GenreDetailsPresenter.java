package code.name.monkey.retromusic.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.GenreDetailsContract;


/**
 * Created by hemanths on 20/08/17.
 */

public class GenreDetailsPresenter extends Presenter
        implements GenreDetailsContract.Presenter {
    private final int genreId;
    @NonNull
    private GenreDetailsContract.GenreDetailsView view;

    public GenreDetailsPresenter(@NonNull GenreDetailsContract.GenreDetailsView view,
                                 int genreId) {
        this.view = view;
        this.genreId = genreId;
    }

    @Override
    public void subscribe() {
        loadGenre(genreId);
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void loadGenre(int genreId) {
        disposable.add(repository.getGenre(genreId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(this::showGenre,
                        throwable -> view.showEmptyView(),
                        () -> view.completed()));
    }

    private void showGenre(ArrayList<Song> songs) {
        if (songs != null) {
            view.showData(songs);
        } else {
            view.showEmptyView();
        }
    }
}
