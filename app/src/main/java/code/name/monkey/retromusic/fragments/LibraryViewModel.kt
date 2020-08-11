package code.name.monkey.retromusic.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.adapter.HomeAdapter
import code.name.monkey.retromusic.fragments.ReloadType.*
import code.name.monkey.retromusic.interfaces.MusicServiceEventListener
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: RepositoryImpl
) : ViewModel(), MusicServiceEventListener {

    private val albums = MutableLiveData<List<Album>>()
    private val songs = MutableLiveData<List<Song>>()
    private val artists = MutableLiveData<List<Artist>>()
    private val playlists = MutableLiveData<List<Playlist>>()
    private val genres = MutableLiveData<List<Genre>>()
    private val home = MutableLiveData<List<Home>>()
    private val paletteColor = MutableLiveData<Int>()

    val paletteColorLiveData: LiveData<Int> = paletteColor
    val homeLiveData: LiveData<List<Home>> = home
    val albumsLiveData: LiveData<List<Album>> = albums
    val songsLiveData: LiveData<List<Song>> = songs
    val artistsLiveData: LiveData<List<Artist>> = artists
    val playlisitsLiveData: LiveData<List<Playlist>> = playlists
    val genresLiveData: LiveData<List<Genre>> = genres

    init {
        viewModelScope.launch {
            loadLibraryContent()
        }
    }

    private fun loadLibraryContent() = viewModelScope.launch {
        songs.value = loadSongs.await()
        albums.value = loadAlbums.await()
        artists.value = loadArtists.await()
        playlists.value = loadPlaylists.await()
        genres.value = loadGenres.await()
        loadHomeSections()
    }

    private fun loadHomeSections() = viewModelScope.launch {
        val list = mutableListOf<Home>()
        val result = listOf(
            repository.topArtists(),
            repository.topAlbums(),
            repository.recentArtists(),
            repository.recentAlbums()/*,
            repository.suggestions(),
            repository.favoritePlaylist(),
            repository.homeGenres()*/
        )
        result.forEach {
            if (it != null && it.arrayList.isNotEmpty()) {
                if (it.homeSection == HomeAdapter.SUGGESTIONS) {
                    if (it.arrayList.size > 9) {
                        list.add(it)
                    }
                } else {
                    list.add(it)
                }
            }
        }
        home.value = list
    }

    private val loadSongs: Deferred<List<Song>>
        get() = viewModelScope.async(IO) {
            repository.allSongs()
        }

    private val loadAlbums: Deferred<List<Album>>
        get() = viewModelScope.async(IO) {
            repository.allAlbums()
        }

    private val loadArtists: Deferred<List<Artist>>
        get() = viewModelScope.async(IO) {
            repository.allArtists()
        }

    private val loadPlaylists: Deferred<List<Playlist>>
        get() = viewModelScope.async(IO) {
            repository.allPlaylists()
        }

    private val loadGenres: Deferred<List<Genre>>
        get() = viewModelScope.async(IO) {
            repository.allGenres()
        }

    fun forceReload(reloadType: ReloadType) = viewModelScope.launch {
        when (reloadType) {
            Songs -> songs.value = loadSongs.await()
            Albums -> albums.value = loadAlbums.await()
            Artists -> artists.value = loadArtists.await()
            HomeSections -> songs.value = loadSongs.await()
        }
    }

    fun updateColor(newColor: Int) {
        paletteColor.postValue(newColor)
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