package code.name.monkey.retromusic.mvp.contract;

import java.util.ArrayList;

import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

public interface HomeContract {

    interface HomeView extends BaseView<ArrayList<Object>> {

        void recentArtist(ArrayList<Artist> artists);

        void recentAlbum(ArrayList<Album> albums);

        void topArtists(ArrayList<Artist> artists);

        void topAlbums(ArrayList<Album> albums);

        void suggestions(ArrayList<Song> songs);

        void playlists(ArrayList<Playlist> playlists);

        void geners(ArrayList<Genre> songs);
    }

    interface HomePresenter extends BasePresenter<HomeView> {

        void loadRecentAlbums();

        void loadTopAlbums();

        void loadRecentArtists();

        void loadTopArtists();

        void loadSuggestions();

        void loadGenres();
    }
}