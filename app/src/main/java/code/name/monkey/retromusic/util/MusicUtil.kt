package code.name.monkey.retromusic.util

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.content.contentValuesOf
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.Constants
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.SongEntity
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.extensions.getLong
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.helper.MusicPlayerRemote.removeFromQueue
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.model.lyrics.AbsSynchronizedLyrics
import code.name.monkey.retromusic.repository.Repository
import code.name.monkey.retromusic.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object MusicUtil : KoinComponent {
    fun createShareSongFileIntent(context: Context, song: Song): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_STREAM, try {
                    FileProvider.getUriForFile(
                        context,
                        context.applicationContext.packageName,
                        File(song.data)
                    )
                } catch (e: IllegalArgumentException) {
                    getSongFileUri(song.id)
                }
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "audio/*"
        }
    }

    fun createShareMultipleSongIntent(context: Context, songs: List<Song>): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            type = "audio/*"

            val files = ArrayList<Uri>()

            for (song in songs) {
                files.add(
                    try {
                        FileProvider.getUriForFile(
                            context,
                            context.applicationContext.packageName,
                            File(song.data)
                        )
                    } catch (e: IllegalArgumentException) {
                        getSongFileUri(song.id)
                    }
                )
            }
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
        }
    }

    fun buildInfoString(string1: String?, string2: String?): String {
        if (string1.isNullOrEmpty()) {
            return if (string2.isNullOrEmpty()) "" else string2
        }
        return if (string2.isNullOrEmpty()) if (string1.isNullOrEmpty()) "" else string1 else "$string1  •  $string2"
    }

    fun createAlbumArtFile(context: Context): File {
        return File(
            createAlbumArtDir(context),
            System.currentTimeMillis().toString()
        )
    }

    private fun createAlbumArtDir(context: Context): File {
        val albumArtDir = File(
            if (VersionUtils.hasR()) context.cacheDir else getExternalStorageDirectory(),
            "/albumthumbs/"
        )
        if (!albumArtDir.exists()) {
            albumArtDir.mkdirs()
            try {
                File(albumArtDir, ".nomedia").createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return albumArtDir
    }

    fun deleteAlbumArt(context: Context, albumId: Long) {
        val contentResolver = context.contentResolver
        val localUri = "content://media/external/audio/albumart".toUri()
        contentResolver.delete(ContentUris.withAppendedId(localUri, albumId), null, null)
        contentResolver.notifyChange(localUri, null)
    }

    fun getArtistInfoString(
        context: Context,
        artist: Artist,
    ): String {
        val albumCount = artist.albumCount
        val songCount = artist.songCount
        val albumString =
            if (albumCount == 1) context.resources.getString(R.string.album)
            else context.resources.getString(R.string.albums)
        val songString =
            if (songCount == 1) context.resources.getString(R.string.song)
            else context.resources.getString(R.string.songs)
        return "$albumCount $albumString • $songCount $songString"
    }

    //iTunes uses for example 1002 for track 2 CD1 or 3011 for track 11 CD3.
    //this method converts those values to normal tracknumbers
    fun getFixedTrackNumber(trackNumberToFix: Int): Int {
        return trackNumberToFix % 1000
    }

    fun getLyrics(song: Song): String? {
        var lyrics: String? = "No lyrics found"
        val file = File(song.data)
        try {
            lyrics = AudioFileIO.read(file).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (lyrics == null || lyrics.trim { it <= ' ' }.isEmpty() || AbsSynchronizedLyrics
                .isSynchronized(lyrics)
        ) {
            val dir = file.absoluteFile.parentFile
            if (dir != null && dir.exists() && dir.isDirectory) {
                val format = ".*%s.*\\.(lrc|txt)"
                val filename = Pattern.quote(
                    FileUtil.stripExtension(file.name)
                )
                val songtitle = Pattern.quote(song.title)
                val patterns =
                    ArrayList<Pattern>()
                patterns.add(
                    Pattern.compile(
                        String.format(format, filename),
                        Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE
                    )
                )
                patterns.add(
                    Pattern.compile(
                        String.format(format, songtitle),
                        Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE
                    )
                )
                val files =
                    dir.listFiles { f: File ->
                        for (pattern in patterns) {
                            if (pattern.matcher(f.name).matches()) {
                                return@listFiles true
                            }
                        }
                        false
                    }
                if (files != null && files.isNotEmpty()) {
                    for (f in files) {
                        try {
                            val newLyrics =
                                FileUtil.read(f)
                            if (newLyrics != null && newLyrics.trim { it <= ' ' }.isNotEmpty()) {
                                if (AbsSynchronizedLyrics.isSynchronized(newLyrics)) {
                                    return newLyrics
                                }
                                lyrics = newLyrics
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        return lyrics
    }

    @JvmStatic
    fun getMediaStoreAlbumCoverUri(albumId: Long): Uri {
        val sArtworkUri = "content://media/external/audio/albumart".toUri()
        return ContentUris.withAppendedId(sArtworkUri, albumId)
    }


    fun getPlaylistInfoString(
        context: Context,
        songs: List<Song>,
    ): String {
        val duration = getTotalDuration(songs)
        return buildInfoString(
            getSongCountString(context, songs.size),
            getReadableDurationString(duration)
        )
    }

    fun playlistInfoString(
        context: Context,
        songs: List<SongEntity>,
    ): String {
        return getSongCountString(context, songs.size)
    }

    fun getReadableDurationString(songDurationMillis: Long): String {
        var minutes = songDurationMillis / 1000 / 60
        val seconds = songDurationMillis / 1000 % 60
        return if (minutes < 60) {
            String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds
            )
        } else {
            val hours = minutes / 60
            minutes %= 60
            String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        }
    }

    fun getSectionName(mediaTitle: String?, stripPrefix: Boolean = false): String {
        var musicMediaTitle = mediaTitle
        return try {
            if (musicMediaTitle.isNullOrEmpty()) {
                return "-"
            }
            musicMediaTitle = musicMediaTitle.trim { it <= ' ' }.lowercase()
            if (stripPrefix) {
                if (musicMediaTitle.startsWith("the ")) {
                    musicMediaTitle = musicMediaTitle.substring(4)
                } else if (musicMediaTitle.startsWith("a ")) {
                    musicMediaTitle = musicMediaTitle.substring(2)
                }
            }

            if (musicMediaTitle.isEmpty()) {
                ""
            } else musicMediaTitle.substring(0, 1).uppercase()
        } catch (e: Exception) {
            ""
        }
    }

    fun getSongCountString(context: Context, songCount: Int): String {
        val songString = if (songCount == 1) context.resources
            .getString(R.string.song) else context.resources.getString(R.string.songs)
        return "$songCount $songString"
    }

    fun getSongFileUri(songId: Long): Uri {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            songId
        )
    }

    fun getSongFilePath(context: Context, uri: Uri): String {
        val projection = arrayOf(Constants.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use {
            if (it.moveToFirst()) {
                return it.getString(0)
            }
        }
        return ""
    }

    fun getTotalDuration(songs: List<Song>): Long {
        var duration: Long = 0
        for (i in songs.indices) {
            duration += songs[i].duration
        }
        return duration
    }

    fun getYearString(year: Int): String {
        return if (year > 0) year.toString() else "-"
    }

    fun indexOfSongInList(songs: List<Song>, songId: Long): Int {
        return songs.indexOfFirst { it.id == songId }
    }

    fun getDateModifiedString(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        val pattern = "dd/MM/yyyy hh:mm:ss"
        calendar.timeInMillis = date
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return formatter.format(calendar.time)
    }

    fun insertAlbumArt(
        context: Context,
        albumId: Long,
        path: String?,
    ) {
        val contentResolver = context.contentResolver
        val artworkUri = "content://media/external/audio/albumart".toUri()
        contentResolver.delete(ContentUris.withAppendedId(artworkUri, albumId), null, null)
        val values = contentValuesOf(
            "album_id" to albumId,
            "_data" to path
        )
        contentResolver.insert(artworkUri, values)
        contentResolver.notifyChange(artworkUri, null)
    }

    fun isArtistNameUnknown(artistName: String?): Boolean {
        if (artistName.isNullOrEmpty()) {
            return false
        }
        if (artistName == Artist.UNKNOWN_ARTIST_DISPLAY_NAME) {
            return true
        }
        val tempName = artistName.trim { it <= ' ' }.lowercase()
        return tempName == "unknown" || tempName == "<unknown>"
    }

    fun isVariousArtists(artistName: String?): Boolean {
        if (artistName.isNullOrEmpty()) {
            return false
        }
        if (artistName == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
            return true
        }
        return false
    }

    private val repository = get<Repository>()
    suspend fun toggleFavorite(song: Song) {
        withContext(IO) {
            val playlist: PlaylistEntity = repository.favoritePlaylist()
            val songEntity = song.toSongEntity(playlist.playListId)
            val isFavorite = repository.isFavoriteSong(songEntity).isNotEmpty()
            if (isFavorite) {
                repository.removeSongFromPlaylist(songEntity)
            } else {
                repository.insertSongs(listOf(song.toSongEntity(playlist.playListId)))
            }
        }
    }

    suspend fun isFavorite(song: Song) = repository.isSongFavorite(song.id)

    fun deleteTracks(
        activity: FragmentActivity,
        songs: List<Song>,
        safUris: List<Uri>?,
        callback: Runnable?,
    ) {
        val songRepository: SongRepository = get()
        val projection = arrayOf(
            BaseColumns._ID, Constants.DATA
        )
        // Split the query into multiple batches, and merge the resulting cursors
        var batchStart: Int
        var batchEnd = 0
        val batchSize =
            1000000 / 10 // 10^6 being the SQLite limite on the query lenth in bytes, 10 being the max number of digits in an int, used to store the track ID
        val songCount = songs.size

        while (batchEnd < songCount) {
            batchStart = batchEnd

            val selection = StringBuilder()
            selection.append(BaseColumns._ID + " IN (")

            var i = 0
            while (i < batchSize - 1 && batchEnd < songCount - 1) {
                selection.append(songs[batchEnd].id)
                selection.append(",")
                i++
                batchEnd++
            }
            // The last element of a batch
            // The last element of a batch
            selection.append(songs[batchEnd].id)
            batchEnd++
            selection.append(")")

            try {
                val cursor = activity.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection.toString(),
                    null, null
                )
                if (cursor != null) {
                    // Step 1: Remove selected tracks from the current playlist, as well
                    // as from the album art cache
                    cursor.moveToFirst()
                    while (!cursor.isAfterLast) {
                        val id = cursor.getLong(BaseColumns._ID)
                        val song: Song = songRepository.song(id)
                        removeFromQueue(song)
                        cursor.moveToNext()
                    }

                    // Step 2: Remove selected tracks from the database
                    activity.contentResolver.delete(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        selection.toString(), null
                    )
                    // Step 3: Remove files from card
                    cursor.moveToFirst()
                    var index = batchStart
                    while (!cursor.isAfterLast) {
                        val name = cursor.getString(1)
                        val safUri =
                            if (safUris == null || safUris.size <= index) null else safUris[index]
                        SAFUtil.delete(activity, name, safUri)
                        index++
                        cursor.moveToNext()
                    }
                    cursor.close()
                }
            } catch (ignored: SecurityException) {

            }
            activity.contentResolver.notifyChange("content://media".toUri(), null)
            activity.runOnUiThread {
                activity.showToast(activity.getString(R.string.deleted_x_songs, songCount))
                callback?.run()
            }
        }
    }

    suspend fun deleteTracks(context: Context, songs: List<Song>) {
        val projection = arrayOf(BaseColumns._ID, Constants.DATA)
        val selection = StringBuilder()
        selection.append(BaseColumns._ID + " IN (")
        for (i in songs.indices) {
            selection.append(songs[i].id)
            if (i < songs.size - 1) {
                selection.append(",")
            }
        }
        selection.append(")")
        var deletedCount = 0
        try {
            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection.toString(),
                null, null
            )
            if (cursor != null) {
                removeFromQueue(songs)

                // Step 2: Remove files from card
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(0)
                    val name: String = cursor.getString(1)
                    try { // File.delete can throw a security exception
                        if (SAFUtil.delete(context, name, null)) {
                            // Step 3: Remove selected track from the database
                            context.contentResolver.delete(
                                ContentUris.withAppendedId(
                                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                    id.toLong()
                                ), null, null
                            )
                            deletedCount++
                        } else {
                            Log.e("MusicUtils", "Failed to delete file $name")
                        }
                        cursor.moveToNext()
                    } catch (ex: SecurityException) {
                        cursor.moveToNext()
                    } catch (e: NullPointerException) {
                        Log.e("MusicUtils", "Failed to find file $name")
                    }
                }
                cursor.close()
            }
            withContext(Dispatchers.Main) {
                context.showToast(context.getString(R.string.deleted_x_songs, deletedCount))
            }

        } catch (ignored: SecurityException) {
        }
    }

    fun songByGenre(genreId: Long): Song {
        return repository.getSongByGenre(genreId)
    }
}