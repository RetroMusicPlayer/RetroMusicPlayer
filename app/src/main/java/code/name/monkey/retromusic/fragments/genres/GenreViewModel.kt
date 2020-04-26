package code.name.monkey.retromusic.fragments.genres

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.Result.Success
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.providers.RepositoryImpl
import kotlinx.coroutines.launch

class GenreViewModel(application: Application) : AndroidViewModel(application) {
    var genres = MutableLiveData<List<Genre>>()

    init {
        loadGenre()
    }

    fun loadGenre() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allGenres()
        if (result is Success) {
            genres.value = result.data
        }
    }
}