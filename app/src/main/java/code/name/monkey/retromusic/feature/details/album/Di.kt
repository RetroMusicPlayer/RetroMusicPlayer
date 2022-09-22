package code.name.monkey.retromusic.feature.details.album

import code.name.monkey.retromusic.feature.details.album.data.AlbumDetailsRepositoryImpl
import code.name.monkey.retromusic.feature.details.album.domain.AlbumDetailsRepository
import code.name.monkey.retromusic.feature.details.album.presentation.AlbumDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsAlbumModule = module {
    single<AlbumDetailsRepository> { AlbumDetailsRepositoryImpl(get()) }
    viewModel { (albumId: String) ->
        AlbumDetailsViewModel(get(), albumId)
    }
}