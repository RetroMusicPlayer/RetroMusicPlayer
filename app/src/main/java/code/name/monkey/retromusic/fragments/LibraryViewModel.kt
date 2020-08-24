package code.name.monkey.retromusic.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.toPlayCount
import code.name.monkey.retromusic.fragments.ReloadType.*
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.MusicServiceEventListener
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.repository.RealRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: RealRepository
) : ViewModel(), MusicServiceEventListener {

    private val paletteColor = MutableLiveData<Int>()
    private val albums = MutableLiveData<List<Album>>()
    private val songs = MutableLiveData<List<Song>>()
    private val artists = MutableLiveData<List<Artist>>()
    private val playlists = MutableLiveData<List<Playlist>>()
    private val roomPlaylists = MutableLiveData<List<PlaylistWithSongs>>()
    private val genres = MutableLiveData<List<Genre>>()
    private val home = MutableLiveData<List<Home>>()

    val paletteColorLiveData: LiveData<Int> = paletteColor
    val homeLiveData: LiveData<List<Home>> = home
    val albumsLiveData: LiveData<List<Album>> = albums
    val songsLiveData: LiveData<List<Song>> = songs
    val artistsLiveData: LiveData<List<Artist>> = artists
    val playlisitsLiveData: LiveData<List<Playlist>> = playlists
    val roomPlaylistsLiveData: LiveData<List<PlaylistWithSongs>> = roomPlaylists
    val genresLiveData: LiveData<List<Genre>> = genres

    init {
        viewModelScope.launch {
            loadLibraryContent()
        }
    }

    private fun loadLibraryContent() = viewModelScope.launch {
        home.value = loadHome.await()
        songs.value = loadSongs.await()
        albums.value = loadAlbums.await()
        artists.value = loadArtists.await()
        playlists.value = loadPlaylists.await()
        roomPlaylists.value = loadPlaylistsWithSongs.await()
        genres.value = loadGenres.await()
    }

    private val loadHome: Deferred<List<Home>>
        get() = viewModelScope.async { repository.homeSections() }

    private val loadSongs: Deferred<List<Song>>
        get() = viewModelScope.async(IO) { repository.allSongs() }

    private val loadAlbums: Deferred<List<Album>>
        get() = viewModelScope.async(IO) {
            repository.allAlbums()
        }

    private val loadArtists: Deferred<List<Artist>>
        get() = viewModelScope.async(IO) {
            repository.albumArtists()
        }

    private val loadPlaylists: Deferred<List<Playlist>>
        get() = viewModelScope.async(IO) {
            repository.allPlaylists()
        }
    private val loadPlaylistsWithSongs: Deferred<List<PlaylistWithSongs>>
        get() = viewModelScope.async(IO) {
            repository.playlistWithSongs()
        }

    private val loadGenres: Deferred<List<Genre>>
        get() = viewModelScope.async(IO) {
            repository.allGenres()
        }


    fun forceReload(reloadType: ReloadType) = viewModelScope.launch {
        println(reloadType)
        when (reloadType) {
            Songs -> songs.value = loadSongs.await()
            Albums -> albums.value = loadAlbums.await()
            Artists -> artists.value = loadArtists.await()
            HomeSections -> home.value = loadHome.await()
            Playlists -> roomPlaylists.value = loadPlaylistsWithSongs.await()
            Genres -> genres.value = loadGenres.await()
        }
    }

    fun updateColor(newColor: Int) {
        paletteColor.postValue(newColor)
    }

    override fun onMediaStoreChanged() {
        loadLibraryContent()
        println("onMediaStoreChanged")
    }


    override fun onServiceConnected() {
        println("onServiceConnected")
    }

    override fun onServiceDisconnected() {
        println("onServiceDisconnected")
    }

    override fun onQueueChanged() {
        println("onQueueChanged")
    }

    override fun onPlayingMetaChanged() {
        println("onPlayingMetaChanged")
        viewModelScope.launch(IO) {
            val entity = repository.songPresentInHistory(MusicPlayerRemote.currentSong)
            if (entity != null) {
                repository.updateHistorySong(MusicPlayerRemote.currentSong)
            } else {
                repository.addSongToHistory(MusicPlayerRemote.currentSong)
            }
            val songs = repository.checkSongExistInPlayCount(MusicPlayerRemote.currentSong.id)
            if (songs.isNotEmpty()) {
                repository.updateSongInPlayCount(songs.first().apply {
                    playCount += playCount + 1
                })
            } else {
                repository.insertSongInPlayCount(MusicPlayerRemote.currentSong.toPlayCount())
            }
        }
    }

    override fun onPlayStateChanged() {
        println("onPlayStateChanged")
    }

    override fun onRepeatModeChanged() {
        println("onRepeatModeChanged")
    }

    override fun onShuffleModeChanged() {
        println("onShuffleModeChanged")
    }

}

enum class ReloadType {
    Songs,
    Albums,
    Artists,
    HomeSections,
    Playlists,
    Genres,
}