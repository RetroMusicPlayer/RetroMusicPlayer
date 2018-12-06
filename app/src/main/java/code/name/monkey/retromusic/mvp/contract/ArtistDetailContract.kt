package code.name.monkey.retromusic.mvp.contract


import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView


/**
 * Created by hemanths on 20/08/17.
 */

interface ArtistDetailContract {
    interface ArtistsDetailsView : BaseView<Artist>

    interface Presenter : BasePresenter<ArtistsDetailsView> {
        fun loadArtistById()
    }
}
