package code.name.monkey.retromusic

import code.name.monkey.retromusic.db.PlaylistDatabase
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.RealRoomPlaylistRepository
import code.name.monkey.retromusic.db.RoomPlaylistRepository
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.albums.AlbumDetailsViewModel
import code.name.monkey.retromusic.fragments.artists.ArtistDetailsViewModel
import code.name.monkey.retromusic.fragments.genres.GenreDetailsViewModel
import code.name.monkey.retromusic.fragments.playlists.PlaylistDetailsViewModel
import code.name.monkey.retromusic.fragments.search.SearchViewModel
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.network.networkModule
import code.name.monkey.retromusic.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val mainModule = module {
    single {
        androidContext().contentResolver
    }

}
private val dataModule = module {
    single {
        RealRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
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
        PlaylistDatabase.getDatabase(get()).playlistDao()
    }

    single {
        RealRoomPlaylistRepository(get())
    } bind RoomPlaylistRepository::class
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

    viewModel { (playlist: PlaylistWithSongs) ->
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

val appModules = listOf(mainModule, dataModule, viewModules, networkModule)