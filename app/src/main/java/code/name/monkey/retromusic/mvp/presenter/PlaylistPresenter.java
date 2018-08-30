package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.PlaylistContract;


/**
 * Created by hemanths on 19/08/17.
 */

public class PlaylistPresenter extends Presenter
        implements PlaylistContract.Presenter {
    @NonNull
    private PlaylistContract.PlaylistView mView;

    public PlaylistPresenter(@NonNull PlaylistContract.PlaylistView view) {

        mView = view;
    }

    @Override
    public void subscribe() {
        loadPlaylists();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void loadPlaylists() {
        disposable.add(repository.getAllPlaylists()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> mView.loading())
                .subscribe(this::showList,
                        throwable -> mView.showEmptyView(),
                        () -> mView.completed()));
    }

    private void showList(@NonNull ArrayList<Playlist> songs) {
        if (songs.isEmpty()) {
            mView.showEmptyView();
        } else {
            mView.showData(songs);
        }
    }
}
