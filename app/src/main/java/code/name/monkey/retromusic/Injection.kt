package code.name.monkey.retromusic

import code.name.monkey.retromusic.providers.RepositoryImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import code.name.monkey.retromusic.rest.KogouClient
import code.name.monkey.retromusic.rest.service.KuGouApiService
import code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider
import code.name.monkey.retromusic.util.schedulers.SchedulerProvider

object Injection {

    fun provideRepository(): Repository {
        return RepositoryImpl.instance
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider.getInstance()
    }

    fun provideKuGouApiService(): KuGouApiService {
        return KogouClient().apiService
    }
}
