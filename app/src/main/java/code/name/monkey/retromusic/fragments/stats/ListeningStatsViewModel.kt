package code.name.monkey.retromusic.fragments.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import code.name.monkey.retromusic.db.ListeningEventDao
import code.name.monkey.retromusic.db.ListeningEventEntity
import code.name.monkey.retromusic.db.ListeningStats
import java.util.concurrent.TimeUnit

/**
 * Room runs the LiveData queries off the main thread, so nothing here touches IO directly.
 */
class ListeningStatsViewModel(dao: ListeningEventDao) : ViewModel() {

    val allTime: LiveData<ListeningStats> = dao.stats(0L)

    val lastSevenDays: LiveData<ListeningStats> =
        dao.stats(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))

    val recent: LiveData<List<ListeningEventEntity>> = dao.recentEvents(RECENT_LIMIT)

    companion object {
        private const val RECENT_LIMIT = 200
    }
}
