package code.name.monkey.retromusic.mvp.contract


import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

import java.util.ArrayList


/**
 * Created by hemanths on 10/08/17.
 */

interface SongContract {

    interface SongView : BaseView<ArrayList<Song>>

    interface Presenter : BasePresenter<SongView> {
        fun loadSongs()
    }
}
