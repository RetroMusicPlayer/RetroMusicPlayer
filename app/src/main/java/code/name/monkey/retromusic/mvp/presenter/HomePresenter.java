package code.name.monkey.retromusic.mvp.presenter;

import androidx.annotation.NonNull;

import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.mvp.Presenter;
import code.name.monkey.retromusic.mvp.contract.HomeContract;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class HomePresenter extends Presenter implements HomeContract.HomePresenter {

    @NonNull
    private HomeContract.HomeView view;

    public HomePresenter(@NonNull HomeContract.HomeView view) {
        this.view = view;
    }

    @Override
    public void subscribe() {

        loadRecentAlbums();
        loadRecentArtists();

        loadTopAlbums();
        loadTopArtists();

        loadSuggestions();

    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    public void loadPlaylists() {
        disposable.add(repository.getAllPlaylists()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe(playlists -> {
                            if (!playlists.isEmpty()) {
                                view.suggestions(playlists);
                            }
                        },
                        throwable -> view.showEmptyView(), () -> view.completed()));
    }

    @Override
    public void loadRecentAlbums() {
        disposable.add(repository.getRecentAlbums()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(artists -> {
                            if (!artists.isEmpty()) {
                                view.recentAlbum(artists);
                            }
                        },
                        throwable -> view.showEmptyView(), () -> view.completed()));
    }

    @Override
    public void loadTopAlbums() {
        disposable.add(repository.getTopAlbums()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(artists -> {
                            if (!artists.isEmpty()) {
                                view.topAlbums(artists);
                            }
                        },
                        throwable -> view.showEmptyView(), () -> view.completed()));
    }

    @Override
    public void loadRecentArtists() {
        disposable.add(repository.getRecentArtists()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(artists -> {
                            if (!artists.isEmpty()) {
                                view.recentArtist(artists);
                            }
                        },
                        throwable -> view.showEmptyView(), () -> view.completed()));
    }

    @Override
    public void loadTopArtists() {
        disposable.add(repository.getTopArtists()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(artists -> {
                            if (!artists.isEmpty()) {
                                view.topArtists(artists);
                            }
                        },
                        throwable -> view.showEmptyView(), () -> view.completed()));

    }

    @Override
    public void loadSuggestions() {

    }

    @Override
    public void loadGenres() {
        disposable.add(repository.getAllGenres()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(genres -> {
                            if (!genres.isEmpty()) {
                                view.geners(genres);
                            }
                        },
                        throwable -> view.showEmptyView(), () -> view.completed()));
    }
}
