package code.name.monkey.retromusic.providers

import org.eclipse.egit.github.core.Repository
import org.koin.dsl.bind
import org.koin.dsl.module

val provideModules = module {
    single {
        RepositoryImpl(get())
    } bind Repository::class
}