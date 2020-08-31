package code.name.monkey.retromusic.fragments.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.repository.SongRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SongsViewModel(
    private val songRepository: SongRepository
) : ViewModel() {
    init {
        update()
    }

    private val songsData = MutableLiveData<List<Song>>().apply { value = mutableListOf() }

    fun getSongList(): LiveData<List<Song>> {
        return songsData
    }

    fun update() {
        viewModelScope.launch(IO) {
            val songs = songRepository.songs()
            songsData.postValue(songs)
        }
    }
}