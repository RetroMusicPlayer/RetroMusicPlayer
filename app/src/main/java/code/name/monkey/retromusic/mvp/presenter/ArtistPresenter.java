package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.ArtistContract;

public class ArtistPresenter extends Presenter implements ArtistContract.Presenter {
    @NonNull
    private ArtistContract.ArtistView mView;

    public ArtistPresenter(@NonNull ArtistContract.ArtistView view) {
        mView = view;
    }

    @Override
    public void subscribe() {
        loadArtists();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    private void showList(@NonNull ArrayList<Artist> songs) {
        if (songs.isEmpty()) {
            mView.showEmptyView();
        } else {
            mView.showData(songs);
        }
    }

    @Override
    public void loadArtists() {
        disposable.add(repository.getAllArtists()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> mView.loading())
                .subscribe(this::showList,
                        throwable -> mView.showEmptyView(),
                        () -> mView.completed()));
    }
}
