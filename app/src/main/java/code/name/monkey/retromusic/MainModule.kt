package code.name.monkey.retromusic

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import code.name.monkey.retromusic.auto.AutoMusicProvider
import code.name.monkey.retromusic.db.BlackListStoreDao
import code.name.monkey.retromusic.db.BlackListStoreEntity
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.RetroDatabase
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.albums.AlbumDetailsViewModel
import code.name.monkey.retromusic.fragments.artists.ArtistDetailsViewModel
import code.name.monkey.retromusic.fragments.genres.GenreDetailsViewModel
import code.name.monkey.retromusic.fragments.playlists.PlaylistDetailsViewModel
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.network.provideDefaultCache
import code.name.monkey.retromusic.network.provideLastFmRest
import code.name.monkey.retromusic.network.provideLastFmRetrofit
import code.name.monkey.retromusic.network.provideOkHttp
import code.name.monkey.retromusic.repository.*
import code.name.monkey.retromusic.util.FilePathUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

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
}

private val roomModule = module {

    single {
        Room.databaseBuilder(androidContext(), RetroDatabase::class.java, "playlist.db")
            .allowMainThreadQueries()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    GlobalScope.launch(IO) {
                        FilePathUtil.blacklistFilePaths().map {
                            get<BlackListStoreDao>().insertBlacklistPath(BlackListStoreEntity(it))
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
    factory {
        get<RetroDatabase>().lyricsDao()
    }

    factory {
        get<RetroDatabase>().playlistDao()
    }

    factory {
        get<RetroDatabase>().blackListStore()
    }

    factory {
        get<RetroDatabase>().playCountDao()
    }

    factory {
        get<RetroDatabase>().historyDao()
    }

    single {
        RealRoomRepository(get(), get(), get(), get(), get())
    } bind RoomRepository::class
}
private val autoModule = module {
    single {
        AutoMusicProvider(androidContext(),
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
        RealLocalDataRepository(get())
    } bind LocalDataRepository::class
}

private val viewModules = module {

    viewModel {
        LibraryViewModel(get())
    }

    viewModel { (albumId: Long) ->
        AlbumDetailsViewModel(
            get(),
            albumId
        )
    }

    viewModel { (artistId: Long?, artistName: String?) ->
        ArtistDetailsViewModel(
            get(),
            artistId,
            artistName
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
}

val appModules = listOf(mainModule, dataModule, autoModule, viewModules, networkModule, roomModule)