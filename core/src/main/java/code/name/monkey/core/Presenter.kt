package code.name.monkey.core

interface Presenter<T> {
    fun attachView(view: T)

    fun detachView()
}