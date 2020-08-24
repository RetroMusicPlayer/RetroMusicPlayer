package code.name.monkey.retromusic.util

import android.os.Environment
import java.io.File

object FilePathUtil {
    fun blacklistFilePaths(): List<String> {
        return listOf<File>(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS)
        ).map {
            FileUtil.safeGetCanonicalPath(it)
        }
    }
}