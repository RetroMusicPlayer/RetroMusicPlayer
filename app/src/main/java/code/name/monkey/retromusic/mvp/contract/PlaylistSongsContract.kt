package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

import java.util.ArrayList


/**
 * Created by hemanths on 20/08/17.
 */

interface PlaylistSongsContract {
    interface PlaylistSongsView : BaseView<ArrayList<Song>>

    interface Presenter : BasePresenter<PlaylistSongsView> {
        fun loadSongs(playlist: Playlist)
    }
}
