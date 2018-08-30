package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.AlbumContract;


/**
 * Created by hemanths on 12/08/17.
 */

public class AlbumPresenter extends Presenter implements AlbumContract.Presenter {
    @NonNull
    private AlbumContract.AlbumView view;


    public AlbumPresenter(@NonNull AlbumContract.AlbumView view) {
        this.view = view;
    }

    @Override
    public void subscribe() {
        loadAlbums();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    private void showList(@NonNull ArrayList<Album> albums) {
        view.showData(albums);
    }

    @Override
    public void loadAlbums() {
        disposable.add(repository.getAllAlbums()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(this::showList,
                        throwable -> view.showEmptyView(),
                        () -> view.completed()));
    }
}
