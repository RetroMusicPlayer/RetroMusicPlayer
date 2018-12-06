package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.SearchContract
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hemanths on 20/08/17.
 */

class SearchPresenter(private val view: SearchContract.SearchView) : Presenter(), SearchContract.SearchPresenter {

    override fun subscribe() {
        search("")
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    private fun showList(albums: ArrayList<Any>) {
        if (albums.isEmpty()) {
            view.showEmptyView()
        } else {
            view.showData(albums)
        }
    }

    override fun search(query: String) {
        disposable.add(repository.search(query)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }
}
