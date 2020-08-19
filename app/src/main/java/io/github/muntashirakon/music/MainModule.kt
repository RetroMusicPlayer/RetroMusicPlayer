package io.github.muntashirakon.music

import io.github.muntashirakon.music.fragments.LibraryViewModel
import io.github.muntashirakon.music.fragments.albums.AlbumDetailsViewModel
import io.github.muntashirakon.music.fragments.artists.ArtistDetailsViewModel
import io.github.muntashirakon.music.fragments.genres.GenreDetailsViewModel
import io.github.muntashirakon.music.fragments.playlists.PlaylistDetailsViewModel
import io.github.muntashirakon.music.fragments.search.SearchViewModel
import io.github.muntashirakon.music.model.Genre
import io.github.muntashirakon.music.model.Playlist
import io.github.muntashirakon.music.network.networkModule
import io.github.muntashirakon.music.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val dataModule = module {
    single {
        RealRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    } bind Repository::class

    single {
        RealSongRepository(get())
    } bind SongRepository::class

    single {
        RealGenreRepository(get(), get())
    } bind GenreRepository::class

    single {
        RealAlbumRepository(get())
    } bind AlbumRepository::class

    single {
        RealArtistRepository(get(), get())
    } bind ArtistRepository::class

    single {
        RealPlaylistRepository(get())
    } bind PlaylistRepository::class

    single {
        RealTopPlayedRepository(get(), get(), get(), get())
    } bind TopPlayedRepository::class

    single {
        RealLastAddedRepository(
            get(),
            get(),
            get()
        )
    } bind LastAddedRepository::class

    single {
        RealSearchRepository(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    single {
        androidContext().contentResolver
    }
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