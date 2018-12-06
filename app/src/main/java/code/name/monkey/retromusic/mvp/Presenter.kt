package code.name.monkey.retromusic.mvp

import code.name.monkey.retromusic.Injection
import code.name.monkey.retromusic.providers.interfaces.Repository
import code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by hemanths on 16/08/17.
 */

open class Presenter {
    protected var repository: Repository = Injection.provideRepository()
    protected var disposable: CompositeDisposable = CompositeDisposable()
    protected var schedulerProvider: BaseSchedulerProvider = Injection.provideSchedulerProvider()
}
