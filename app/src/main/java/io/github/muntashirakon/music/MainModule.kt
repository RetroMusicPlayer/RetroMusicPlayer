package code.name.monkey.retromusic

import code.name.monkey.retromusic.activities.albums.AlbumDetailsViewModel
import code.name.monkey.retromusic.activities.artists.ArtistDetailsViewModel
import code.name.monkey.retromusic.activities.genre.GenreDetailsViewModel
import code.name.monkey.retromusic.activities.playlist.PlaylistDetailsViewModel
import code.name.monkey.retromusic.activities.search.SearchViewModel
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Playlist
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mainModule = module {

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