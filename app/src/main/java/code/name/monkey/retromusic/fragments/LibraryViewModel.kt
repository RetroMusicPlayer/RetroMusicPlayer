package code.name.monkey.retromusic.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result.Error
import code.name.monkey.retromusic.Result.Success
import code.name.monkey.retromusic.fragments.ReloadType.*
import code.name.monkey.retromusic.interfaces.MusicServiceEventListener
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.providers.RepositoryImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) :
    AndroidViewModel(application), MusicServiceEventListener {

    private val _repository: Repository = RepositoryImpl(application.applicationContext)
    private val _albums = MutableLiveData<List<Album>>()
    private val _songs = MutableLiveData<List<Song>>()
    private val _artists = MutableLiveData<List<Artist>>()
    private val _playlist = MutableLiveData<List<Playlist>>()
    private val _genre = MutableLiveData<List<Genre>>()
    private val _homeSections = MutableLiveData<List<Home>>()

    fun homeSections(): LiveData<List<Home>> = _homeSections
    fun allAlbums(): LiveData<List<Album>> = _albums
    fun allSongs(): LiveData<List<Song>> = _songs
    fun allArtists(): LiveData<List<Artist>> = _artists
    fun allPlaylisits(): LiveData<List<Playlist>> = _playlist
    fun allGenres(): LiveData<List<Genre>> = _genre

    init {
        viewModelScope.launch {
            loadLibraryContent()
            loadHomeSections()
        }
    }

    private fun loadLibraryContent() = viewModelScope.launch {
        _songs.value = loadSongs.await()
        _albums.value = loadAlbums.await()
        _artists.value = loadArtists.await()
        _playlist.value = loadPlaylists.await()
        _genre.value = loadGenres.await()
    }

    private fun loadHomeSections() = viewModelScope.launch {
        val list = mutableListOf<Home>()
        val result = listOf(
            _repository.topArtists(),
            _repository.topAlbums(),
            _repository.recentArtists(),
            _repository.recentAlbums(),
            _repository.favoritePlaylist()
        )
        for (r in result) {
            if (r is Success) {
                list.add(r.data)
            }
        }
        _homeSections.value = list
    }

    private val loadSongs: Deferred<List<Song>>
        get() = viewModelScope.async(IO) {
            when (val result = _repository.allSongs()) {
                is Success -> result.data
                is Error -> arrayListOf()
            }
        }

    private val loadAlbums: Deferred<List<Album>>
        get() = viewModelScope.async(IO) {
            when (val result = _repository.allAlbums()) {
                is Success -> result.data
                is Error -> arrayListOf()
            }
        }
    private val loadArtists: Deferred<List<Artist>>
        get() = viewModelScope.async(IO) {
            when (val result = _repository.allArtists()) {
                is Success -> result.data
                is Error -> arrayListOf()
            }
        }

    private val loadPlaylists: Deferred<List<Playlist>>
        get() = viewModelScope.async(IO) {
            when (val result = _repository.allPlaylists()) {
                is Success -> result.data
                is Error -> arrayListOf()
            }
        }

    private val loadGenres: Deferred<List<Genre>>
        get() = viewModelScope.async(IO) {
            when (val result = _repository.allGenres()) {
                is Success -> result.data
                is Error -> arrayListOf()
            }
        }

    fun forceReload(reloadType: ReloadType) = viewModelScope.launch {
        when (reloadType) {
            Songs -> _songs.value = loadSongs.await()
            Albums -> _albums.value = loadAlbums.await()
            Artists -> _artists.value = loadArtists.await()
            HomeSections -> _songs.value = loadSongs.await()
        }
    }

    override fun onMediaStoreChanged() {
        loadLibraryContent()
    }

    override fun onServiceConnected() {}
    override fun onServiceDisconnected() {}
    override fun onQueueChanged() {}
    override fun onPlayingMetaChanged() {}
    override fun onPlayStateChanged() {}
    override fun onRepeatModeChanged() {}
    override fun onShuffleModeChanged() {}
}

enum class ReloadType {
    Songs,
    Albums,
    Artists,
    HomeSections
}