package code.name.monkey.retromusic.fragments.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import code.name.monkey.retromusic.interfaces.MusicServiceEventListener
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.network.Result
import code.name.monkey.retromusic.network.model.LastFmAlbum
import code.name.monkey.retromusic.repository.RealRepository
import kotlinx.coroutines.Dispatchers.IO

class AlbumDetailsViewModel(
    private val repository: RealRepository,
    private val albumId: Long
) : ViewModel(), MusicServiceEventListener {

    fun getAlbum(): LiveData<Album> = liveData(IO) {
        emit(repository.albumByIdAsync(albumId))
    }

    fun getArtist(artistId: Long): LiveData<Artist> = liveData(IO) {
        val artist = repository.artistById(artistId)
        emit(artist)
    }

    fun getAlbumInfo(album: Album): LiveData<Result<LastFmAlbum>> = liveData {
        emit(Result.Loading)
        emit(repository.albumInfo(album.artistName ?: "-", album.title ?: "-"))
    }

    fun getMoreAlbums(artist: Artist): LiveData<List<Album>> = liveData(IO) {
        artist.albums.filter { item -> item.id != albumId }.let { albums ->
            if (albums.isNotEmpty()) emit(albums)
        }
    }

    override fun onMediaStoreChanged() {

    }

    override fun onServiceConnected() {}
    override fun onServiceDisconnected() {}
    override fun onQueueChanged() {}
    override fun onPlayingMetaChanged() {}
    override fun onPlayStateChanged() {}
    override fun onRepeatModeChanged() {}
    override fun onShuffleModeChanged() {}
}