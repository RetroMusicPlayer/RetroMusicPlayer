package code.name.monkey.retromusic.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<List<T>>.mapItems(crossinline block: suspend (item: T) -> R): Flow<List<R>> {
    return map { items ->
        items.map {
            block.invoke(it)
        }
    }
}