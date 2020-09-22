package io.github.muntashirakon.music.fragments.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.github.muntashirakon.music.interfaces.MusicServiceEventListener
import io.github.muntashirakon.music.model.Artist
import io.github.muntashirakon.music.network.Result
import io.github.muntashirakon.music.network.model.LastFmArtist
import io.github.muntashirakon.music.repository.RealRepository
import kotlinx.coroutines.Dispatchers.IO

class ArtistDetailsViewModel(
    private val realRepository: RealRepository,
    private val artistId: Long
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