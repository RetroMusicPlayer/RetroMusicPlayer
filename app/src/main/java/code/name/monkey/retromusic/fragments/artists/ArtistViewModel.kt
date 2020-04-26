package code.name.monkey.retromusic.fragments.artists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application) : AndroidViewModel(application) {
    var artists = MutableLiveData<List<Artist>>()

    init {
        loadArtists()
    }

    fun loadArtists() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allArtists()
        if (result is Result.Success) {
            artists.value = result.data
        }
    }
}