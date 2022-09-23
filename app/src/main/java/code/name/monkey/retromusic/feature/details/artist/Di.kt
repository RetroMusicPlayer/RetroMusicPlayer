package code.name.monkey.retromusic.feature.details.artist

import code.name.monkey.retromusic.feature.details.artist.data.ArtistDetailsRepositoryImpl
import code.name.monkey.retromusic.feature.details.artist.domain.ArtistDetailsRepository
import code.name.monkey.retromusic.feature.details.artist.presentation.ArtistDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsArtistModule = module {
    single<ArtistDetailsRepository> { ArtistDetailsRepositoryImpl(get()) }
    viewModel { (artistId: String) ->
        ArtistDetailsViewModel(artistId, get())
    }
}