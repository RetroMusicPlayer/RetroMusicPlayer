package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.Result.Error
import code.name.monkey.retromusic.Result.Success
import code.name.monkey.retromusic.model.Contributor
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface AboutView {
    fun showContributors(contributor: List<Contributor>)
    fun translators(contributor: List<Contributor>)

}

interface AboutPresenter : Presenter<AboutView> {
    fun loadContributors()
    fun loadTranslators()
    class AboutPresenterImpl @Inject constructor(
        private val repository: Repository
    ) : PresenterImpl<AboutView>(), AboutPresenter, CoroutineScope {

        override fun loadContributors() {
            launch {
                when (val result = repository.contributors()) {
                    is Success -> withContext(Dispatchers.Main) {
                        view.showContributors(
                            result.data
                        )
                    }
                    is Error -> withContext(Dispatchers.Main) {}
                }
            }
        }

        override fun loadTranslators() {
            launch {
                when (val result = repository.translators()) {
                    is Success -> withContext(Dispatchers.Main) {
                        view.translators(
                            result.data
                        )
                    }
                    is Error -> withContext(Dispatchers.Main) {}
                }
            }
        }

        private var job: Job = Job()
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

    }
}