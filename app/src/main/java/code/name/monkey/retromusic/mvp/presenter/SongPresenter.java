package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.SongContract;

/**
 * Created by hemanths on 10/08/17.
 */

public class SongPresenter extends Presenter implements SongContract.Presenter {

    @NonNull
    private SongContract.SongView view;


    public SongPresenter(@NonNull SongContract.SongView view) {
        this.view = view;
    }

    @Override
    public void loadSongs() {
        disposable.add(repository.getAllSongs()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(this::showList,
                        throwable -> view.showEmptyView(),
                        () -> view.completed()));
    }

    @Override
    public void subscribe() {
        loadSongs();
    }

    private void showList(@NonNull ArrayList<Song> songs) {
        if (songs.isEmpty()) {
            view.showEmptyView();
        } else {
            view.showData(songs);
        }
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }
}
