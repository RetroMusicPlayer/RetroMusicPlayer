package code.name.monkey.retromusic.mvp.contract


import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

import java.util.ArrayList


/**
 * Created by hemanths on 16/08/17.
 */

interface ArtistContract {
    interface ArtistView : BaseView<ArrayList<Artist>>

    interface Presenter : BasePresenter<ArtistView> {
        fun loadArtists()
    }
}
