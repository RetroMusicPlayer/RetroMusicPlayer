package code.name.monkey.retromusic.fragments.albums

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.launch

class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    var albums = MutableLiveData<List<Album>>()

    init {
        getAlbums()
    }

    fun getAlbums() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allAlbums()
        if (result is Result.Success) {
            albums.value = result.data
        } else {
            albums.value = listOf()
        }
    }
}