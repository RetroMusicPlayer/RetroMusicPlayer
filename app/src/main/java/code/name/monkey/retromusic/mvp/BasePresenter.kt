package code.name.monkey.retromusic.mvp

/**
 * Created by hemanths on 09/08/17.
 */

interface BasePresenter<T> {

    fun subscribe()

    fun unsubscribe()
}
