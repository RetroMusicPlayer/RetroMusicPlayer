package code.name.monkey.retromusic.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result
import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var sections: MutableLiveData<List<Home>>
    var repository: RepositoryImpl = RepositoryImpl(getApplication())

    init {
        loadHome()
    }

    fun loadHome() = viewModelScope.launch {
        val list = mutableListOf<Home>()
        val result = listOf(
            repository.topArtists(),
            repository.topAlbums(),
            repository.recentArtists(),
            repository.recentAlbums(),
            repository.favoritePlaylist()
        )
        for (r in result) {
            when (r) {
                is Result.Success -> list.add(r.data)
            }
        }
        sections = MutableLiveData(list)
    }
}