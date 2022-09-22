package code.name.monkey.retromusic.feature.library.artist

import code.name.monkey.retromusic.feature.library.artist.data.LibraryArtistRepositoryImpl
import code.name.monkey.retromusic.feature.library.artist.domain.LibraryArtistRepository
import code.name.monkey.retromusic.feature.library.artist.presentation.LibraryArtistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryArtistModule = module {
    single<LibraryArtistRepository> { LibraryArtistRepositoryImpl(get()) }
    viewModel { LibraryArtistViewModel(get()) }
}