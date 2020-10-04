package code.name.monkey.retromusic.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.db.SongEntity
import code.name.monkey.retromusic.interfaces.IMusicServiceEventListener
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.repository.RealRepository

class PlaylistDetailsViewModel(
    private val realRepository: RealRepository,
    private var playlist: PlaylistWithSongs
) : ViewModel(), IMusicServiceEventListener {

    private val playListSongs = MutableLiveData<List<Song>>()

    fun getSongs(): LiveData<List<SongEntity>> =
        realRepository.playlistSongs(playlist.playlistEntity.playListId)


    override fun onMediaStoreChanged() {
        /*if (playlist !is AbsCustomPlaylist) {
            // Playlist deleted
            if (!PlaylistsUtil.doesPlaylistExist(App.getContext(), playlist.id)) {
                //TODO Finish the page
                return
            }
            // Playlist renamed
            val playlistName =
                PlaylistsUtil.getNameForPlaylist(App.getContext(), playlist.id.toLong())
            if (playlistName != playlist.name) {
                viewModelScope.launch {
                    playlist = realRepository.playlist(playlist.id)
                    _playlist.postValue(playlist)
                }
            }
        }
        loadPlaylistSongs(playlist)*/
    }

    override fun onServiceConnected() {}
    override fun onServiceDisconnected() {}
    override fun onQueueChanged() {}
    override fun onPlayingMetaChanged() {}
    override fun onPlayStateChanged() {}
    override fun onRepeatModeChanged() {}
    override fun onShuffleModeChanged() {}
}
