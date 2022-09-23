package code.name.monkey.retromusic

import androidx.room.Room
import code.name.monkey.retromusic.auto.AutoMusicProvider
import code.name.monkey.retromusic.cast.RetroWebServer
import code.name.monkey.retromusic.db.MIGRATION_23_24
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.RetroDatabase
import code.name.monkey.retromusic.feature.details.album.detailsAlbumModule
import code.name.monkey.retromusic.feature.details.artist.detailsArtistModule
import code.name.monkey.retromusic.feature.library.album.libraryAlbumModule
import code.name.monkey.retromusic.feature.library.artist.libraryArtistModule
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.genres.GenreDetailsViewModel
import code.name.monkey.retromusic.fragments.playlists.PlaylistDetailsViewModel
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.network.provideDefaultCache
import code.name.monkey.retromusic.network.provideLastFmRest
import code.name.monkey.retromusic.network.provideLastFmRetrofit
import code.name.monkey.retromusic.network.provideOkHttp
import code.name.monkey.retromusic.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.stersh.apisonic.ApiSonic
import ru.stersh.retrosonic.core.storage.coreModule

val networkModule = module {

    factory {
        provideDefaultCache()
    }
    factory {
        provideOkHttp(get(), get())
    }
    single {
        provideLastFmRetrofit(get())
    }
    single {
        provideLastFmRest(get())
    }

    single {
        ApiSonic(
            url = "http://192.168.1.100:4040/rest/",
            userName = "admin",
            password = "admin",
            apiVersion = "1.15.0",
            clientId = "retro"
        )
    }
}

private val roomModule = module {

    single {
        Room.databaseBuilder(androidContext(), RetroDatabase::class.java, "playlist.db")
            .addMigrations(MIGRATION_23_24)
            .build()
    }

    factory {
        get<RetroDatabase>().playlistDao()
    }

    factory {
        get<RetroDatabase>().playCountDao()
    }

    factory {
        get<RetroDatabase>().historyDao()
    }

    single {
        RealRoomRepository(get(), get(), get())
    } bind RoomRepository::class
}
private val autoModule = module {
    single {
        AutoMusicProvider(
            androidContext(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
private val mainModule = module {
    single {
        androidContext().contentResolver
    }
    single {
        RetroWebServer(get())
    }
}
private val dataModule = module {
    single {
        RealRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    } bind Repository::class

    single {
        RealSongRepository(get())
    } bind SongRepository::class

    single {
        RealGenreRepository(get(), get())
    } bind GenreRepository::class

    single {
        RealAlbumRepository(get(), get())
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
        RealLocalDataRepository(get())
    } bind LocalDataRepository::class
}

private val viewModules = module {

    viewModel {
        LibraryViewModel(get())
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
}

val appModules = listOf(
    mainModule,
    dataModule,
    autoModule,
    viewModules,
    networkModule,
    roomModule,
    libraryAlbumModule,
    detailsAlbumModule,
    libraryArtistModule,
    detailsArtistModule,
    coreModule
)