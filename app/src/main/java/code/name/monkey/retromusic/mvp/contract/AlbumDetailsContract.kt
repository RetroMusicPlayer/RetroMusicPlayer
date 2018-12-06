package code.name.monkey.retromusic.mvp.contract


import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

interface AlbumDetailsContract {

    interface AlbumDetailsView : BaseView<Album>

    interface Presenter : BasePresenter<AlbumDetailsView> {

        fun loadAlbumSongs(albumId: Int)
    }
}
