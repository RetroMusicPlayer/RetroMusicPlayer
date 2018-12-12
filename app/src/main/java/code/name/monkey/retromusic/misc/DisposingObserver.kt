package code.name.monkey.retromusic.misc

import androidx.annotation.CallSuper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class DisposingObserver<T> : Observer<T> {
    @CallSuper
    override fun onSubscribe(d: Disposable) {
        DisposableManager.add(d)
    }

    override fun onNext(next: T) {}

    override fun onError(e: Throwable) {}

    override fun onComplete() {}
}