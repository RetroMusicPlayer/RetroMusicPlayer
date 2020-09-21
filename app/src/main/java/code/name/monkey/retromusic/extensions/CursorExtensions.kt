package code.name.monkey.retromusic.extensions

import android.database.Cursor

// exception is rethrown manually in order to have a readable stacktrace

internal fun Cursor.getInt(columnName: String): Int {
    try {
        return this.getInt(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

internal fun Cursor.getLong(columnName: String): Long {
    try {
        return this.getLong(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

internal fun Cursor.getString(columnName: String): String {
    try {
        return this.getString(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

internal fun Cursor.getStringOrNull(columnName: String): String? {
    try {
        return this.getString(this.getColumnIndex(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}