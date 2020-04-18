package code.name.monkey.retromusic.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

const val THREAD_COUNT = 3

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
open class AppExecutors constructor(
    val ioContext: CoroutineDispatcher = Dispatchers.IO
)