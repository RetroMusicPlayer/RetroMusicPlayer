package code.name.monkey.retromusic.util

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun copyFileToUri(context: Context, fromFile: File, toUri: Uri) {
        context.contentResolver.openOutputStream(toUri)
            ?.use { output ->
                fromFile.inputStream().use { input ->
                    input.copyTo(output)
                }
            }
    }
}