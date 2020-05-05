package code.name.monkey.retromusic.room.playlist

import code.name.monkey.retromusic.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlaylistDatabaseModel() {
    private val playlistSongDao = PlaylistDatabase.instance().playlistDao();

    suspend fun getPlaylistNames(): List<PlaylistEntity> = playlistSongDao.getPlaylists()

    fun savePlaylist(playlistEntity: PlaylistEntity) = GlobalScope.launch(Dispatchers.IO) {
        playlistSongDao.addPlaylist(playlistEntity)
    }

    fun addSongsToPlaylist(songs: List<Song>, playlistEntity: PlaylistEntity) =
        GlobalScope.launch(Dispatchers.IO) {
            songs.map { Song.toPlaylistSong(it, playlistEntity) }.map {
                val isExist =
                    playlistSongDao.checkPlaylistSongExist(it.playlistId, it.id).isEmpty()
                if (isExist) {
                    playlistSongDao.insertSingle(it)
                }
            }
        }
}