package code.name.monkey.retromusic.fragments.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import code.name.monkey.retromusic.interfaces.MusicServiceEventListener
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.network.Result
import code.name.monkey.retromusic.network.model.LastFmArtist
import code.name.monkey.retromusic.repository.RealRepository
import kotlinx.coroutines.Dispatchers.IO

class ArtistDetailsViewModel(
    private val realRepository: RealRepository,
    private val artistId: Int
) : ViewModel(), MusicServiceEventListener {

    fun getArtist(): LiveData<Artist> = liveData(IO) {
        val artist = realRepository.artistById(artistId)
        emit(artist)
    }

    fun getArtistInfo(
        name: String,
        lang: String?,
        cache: String?
    ): LiveData<Result<LastFmArtist>> = liveData(IO) {
        emit(Result.Loading)
        val info = realRepository.artistInfo(name, lang, cache)
        emit(info)
    }

    override fun onMediaStoreChanged() {
        getArtist()
    }

    override fun onServiceConnected() {}
    override fun onServiceDisconnected() {}
    override fun onQueueChanged() {}
    override fun onPlayingMetaChanged() {}
    override fun onPlayStateChanged() {}
    override fun onRepeatModeChanged() {}
    override fun onShuffleModeChanged() {}
}