package code.name.monkey.retromusic.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.provider.MediaStore
import java.io.File
import java.util.*
import android.content.Intent




class MediaStoreUtil {
    companion object {
        public fun addToMediaStore(file: File, context: Context): Unit {
            val cursor: Cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media._ID),
                "${MediaStore.Audio.Media.DATA}=?",
                arrayOf(file.absolutePath),
                null
            )!!

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = context.contentResolver
                val audioCollection = MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
                val audioDetail = ContentValues()
                audioDetail.put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
                audioDetail.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
                audioDetail.put(MediaStore.Audio.Media.RELATIVE_PATH,"Music/" + UUID.randomUUID().toString())
                audioDetail.put(MediaStore.Audio.Media.IS_PENDING, 1)
                audioDetail.put(MediaStore.Audio.Media.DATA, file.absolutePath)
                val uri = resolver.insert(audioCollection, audioDetail)
                audioDetail.clear()
                audioDetail.put(MediaStore.Audio.Media.IS_PENDING, 0)
                resolver.update(audioCollection, audioDetail, null, null)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                return uri!!
            } else {
                val values = ContentValues()
                values.put(MediaStore.Audio.Media.DATA, file.absolutePath)
                return context.contentResolver.insert(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values
                )!!
            }*/
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
        }
    }
}