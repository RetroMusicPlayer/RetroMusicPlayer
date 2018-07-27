package code.name.monkey.retromusic.mvp.contract;

import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;
import java.util.ArrayList;

public interface AlbumContract {

  interface AlbumView extends BaseView<ArrayList<Album>> {

  }

  interface Presenter extends BasePresenter<AlbumView> {

    void loadAlbums();
  }

}
