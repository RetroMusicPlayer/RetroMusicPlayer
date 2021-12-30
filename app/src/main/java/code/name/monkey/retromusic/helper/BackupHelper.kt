package code.name.monkey.retromusic.helper

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.helper.BackupContent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object BackupHelper {
    suspend fun createBackup(context: Context, name: String) {
        val backupFile =
            File(backupRootPath + File.separator + name + APPEND_EXTENSION)
        if (backupFile.parentFile?.exists() != true) {
            backupFile.parentFile?.mkdirs()
        }
        val zipItems = mutableListOf<ZipItem>()
        zipItems.addAll(getDatabaseZipItems(context))
        zipItems.addAll(getSettingsZipItems(context))
        getUserImageZipItems(context)?.let { zipItems.addAll(it) }
        zipItems.addAll(getCustomArtistZipItems(context))
        zipItems.addAll(getQueueZipItems(context))
        zipAll(zipItems, backupFile)
    }

    private suspend fun zipAll(zipItems: List<ZipItem>, backupFile: File) =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                ZipOutputStream(BufferedOutputStream(FileOutputStream(backupFile))).use { out ->
                    for (zipItem in zipItems) {
                        FileInputStream(zipItem.filePath).use { fi ->
                            BufferedInputStream(fi).use { origin ->
                                val entry = ZipEntry(zipItem.zipPath)
                                out.putNextEntry(entry)
                                origin.copyTo(out)
                            }
                        }
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    Toast.makeText(App.getContext(), "Couldn't create backup", Toast.LENGTH_SHORT)
                        .show()
                }
                throw Exception(it)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        App.getContext(),
                        "Backup created successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        }

    private fun getDatabaseZipItems(context: Context): List<ZipItem> {
        return context.databaseList().filter {
            it.endsWith(".db") && it != queueDatabase
        }.map {
            ZipItem(context.getDatabasePath(it).absolutePath, "$DATABASES_PATH${File.separator}$it")
        }
    }

    private fun getQueueZipItems(context: Context): List<ZipItem> {
        Log.d("RetroMusic", context.getDatabasePath(queueDatabase).absolutePath)
        return listOf(
            ZipItem(
                context.getDatabasePath(queueDatabase).absolutePath,
                "$QUEUE_PATH${File.separator}$queueDatabase"
            )
        )
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

    private fun getCustomArtistZipItems(context: Context): List<ZipItem> {
        val zipItemList = mutableListOf<ZipItem>()
        val sharedPrefPath = context.filesDir.parentFile?.absolutePath + "/shared_prefs/"

        zipItemList.addAll(
            File(context.filesDir, "custom_artist_images")
                .listFiles()?.map {
                    ZipItem(
                        it.absolutePath,
                        "$CUSTOM_ARTISTS_PATH${File.separator}custom_artist_images${File.separator}${it.name}"
                    )
                }?.toList() ?: listOf()
        )
        File(sharedPrefPath + File.separator + "custom_artist_image.xml").let {
            if (it.exists()) {
                zipItemList.add(
                    ZipItem(
                        it.absolutePath,
                        "$CUSTOM_ARTISTS_PATH${File.separator}prefs${File.separator}custom_artist_image.xml"
                    )
                )
            }
        }


        return zipItemList
    }

    suspend fun restoreBackup(
        context: Context,
        inputStream: InputStream?,
        contents: List<BackupContent>
    ) {
        withContext(Dispatchers.IO) {
            ZipInputStream(inputStream).use {
                var entry = it.nextEntry
                while (entry != null) {
                    if (entry.isDatabaseEntry() && contents.contains(PLAYLISTS)) {
                        restoreDatabase(context, it, entry)
                    } else if (entry.isPreferenceEntry() && contents.contains(SETTINGS)) {
                        restorePreferences(context, it, entry)
                    } else if (entry.isImageEntry() && contents.contains(USER_IMAGES)) {
                        restoreImages(context, it, entry)
                    } else if (entry.isCustomArtistImageEntry() && contents.contains(
                            CUSTOM_ARTIST_IMAGES
                        )
                    ) {
                        restoreCustomArtistImages(context, it, entry)
                        restoreCustomArtistPrefs(context, it, entry)
                    } else if (entry.isQueueEntry() && contents.contains(QUEUE)) {
                        restoreQueueDatabase(context, it, entry)
                    }

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
            zipIn.copyTo(bos)
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
            zipIn.copyTo(bos)
        }
    }

    private fun restoreDatabase(context: Context, zipIn: ZipInputStream, zipEntry: ZipEntry) {
        val filePath =
            context.filesDir.parent!! + File.separator + DATABASES_PATH + File.separator + zipEntry.getFileName()
        BufferedOutputStream(FileOutputStream(filePath)).use { bos ->
            zipIn.copyTo(bos)
        }
    }

    private fun restoreQueueDatabase(context: Context, zipIn: ZipInputStream, zipEntry: ZipEntry) {
        PreferenceManager.getDefaultSharedPreferences(context).edit(commit = true) {
            putInt("POSITION", 0)
        }
        val filePath =
            context.filesDir.parent!! + File.separator + DATABASES_PATH + File.separator + zipEntry.getFileName()
        BufferedOutputStream(FileOutputStream(filePath)).use { bos ->
            zipIn.copyTo(bos)
        }
    }

    private fun restoreCustomArtistImages(
        context: Context,
        zipIn: ZipInputStream,
        zipEntry: ZipEntry
    ) {
        val parentFolder = File(context.filesDir, "custom_artist_images")

        if (!parentFolder.exists()) {
            parentFolder.mkdirs()
        }
        BufferedOutputStream(
            FileOutputStream(
                File(
                    parentFolder,
                    zipEntry.getFileName()
                )
            )
        ).use { bos ->
            zipIn.copyTo(bos)
        }
    }

    private fun restoreCustomArtistPrefs(
        context: Context,
        zipIn: ZipInputStream,
        zipEntry: ZipEntry
    ) {
        val filePath =
            context.filesDir.parentFile?.absolutePath + "/shared_prefs/" + zipEntry.getFileName()
        BufferedOutputStream(FileOutputStream(filePath)).use { bos ->
            zipIn.copyTo(bos)
        }
    }

    val backupRootPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            .toString() + "/RetroMusic/Backups/"
    const val BACKUP_EXTENSION = "rmbak"
    const val APPEND_EXTENSION = ".$BACKUP_EXTENSION"
    private const val DATABASES_PATH = "databases"
    private const val QUEUE_PATH = "queue"
    private const val SETTINGS_PATH = "prefs"
    private const val IMAGES_PATH = "userImages"
    private const val CUSTOM_ARTISTS_PATH = "artistImages"
    private const val THEME_PREFS_KEY_DEFAULT = "[[kabouzeid_app-theme-helper]]"
    private const val queueDatabase = "music_playback_state.db"

    private fun ZipEntry.isDatabaseEntry(): Boolean {
        return name.startsWith(DATABASES_PATH)
    }

    private fun ZipEntry.isPreferenceEntry(): Boolean {
        return name.startsWith(SETTINGS_PATH)
    }

    private fun ZipEntry.isImageEntry(): Boolean {
        return name.startsWith(IMAGES_PATH)
    }

    private fun ZipEntry.isCustomArtistImageEntry(): Boolean {
        return name.startsWith(CUSTOM_ARTISTS_PATH) && name.contains("custom_artist_images")
    }

    private fun ZipEntry.isCustomArtistPrefEntry(): Boolean {
        return name.startsWith(CUSTOM_ARTISTS_PATH) && name.contains("prefs")
    }

    private fun ZipEntry.isQueueEntry(): Boolean {
        return name.startsWith(QUEUE_PATH)
    }

    private fun ZipEntry.getFileName(): String {
        return name.substring(name.lastIndexOf(File.separator))
    }
}

data class ZipItem(val filePath: String, val zipPath: String)

fun CharSequence.sanitize(): String {
    return toString().replace("/", "_")
        .replace(":", "_")
        .replace("*", "_")
        .replace("?", "_")
        .replace("\"", "_")
        .replace("<", "_")
        .replace(">", "_")
        .replace("|", "_")
        .replace("\\", "_")
        .replace("&", "_")
}

enum class BackupContent {
    SETTINGS,
    USER_IMAGES,
    CUSTOM_ARTIST_IMAGES,
    PLAYLISTS,
    QUEUE
}