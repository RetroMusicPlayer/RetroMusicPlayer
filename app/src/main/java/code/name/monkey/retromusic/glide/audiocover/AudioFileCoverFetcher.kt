package code.name.monkey.retromusic.glide.audiocover

import android.media.MediaMetadataRetriever
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import org.jaudiotagger.audio.mp3.MP3File
import java.io.*

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
class AudioFileCoverFetcher(private val model: AudioFileCover) : DataFetcher<InputStream> {
    private var stream: FileInputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val retriever = MediaMetadataRetriever()
        val data: InputStream?
        try {
            retriever.setDataSource(model.filePath)
            val picture = retriever.embeddedPicture
            if (picture != null) {
                data = ByteArrayInputStream(picture)
            } else {
                data = fallback(model.filePath)
            }
            callback.onDataReady(data)
        } catch (e: FileNotFoundException) {
            callback.onLoadFailed(e)
        } finally {
            retriever.release()
        }
    }


    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }


    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }

    @Throws(FileNotFoundException::class)
    private fun fallback(path: String): InputStream? {
        try {
            val mp3File = MP3File(path)
            if (mp3File.hasID3v2Tag()) {
                val art = mp3File.tag.firstArtwork
                if (art != null) {
                    val imageData = art.binaryData
                    return ByteArrayInputStream(imageData)
                }
            }
            // If there are any exceptions, we ignore them and continue to the other fallback method
        } catch (ignored: Exception) {
        }

        // Method 2: look for album art in external files
        val parent = File(path).parentFile
        for (fallback in FALLBACKS) {
            val cover = File(parent, fallback)
            if (cover.exists()) {
                stream = FileInputStream(cover)
                return stream
            }
        }
        return null
    }

    override fun cleanup() {
        // already cleaned up in loadData and ByteArrayInputStream will be GC'd
        if (stream != null) {
            try {
                stream!!.close()
            } catch (ignore: IOException) {
                // can't do much about it
            }

        }
    }

    override fun cancel() {
        // cannot cancel
    }

    companion object {

        private val FALLBACKS = arrayOf("cover.jpg", "album.jpg", "folder.jpg", "cover.png", "album.png", "folder.png")
    }
}