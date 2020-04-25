package code.name.monkey.retromusic.room

import android.content.Context
import code.name.monkey.retromusic.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NowPlayingQueue(context: Context) {

    private val queueDao = MusicPlaybackQueueStoreDatabase.getMusicDatabase(context).queueDao()

    private val musicQueueRepository: MusicQueueRepository = MusicQueueRepository(queueDao)

    fun saveQueue(songs: List<Song>) = GlobalScope.launch(Dispatchers.IO) {
        val songEntity = songs.map {
            Song.toSongEntity(it)
        }
        musicQueueRepository.insertQueue(songEntity)
    }

    fun saveOriginalQueue(playingQueue: List<Song>) = GlobalScope.launch(Dispatchers.IO) {
        val songEntity = playingQueue.map {
            Song.toSongEntity(it)
        }
        musicQueueRepository.insertOriginalQueue(songEntity)
    }

    fun getQueue(): List<Song> {
        val songEntity = musicQueueRepository.getQueue()
        return songEntity.map {
            SongEntity.toSong(it)
        }
    }

    fun getOriginalQueue(): List<Song> {
        val songEntity = musicQueueRepository.getOriginalQueue()
        return songEntity.map {
            SongEntity.toSong(it)
        }
    }
}