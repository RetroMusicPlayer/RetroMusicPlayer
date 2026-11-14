package code.name.monkey.retromusic.helper

import android.content.Context
import android.os.Environment
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.HistoryEntity
import code.name.monkey.retromusic.db.ListeningEventEntity
import code.name.monkey.retromusic.db.PlayCountEntity
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.RetroDatabase
import code.name.monkey.retromusic.db.toHistoryEntity
import code.name.monkey.retromusic.db.toPlayCount
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.extensions.zipOutputStream
import code.name.monkey.retromusic.helper.BackupContent.*
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.repository.Repository
import code.name.monkey.retromusic.repository.SongRepository
import code.name.monkey.retromusic.util.getExternalStoragePublicDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object BackupHelper : KoinComponent {
    private val repository by inject<Repository>()
    private val songRepository by inject<SongRepository>()
    private val database by inject<RetroDatabase>()

    suspend fun createBackup(context: Context, name: String) {
        val backupFile =
            File(getBackupRoot(), name + APPEND_EXTENSION)
        if (backupFile.parentFile?.exists() != true) {
            backupFile.parentFile?.mkdirs()
        }
        val zipItems = mutableListOf<ZipItem>()
        zipItems.addAll(getPlaylistZipItems(context))
        zipItems.addAll(getSettingsZipItems(context))
        getUserImageZipItems(context)?.let { zipItems.addAll(it) }
        zipItems.addAll(getCustomArtistZipItems(context))
        zipItems.addAll(getListeningDataZipItems(context))
        zipAll(context, zipItems, backupFile)
        // Clean Cache Playlist Directory
        File(context.filesDir, PLAYLISTS_PATH).deleteRecursively()
        File(context.filesDir, LISTENING_PATH).deleteRecursively()
    }

    private suspend fun zipAll(context: Context, zipItems: List<ZipItem>, backupFile: File) =
        withContext(Dispatchers.IO) {
            runCatching {
                backupFile.outputStream().buffered().zipOutputStream().use { out ->
                    for (zipItem in zipItems) {
                        File(zipItem.filePath).inputStream().buffered().use { origin ->
                            val entry = ZipEntry(zipItem.zipPath)
                            out.putNextEntry(entry)
                            origin.copyTo(out)
                        }
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    context.showToast(R.string.error_create_backup)
                }
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    context.showToast(R.string.message_backup_create_success)
                }
            }
        }

    private suspend fun getListeningDataZipItems(context: Context): List<ZipItem> =
        withContext(Dispatchers.IO) {
            runCatching {
                val folder = File(context.filesDir, LISTENING_PATH)
                if (!folder.exists()) folder.mkdirs()
                val root = JSONObject()
                root.put("version", 2)
                root.put("history", JSONArray().also { arr ->
                    database.historyDao().allHistory().forEach { arr.put(it.toJson()) }
                })
                root.put("playCounts", JSONArray().also { arr ->
                    database.playCountDao().playCountSongs().forEach { arr.put(it.toJson()) }
                })
                // Added in version 2; older backups simply don't have this key.
                root.put("events", JSONArray().also { arr ->
                    database.listeningEventDao().allEvents().forEach { arr.put(it.toJson()) }
                })
                val file = File(folder, LISTENING_FILE)
                file.writeText(root.toString(), Charsets.UTF_8)
                listOf(ZipItem(file.absolutePath, LISTENING_PATH.child(LISTENING_FILE)))
            }.getOrDefault(emptyList())
        }

    // Songs are matched by title/artist/album/duration, not by MediaStore id or file path,
    // because both change when the library moves to another device.
    private suspend fun restoreListeningData(zipIn: ZipInputStream) {
        val text = zipIn.readBytes().toString(Charsets.UTF_8)
        val root = JSONObject(text)
        val songs = songRepository.songs()
        val byFullKey = songs.groupBy { songKey(it.title, it.artistName, it.albumName) }
        val byShortKey = songs.groupBy { songKey(it.title, it.artistName, null) }

        fun findSong(o: JSONObject): Song? {
            val title = o.optString("title")
            val artist = o.optString("artist_name")
            val album = o.optString("album_name")
            val duration = o.optLong("duration")
            val candidates = byFullKey[songKey(title, artist, album)]
                ?: byShortKey[songKey(title, artist, null)]
                ?: return null
            return candidates.firstOrNull { kotlin.math.abs(it.duration - duration) <= 2000 }
                ?: candidates.first()
        }

        val historyDao = database.historyDao()
        val playCountDao = database.playCountDao()
        root.optJSONArray("history")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val song = findSong(o) ?: continue
                historyDao.upsertSongInHistory(song.toHistoryEntity(o.optLong("time_played")))
            }
        }
        root.optJSONArray("playCounts")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val song = findSong(o) ?: continue
                val existing = playCountDao.findSongExistInPlayCount(song.id)
                val count = maxOf(o.optInt("play_count"), existing?.playCount ?: 0)
                val timePlayed = maxOf(o.optLong("time_played"), existing?.timePlayed ?: 0L)
                playCountDao.upsertSongInPlayCount(
                    song.toPlayCount().let {
                        PlayCountEntity(
                            id = it.id, title = it.title, trackNumber = it.trackNumber,
                            year = it.year, duration = it.duration, data = it.data,
                            dateModified = it.dateModified, albumId = it.albumId,
                            albumName = it.albumName, artistId = it.artistId,
                            artistName = it.artistName, composer = it.composer,
                            albumArtist = it.albumArtist, timePlayed = timePlayed,
                            playCount = count
                        )
                    }
                )
            }
        }
        // Events keep their own title/artist/album, so they are restored even when the song
        // is no longer in the library; songId is only filled in when a match is found.
        val eventDao = database.listeningEventDao()
        root.optJSONArray("events")?.let { arr ->
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val title = o.optString("title")
                val artist = o.optString("artist_name")
                val playedAt = o.optLong("played_at")
                if (eventDao.countSameEvent(playedAt, title, artist) > 0) continue
                eventDao.insert(
                    ListeningEventEntity(
                        songId = findSong(o)?.id ?: -1L,
                        title = title,
                        artistName = artist,
                        albumName = o.optString("album_name"),
                        duration = o.optLong("duration"),
                        playedAt = playedAt,
                        listenedMs = o.optLong("listened_ms")
                    )
                )
            }
        }
    }

    private fun songKey(title: String, artist: String, album: String?): String {
        fun n(s: String) = s.trim().lowercase(Locale.ROOT)
        return if (album == null) "${n(title)}|${n(artist)}" else "${n(title)}|${n(artist)}|${n(album)}"
    }

    private fun HistoryEntity.toJson() = JSONObject().apply {
        put("title", title); put("artist_name", artistName); put("album_name", albumName)
        put("duration", duration); put("track_number", trackNumber); put("year", year)
        put("composer", composer ?: JSONObject.NULL); put("album_artist", albumArtist ?: JSONObject.NULL)
        put("time_played", timePlayed)
    }

    private fun ListeningEventEntity.toJson() = JSONObject().apply {
        put("title", title); put("artist_name", artistName); put("album_name", albumName)
        put("duration", duration); put("played_at", playedAt); put("listened_ms", listenedMs)
    }

    private fun PlayCountEntity.toJson() = JSONObject().apply {
        put("title", title); put("artist_name", artistName); put("album_name", albumName)
        put("duration", duration); put("track_number", trackNumber); put("year", year)
        put("composer", composer ?: JSONObject.NULL); put("album_artist", albumArtist ?: JSONObject.NULL)
        put("time_played", timePlayed); put("play_count", playCount)
    }

    private suspend fun getPlaylistZipItems(context: Context): List<ZipItem> {
        val playlistZipItems = mutableListOf<ZipItem>()
        // Cache Playlist files in App storage
        val playlistFolder = File(context.filesDir, PLAYLISTS_PATH)
        if (!playlistFolder.exists()) {
            playlistFolder.mkdirs()
        }
        for (playlist in repository.fetchPlaylistWithSongs()) {
            runCatching {
                M3UWriter.writeIO(playlistFolder, playlist)
            }.onSuccess { playlistFile ->
                if (playlistFile.exists()) {
                    playlistZipItems.add(
                        ZipItem(
                            playlistFile.absolutePath,
                            PLAYLISTS_PATH.child(playlistFile.name)
                        )
                    )
                }
            }
        }
        return playlistZipItems
    }

    private fun getSettingsZipItems(context: Context): List<ZipItem> {
        val sharedPrefPath = File(context.filesDir.parentFile, "shared_prefs")
        return listOf(
            "${BuildConfig.APPLICATION_ID}_preferences.xml", // App settings pref path
            "$THEME_PREFS_KEY_DEFAULT.xml"  // appthemehelper pref path
        ).map {
            ZipItem(File(sharedPrefPath, it).absolutePath, SETTINGS_PATH.child(it))
        }
    }

    private fun getUserImageZipItems(context: Context): List<ZipItem>? {
        return context.filesDir.listFiles { _, name ->
            name.endsWith(".jpg")
        }?.map {
            ZipItem(it.absolutePath, IMAGES_PATH.child(it.name))
        }
    }

    private fun getCustomArtistZipItems(context: Context): List<ZipItem> {
        val zipItemList = mutableListOf<ZipItem>()
        val sharedPrefPath = File(context.filesDir.parentFile, "shared_prefs")

        zipItemList.addAll(
            File(context.filesDir, "custom_artist_images")
                .listFiles()?.map {
                    ZipItem(
                        it.absolutePath,
                        CUSTOM_ARTISTS_PATH.child("custom_artist_images").child(it.name)
                    )
                }?.toList() ?: listOf()
        )
        File(sharedPrefPath, "custom_artist_image.xml").let {
            if (it.exists()) {
                zipItemList.add(
                    ZipItem(
                        it.absolutePath,
                        CUSTOM_ARTISTS_PATH.child("prefs").child("custom_artist_image.xml")
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
                    if (entry.isPlaylistEntry() && contents.contains(PLAYLISTS)) {
                        restorePlaylists(it, entry)
                    } else if (entry.isPreferenceEntry() && contents.contains(SETTINGS)) {
                        restorePreferences(context, it, entry)
                    } else if (entry.isImageEntry() && contents.contains(USER_IMAGES)) {
                        restoreImages(context, it, entry)
                    } else if (entry.isListeningEntry() && contents.contains(LISTENING_DATA)) {
                        restoreListeningData(it)
                    } else if (entry.isCustomArtistEntry() && contents.contains(CUSTOM_ARTIST_IMAGES)) {
                        if (entry.isCustomArtistPrefEntry()) {
                            restoreCustomArtistPrefs(context, it, entry)
                        } else if (entry.isCustomArtistImageEntry()) {
                            restoreCustomArtistImages(context, it, entry)
                        }
                    }
                    entry = it.nextEntry
                }
            }
            withContext(Dispatchers.Main) {
                context.showToast(R.string.message_restore_success)
            }
        }
    }

    private fun restoreImages(context: Context, zipIn: ZipInputStream, zipEntry: ZipEntry) {
        val file = File(
            context.filesDir.path, zipEntry.getFileName()
        )
        file.outputStream().buffered().use { bos ->
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
        file.outputStream().buffered().use { bos ->
            zipIn.copyTo(bos)
        }
    }

    private suspend fun restorePlaylists(
        zipIn: ZipInputStream,
        zipEntry: ZipEntry
    ) {
        val playlistName = zipEntry.getFileName().substringBeforeLast(".")
        val songs = mutableListOf<Song>()

        // Get songs from m3u playlist files
        zipIn.bufferedReader().lineSequence().forEach { line ->
            if (line.startsWith(File.separator)) {
                if (File(line).exists()) {
                    songs.addAll(songRepository.songsByFilePath(line))
                }
            }
        }
        val playlistEntity = repository.checkPlaylistExists(playlistName).firstOrNull()
        if (playlistEntity != null) {
            val songEntities = songs.map {
                it.toSongEntity(playlistEntity.playListId)
            }
            repository.insertSongs(songEntities)
        } else {
            val playListId = repository.createPlaylist(PlaylistEntity(playlistName = playlistName))
            val songEntities = songs.map {
                it.toSongEntity(playListId)
            }
            repository.insertSongs(songEntities)
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
        val file = File(
            parentFolder,
            zipEntry.getFileName()
        )
        file.outputStream().buffered()
            .use { bos ->
                zipIn.copyTo(bos)
            }
    }

    private fun restoreCustomArtistPrefs(
        context: Context,
        zipIn: ZipInputStream,
        zipEntry: ZipEntry
    ) {
        val file =
            File(context.filesDir.parentFile, "shared_prefs".child(zipEntry.getFileName()))
        file.outputStream().buffered().use { bos ->
            zipIn.copyTo(bos)
        }
    }

    fun getBackupRoot(): File {
        return File(
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "RetroMusic/Backups"
        )
    }

    const val BACKUP_EXTENSION = "rmbak"
    const val APPEND_EXTENSION = ".$BACKUP_EXTENSION"
    private const val PLAYLISTS_PATH = "Playlists"
    private const val SETTINGS_PATH = "prefs"
    private const val IMAGES_PATH = "userImages"
    private const val LISTENING_PATH = "listening"
    private const val LISTENING_FILE = "listening.json"
    private const val CUSTOM_ARTISTS_PATH = "artistImages"
    private const val THEME_PREFS_KEY_DEFAULT = "[[kabouzeid_app-theme-helper]]"

    private fun ZipEntry.isPlaylistEntry(): Boolean {
        return name.startsWith(PLAYLISTS_PATH)
    }

    private fun ZipEntry.isPreferenceEntry(): Boolean {
        return name.startsWith(SETTINGS_PATH)
    }

    private fun ZipEntry.isListeningEntry(): Boolean {
        return name.startsWith(LISTENING_PATH)
    }

    private fun ZipEntry.isImageEntry(): Boolean {
        return name.startsWith(IMAGES_PATH)
    }

    private fun ZipEntry.isCustomArtistEntry(): Boolean {
        return name.startsWith(CUSTOM_ARTISTS_PATH)
    }

    private fun ZipEntry.isCustomArtistImageEntry(): Boolean {
        return name.startsWith(CUSTOM_ARTISTS_PATH) && name.contains("custom_artist_images")
    }

    private fun ZipEntry.isCustomArtistPrefEntry(): Boolean {
        return name.startsWith(CUSTOM_ARTISTS_PATH) && name.contains("prefs")
    }

    private fun ZipEntry.getFileName(): String {
        return name.substring(name.lastIndexOf(File.separator) + 1)
    }

    fun getTimeStamp(): String {
        return SimpleDateFormat("dd-MMM yyyy HHmmss", Locale.getDefault()).format(Date())
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

fun String.child(child: String): String {
    return this + File.separator + child
}

enum class BackupContent {
    SETTINGS,
    USER_IMAGES,
    CUSTOM_ARTIST_IMAGES,
    PLAYLISTS,
    LISTENING_DATA
}