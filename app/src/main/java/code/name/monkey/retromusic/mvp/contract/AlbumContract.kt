package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView
import java.util.ArrayList

interface AlbumContract {

    interface AlbumView : BaseView<ArrayList<Album>>

    interface Presenter : BasePresenter<AlbumView> {

        fun loadAlbums()
    }

}
