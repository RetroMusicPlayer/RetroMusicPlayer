package code.name.monkey.retromusic.helper

import android.content.Context
import android.os.Environment
import code.name.monkey.retromusic.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object BackupHelper {
    suspend fun createBackup(context: Context) {
        withContext(Dispatchers.IO) {
            val finalPath = backupRootPath + System.currentTimeMillis().toString() + backupExt
            val zipItems = mutableListOf<ZipItem>()
            zipItems.addAll(getDatabaseZipItems(context))
            zipItems.addAll(getSettingsZipItems(context))
            getUserImageZipItems(context)?.let { zipItems.addAll(it) }
            zipAll(zipItems, finalPath)
        }
    }

    private fun zipAll(zipItems: List<ZipItem>, finalPath: String) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(finalPath))).use { out ->
            for (zipItem in zipItems) {
                FileInputStream(zipItem.filePath).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val entry = ZipEntry(zipItem.zipPath)
                        out.putNextEntry(entry)
                        origin.copyTo(out, 1024)
                    }
                }
            }
        }
    }

    private fun getDatabaseZipItems(context: Context): List<ZipItem> {
        return context.databaseList().filter {
            it.endsWith(".db")
        }.map {
            ZipItem(context.getDatabasePath(it).absolutePath, "databases${File.separator}$it")
        }
    }

    private fun getSettingsZipItems(context: Context): List<ZipItem> {
        val sharedPrefPath = context.filesDir.parentFile?.absolutePath + "/shared_prefs/"
        return listOf(
            "${BuildConfig.APPLICATION_ID}_preferences.xml", // App settings pref path
            "$THEME_PREFS_KEY_DEFAULT.xml"  // appthemehelper pref path
        ).map {
            ZipItem(sharedPrefPath + it, "preferences${File.separator}$it")
        }
    }

    private fun getUserImageZipItems(context: Context): List<ZipItem>? {
        return context.filesDir.listFiles { _, name ->
            name.endsWith(".jpg")
        }?.map {
            ZipItem(it.absolutePath, "userImages${File.separator}${it.name}")
        }
    }

    val backupRootPath =
        Environment.getExternalStorageDirectory().toString() + "/RetroMusic/Backups/"
    const val backupExt = ".rmbak"
    private const val THEME_PREFS_KEY_DEFAULT = "[[kabouzeid_app-theme-helper]]"

}

data class ZipItem(val filePath: String, val zipPath: String)