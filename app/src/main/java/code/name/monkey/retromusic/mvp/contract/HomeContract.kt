package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

interface HomeContract {

    interface HomeView : BaseView<ArrayList<Home>> {
        fun showEmpty()
    }

    interface HomePresenter : BasePresenter<HomeView> {

        fun homeSections();
    }
}