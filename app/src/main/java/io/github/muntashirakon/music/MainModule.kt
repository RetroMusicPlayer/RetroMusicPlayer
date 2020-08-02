package io.github.muntashirakon.music

import io.github.muntashirakon.music.activities.albums.AlbumDetailsViewModel
import io.github.muntashirakon.music.activities.artists.ArtistDetailsViewModel
import io.github.muntashirakon.music.activities.genre.GenreDetailsViewModel
import io.github.muntashirakon.music.activities.playlist.PlaylistDetailsViewModel
import io.github.muntashirakon.music.activities.search.SearchViewModel
import io.github.muntashirakon.music.fragments.LibraryViewModel
import io.github.muntashirakon.music.model.Genre
import io.github.muntashirakon.music.model.Playlist
import io.github.muntashirakon.music.network.networkModule
import io.github.muntashirakon.music.providers.RepositoryImpl
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
        AlbumDetailsViewModel(get(), albumId)
    }

    viewModel { (artistId: Int) ->
        ArtistDetailsViewModel(get(), artistId)
    }

    viewModel { (playlist: Playlist) ->
        PlaylistDetailsViewModel(get(), playlist)
    }

    viewModel { (genre: Genre) ->
        GenreDetailsViewModel(get(), genre)
    }

    viewModel {
        SearchViewModel(get())
    }
}

val appModules = listOf(dataModule, viewModules, networkModule)