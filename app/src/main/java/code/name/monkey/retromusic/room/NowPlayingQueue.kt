package code.name.monkey.retromusic.room

import android.content.Context
import code.name.monkey.retromusic.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NowPlayingQueue(context: Context) {

    private val queueDao = MusicPlaybackQueueStoreDatabase.getMusicDatabase(context).queueDao()

    private val musicQueueRepository: MusicQueueRepository = MusicQueueRepository(queueDao)

    fun saveQueue(songs: List<Song>) = GlobalScope.launch(Dispatchers.Default) {
        musicQueueRepository.insertQueue(songs)
    }

    fun saveOriginalQueue(songs: List<Song>) = GlobalScope.launch(Dispatchers.Default) {
        musicQueueRepository.insertOriginalQueue(songs)
    }

    fun getQueue(): List<Song> {
        return musicQueueRepository.getQueue()
    }

    fun getOriginalQueue(): List<Song> {
        return musicQueueRepository.getOriginalQueue()
    }
}