package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView
import java.util.*


/**
 * Created by hemanths on 20/08/17.
 */

interface SearchContract {
    interface SearchView : BaseView<ArrayList<Any>>

    interface SearchPresenter : BasePresenter<SearchView> {
        fun search(query: String?)
    }
}
