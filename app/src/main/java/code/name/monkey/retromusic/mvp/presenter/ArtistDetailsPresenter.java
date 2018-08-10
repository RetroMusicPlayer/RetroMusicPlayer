package code.name.monkey.retromusic.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.ArtistDetailContract;

import static code.name.monkey.retromusic.ui.activities.ArtistDetailActivity.EXTRA_ARTIST_ID;


/**
 * Created by hemanths on 20/08/17.
 */

public class ArtistDetailsPresenter extends Presenter implements ArtistDetailContract.Presenter {

    @NonNull
    private final ArtistDetailContract.ArtistsDetailsView view;
    private Bundle bundle;

    public ArtistDetailsPresenter(@NonNull ArtistDetailContract.ArtistsDetailsView view,
                                  Bundle artistId) {
        this.view = view;
        this.bundle = artistId;
    }

    @Override
    public void subscribe() {
        loadArtistById();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void loadArtistById() {
        disposable.add(repository.getArtistById(bundle.getInt(EXTRA_ARTIST_ID))
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(this::showArtist,
                        throwable -> view.showEmptyView(),
                        view::completed));
    }

    private void showArtist(Artist album) {
        if (album != null) {
            view.showData(album);
        } else {
            view.showEmptyView();
        }
    }
}
