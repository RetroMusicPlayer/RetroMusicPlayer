package code.name.monkey.retromusic.fragments.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result.Success
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.launch

class SongsViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var songs: MutableLiveData<List<Song>>

    init {
        loadSongs()
    }

    fun loadSongs() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allSongs()
        if (result is Success) {
            songs = MutableLiveData(result.data)
        }
    }
}