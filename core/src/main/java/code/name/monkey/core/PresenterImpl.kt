package code.name.monkey.core

class PresenterImpl<T> {
    var view: T? = null

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }
}