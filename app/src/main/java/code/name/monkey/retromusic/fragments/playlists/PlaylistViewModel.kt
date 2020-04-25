package code.name.monkey.retromusic.fragments.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var playlists: MutableLiveData<List<Playlist>>

    init {
        loadPlaylist()
    }

    fun loadPlaylist() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allPlaylists()
        if (result is Result.Success) {
            playlists = MutableLiveData(result.data)
        }
    }
}