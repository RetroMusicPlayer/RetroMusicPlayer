package code.name.monkey.retromusic.glide.audiocover

import android.media.MediaMetadataRetriever
import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.audio.mp3.MP3File
import org.jaudiotagger.tag.TagException
import java.io.*


class AudioFileCoverFetcher(private val model: AudioFileCover) : DataFetcher<InputStream> {
    private var stream: FileInputStream? = null

    override fun getId(): String {
        // makes sure we never ever return null here
        return model.filePath
    }

    @Throws(Exception::class)
    override fun loadData(priority: Priority): InputStream? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(model.filePath)
            val picture = retriever.embeddedPicture
            return if (picture != null) {
                ByteArrayInputStream(picture)
            } else {
                fallback(model.filePath)
            }
        } finally {
            retriever.release()
        }
    }

    @Throws(FileNotFoundException::class)
    private fun fallback(path: String): InputStream? {
        // Method 1: use embedded high resolution album art if there is any
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
        } catch (ignored: ReadOnlyFileException) {
        } catch (ignored: InvalidAudioFrameException) {
        } catch (ignored: TagException) {
        } catch (ignored: IOException) {
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