package code.name.monkey.retromusic.room.playlist

class PlaylistRepository(private val playlistSongDao: PlaylistSongDao) {
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity) {
        playlistSongDao.addPlaylist(playlistEntity)
    }

    suspend fun insertPlaylistSongs(songs: List<PlaylistSongEntity>) {
        playlistSongDao.addPlaylistSongs(songs)
    }
}