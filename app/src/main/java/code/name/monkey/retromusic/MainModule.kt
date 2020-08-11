package code.name.monkey.retromusic

import code.name.monkey.retromusic.fragments.albums.AlbumDetailsViewModel
import code.name.monkey.retromusic.fragments.artists.ArtistDetailsViewModel
import code.name.monkey.retromusic.fragments.genres.GenreDetailsViewModel
import code.name.monkey.retromusic.fragments.playlists.PlaylistDetailsViewModel
import code.name.monkey.retromusic.fragments.search.SearchViewModel
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.network.networkModule
import code.name.monkey.retromusic.providers.RepositoryImpl
import org.eclipse.egit.github.core.Repository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val dataModule = module {
    single {
        RepositoryImpl(get(), get())
    } bind Repository::class
}

private val viewModules = module {

    viewModel {
        LibraryViewModel(get())
    }

    viewModel { (albumId: Int) ->
        AlbumDetailsViewModel(
            get(),
            albumId
        )
    }

    viewModel { (artistId: Int) ->
        ArtistDetailsViewModel(
            get(),
            artistId
        )
    }

    viewModel { (playlist: Playlist) ->
        PlaylistDetailsViewModel(
            get(),
            playlist
        )
    }

    viewModel { (genre: Genre) ->
        GenreDetailsViewModel(
            get(),
            genre
        )
    }

    viewModel {
        SearchViewModel(get())
    }
}

val appModules = listOf(dataModule, viewModules, networkModule)