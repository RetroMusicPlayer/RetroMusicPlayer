package code.name.monkey.retromusic.feature.library.album

import code.name.monkey.retromusic.feature.library.album.data.LibraryAlbumRepositoryImpl
import code.name.monkey.retromusic.feature.library.album.domain.LibraryAlbumRepository
import code.name.monkey.retromusic.feature.library.album.presentation.LibraryAlbumViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryAlbumModule = module {
    viewModel { LibraryAlbumViewModel(get()) }
    single<LibraryAlbumRepository> { LibraryAlbumRepositoryImpl(get()) }
}