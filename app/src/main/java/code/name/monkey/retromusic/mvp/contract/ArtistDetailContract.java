package code.name.monkey.retromusic.mvp.contract;


import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.mvp.BasePresenter;
import code.name.monkey.retromusic.mvp.BaseView;


/**
 * Created by hemanths on 20/08/17.
 */

public interface ArtistDetailContract {
    interface ArtistsDetailsView extends BaseView<Artist> {

    }

    interface Presenter extends BasePresenter<ArtistsDetailsView> {
        void loadArtistById();
    }
}
