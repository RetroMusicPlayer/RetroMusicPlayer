package code.name.monkey.retromusic.room

class MusicQueueRepository(private val queueDao: QueueDao) {

    fun getQueue(): List<SongEntity> = queueDao.getQueue()

    fun getOriginalQueue(): List<SongEntity> = queueDao.getQueue()

    suspend fun insertQueue(queue: List<SongEntity>) {
        queueDao.saveQueue(queue)
    }

    suspend fun insertOriginalQueue(queue: List<SongEntity>) {
        queueDao.saveQueue(queue)
    }
}