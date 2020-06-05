package code.name.monkey.retromusic.activities.artists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.providers.RepositoryImpl
import code.name.monkey.retromusic.rest.model.LastFmArtist
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ArtistDetailsViewModel(
    application: Application,
    private val artistId: Int
) : AndroidViewModel(application) {

    private val loadArtistDetailsAsync: Deferred<Artist?>
        get() = viewModelScope.async(Dispatchers.IO) {
            _repository.artistById(artistId)
        }
    private val _repository = RepositoryImpl(application.applicationContext)
    private val _artist = MutableLiveData<Artist>()
    private val _lastFmArtist = MutableLiveData<LastFmArtist>()

    fun getArtist(): LiveData<Artist> = _artist
    fun getArtistInfo(): LiveData<LastFmArtist> = _lastFmArtist

    init {
        loadArtistDetails()
    }

    private fun loadArtistDetails() = viewModelScope.launch {
        val artist =
            loadArtistDetailsAsync.await() ?: throw NullPointerException("Album couldn't found")
        _artist.postValue(artist)
    }

    fun loadBiography(name: String, lang: String?, cache: String?) = viewModelScope.launch {
        val info = _repository.artistInfo(name, lang, cache)
        _lastFmArtist.postValue(info)
    }
}