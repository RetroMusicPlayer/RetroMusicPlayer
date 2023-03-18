package code.name.monkey.retromusic.activities.tageditor

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.misc.UpdateToastMediaScannerCompletionListener
import code.name.monkey.retromusic.model.AudioTagInfo
import code.name.monkey.retromusic.util.MusicUtil.createAlbumArtFile
import code.name.monkey.retromusic.util.MusicUtil.deleteAlbumArt
import code.name.monkey.retromusic.util.MusicUtil.insertAlbumArt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.CannotWriteException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldDataInvalidException
import org.jaudiotagger.tag.TagException
import org.jaudiotagger.tag.images.AndroidArtwork
import org.jaudiotagger.tag.images.Artwork
import java.io.File
import java.io.IOException

class TagWriter {

    companion object {

        suspend fun scan(context: Context, toBeScanned: List<String?>?) {
            if (toBeScanned.isNullOrEmpty()) {
                Log.i("scan", "scan: Empty")
                context.showToast("Scan file from folder")
                return
            }
            MediaScannerConnection.scanFile(
                context,
                toBeScanned.toTypedArray(),
                null,
                withContext(Dispatchers.Main) {
                    if (context is Activity) UpdateToastMediaScannerCompletionListener(
                        context, toBeScanned
                    ) else null
                }
            )
        }

        suspend fun writeTagsToFiles(context: Context, info: AudioTagInfo) {
            withContext(Dispatchers.IO) {
                var artwork: Artwork? = null
                var albumArtFile: File? = null
                if (info.artworkInfo?.artwork != null) {
                    try {
                        albumArtFile = createAlbumArtFile(context).canonicalFile
                        info.artworkInfo.artwork.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            albumArtFile.outputStream()
                        )
                        artwork = AndroidArtwork.createArtworkFromFile(albumArtFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                var wroteArtwork = false
                var deletedArtwork = false
                for (filePath in info.filePaths!!) {
                    try {
                        val audioFile = AudioFileIO.read(File(filePath))
                        val tag = audioFile.tagOrCreateAndSetDefault
                        if (info.fieldKeyValueMap != null) {
                            for ((key, value) in info.fieldKeyValueMap) {
                                try {
                                    tag.setField(key, value)
                                } catch (e: FieldDataInvalidException) {
                                    withContext(Dispatchers.Main) {
                                        context.showToast(R.string.could_not_write_tags_to_file)
                                    }
                                    return@withContext listOf<File>()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (info.artworkInfo != null) {
                            if (info.artworkInfo.artwork == null) {
                                tag.deleteArtworkField()
                                deletedArtwork = true
                            } else if (artwork != null) {
                                tag.deleteArtworkField()
                                tag.setField(artwork)
                                wroteArtwork = true
                            }
                        }
                        audioFile.commit()
                    } catch (e: CannotReadException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: CannotWriteException) {
                        e.printStackTrace()
                    } catch (e: TagException) {
                        e.printStackTrace()
                    } catch (e: ReadOnlyFileException) {
                        e.printStackTrace()
                    } catch (e: InvalidAudioFrameException) {
                        e.printStackTrace()
                    }
                }
                if (wroteArtwork) {
                    insertAlbumArt(context, info.artworkInfo!!.albumId, albumArtFile!!.path)
                } else if (deletedArtwork) {
                    deleteAlbumArt(context, info.artworkInfo!!.albumId)
                }
                scan(context, info.filePaths)
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        suspend fun writeTagsToFilesR(context: Context, info: AudioTagInfo): List<File> =
            withContext(Dispatchers.IO) {
                val cacheFiles = mutableListOf<File>()
                var artwork: Artwork? = null
                var albumArtFile: File? = null
                if (info.artworkInfo?.artwork != null) {
                    try {
                        albumArtFile = createAlbumArtFile(context).canonicalFile
                        info.artworkInfo.artwork.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            albumArtFile.outputStream()
                        )
                        artwork = AndroidArtwork.createArtworkFromFile(albumArtFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                var wroteArtwork = false
                var deletedArtwork = false
                for (filePath in info.filePaths!!) {
                    try {
                        val originFile = File(filePath)
                        val cacheFile = File(context.cacheDir, originFile.name)
                        cacheFiles.add(cacheFile)
                        originFile.inputStream().use { input ->
                            cacheFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        val audioFile = AudioFileIO.read(cacheFile)
                        val tag = audioFile.tagOrCreateAndSetDefault
                        if (info.fieldKeyValueMap != null) {
                            for ((key, value) in info.fieldKeyValueMap) {
                                try {
                                    tag.setField(key, value)
                                } catch (e: FieldDataInvalidException) {
                                    withContext(Dispatchers.Main) {
                                        context.showToast(R.string.could_not_write_tags_to_file)
                                    }
                                    return@withContext listOf<File>()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (info.artworkInfo != null) {
                            if (info.artworkInfo.artwork == null) {
                                tag.deleteArtworkField()
                                deletedArtwork = true
                            } else if (artwork != null) {
                                tag.deleteArtworkField()
                                tag.setField(artwork)
                                wroteArtwork = true
                            }
                        }
                        audioFile.commit()
                    } catch (e: CannotReadException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: CannotWriteException) {
                        e.printStackTrace()
                    } catch (e: TagException) {
                        e.printStackTrace()
                    } catch (e: ReadOnlyFileException) {
                        e.printStackTrace()
                    } catch (e: InvalidAudioFrameException) {
                        e.printStackTrace()
                    }
                }
                if (wroteArtwork) {
                    insertAlbumArt(context, info.artworkInfo!!.albumId, albumArtFile!!.path)
                } else if (deletedArtwork) {
                    deleteAlbumArt(context, info.artworkInfo!!.albumId)
                }
                cacheFiles
            }
    }
}