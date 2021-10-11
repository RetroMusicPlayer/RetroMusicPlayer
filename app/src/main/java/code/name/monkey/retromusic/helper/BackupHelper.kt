package code.name.monkey.retromusic.helper

import android.content.Context
import android.os.Environment
import android.widget.Toast
import code.name.monkey.retromusic.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object BackupHelper {
    suspend fun createBackup(context: Context) {
        withContext(Dispatchers.IO) {
            val finalPath =
                backupRootPath + System.currentTimeMillis().toString() + APPEND_EXTENSION
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
            ZipItem(context.getDatabasePath(it).absolutePath, "$DATABASES_PATH${File.separator}$it")
        }
    }

    private fun getSettingsZipItems(context: Context): List<ZipItem> {
        val sharedPrefPath = context.filesDir.parentFile?.absolutePath + "/shared_prefs/"
        return listOf(
            "${BuildConfig.APPLICATION_ID}_preferences.xml", // App settings pref path
            "$THEME_PREFS_KEY_DEFAULT.xml"  // appthemehelper pref path
        ).map {
            ZipItem(sharedPrefPath + it, "$SETTINGS_PATH${File.separator}$it")
        }
    }

    private fun getUserImageZipItems(context: Context): List<ZipItem>? {
        return context.filesDir.listFiles { _, name ->
            name.endsWith(".jpg")
        }?.map {
            ZipItem(it.absolutePath, "$IMAGES_PATH${File.separator}${it.name}")
        }
    }

    suspend fun restoreBackup(context: Context, file: File) {
        withContext(Dispatchers.IO) {
            ZipInputStream(FileInputStream(file)).use {
                var entry = it.nextEntry
                while (entry != null) {
                    if (entry.isDatabaseEntry()) restoreDatabase(context, it, entry)
                    if (entry.isPreferenceEntry()) restorePreferences(context, it, entry)
                    if (entry.isImageEntry()) restoreImages(context, it, entry)
                    entry = it.nextEntry
                }
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Restore Completed Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun restoreImages(context: Context, zipIn: ZipInputStream, zipEntry: ZipEntry) {
        val filePath =
            context.filesDir.path + File.separator + zipEntry.getFileName()
        BufferedOutputStream(FileOutputStream(filePath)).use { bos ->
            val bytesIn = ByteArray(DEFAULT_BUFFER_SIZE)
            var read: Int
            while (zipIn.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
        }
    }

    private fun restorePreferences(context: Context, zipIn: ZipInputStream, zipEntry: ZipEntry) {
        val file = File(
            context.filesDir.parent!! + File.separator + "shared_prefs" + File.separator + zipEntry.getFileName()
        )
        if (file.exists()) {
            file.delete()
        }
        BufferedOutputStream(FileOutputStream(file)).use { bos ->
            val bytesIn = ByteArray(DEFAULT_BUFFER_SIZE)
            var read: Int
            while (zipIn.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
        }
    }

    private fun restoreDatabase(context: Context, zipIn: ZipInputStream, zipEntry: ZipEntry) {
        val filePath =
            context.filesDir.parent!! + File.separator + DATABASES_PATH + File.separator + zipEntry.getFileName()
        BufferedOutputStream(FileOutputStream(filePath)).use { bos ->
            val bytesIn = ByteArray(DEFAULT_BUFFER_SIZE)
            var read: Int
            while (zipIn.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
        }
    }

    val backupRootPath =
        Environment.getExternalStorageDirectory().toString() + "/RetroMusic/Backups/"
    const val BACKUP_EXTENSION = "rmbak"
    private const val APPEND_EXTENSION = ".$BACKUP_EXTENSION"
    private const val DATABASES_PATH = "databases"
    private const val SETTINGS_PATH = "prefs"
    private const val IMAGES_PATH = "userImages"
    private const val THEME_PREFS_KEY_DEFAULT = "[[kabouzeid_app-theme-helper]]"

    private fun ZipEntry.isDatabaseEntry(): Boolean {
        return name.startsWith(DATABASES_PATH)
    }

    private fun ZipEntry.isPreferenceEntry(): Boolean {
        return name.startsWith(SETTINGS_PATH)
    }

    private fun ZipEntry.isImageEntry(): Boolean {
        return name.startsWith(IMAGES_PATH)
    }

    private fun ZipEntry.getFileName(): String {
        return name.substring(name.lastIndexOf(File.separator))
    }
}

data class ZipItem(val filePath: String, val zipPath: String)