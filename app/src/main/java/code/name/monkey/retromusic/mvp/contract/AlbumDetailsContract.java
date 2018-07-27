package code.name.monkey.retromusic.mvp.contract;


import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;

public interface AlbumDetailsContract {

  interface AlbumDetailsView extends BaseView<Album> {

  }

  interface Presenter extends BasePresenter<AlbumDetailsView> {

    void loadAlbumSongs(int albumId);
  }
}
