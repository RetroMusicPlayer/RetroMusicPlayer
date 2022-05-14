package code.name.monkey.retromusic.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import code.name.monkey.retromusic.extensions.showToast
import java.io.File

object BackupUtil {
    fun createShareFileIntent(file: File, context: Context): Intent? {
        return try {
            Intent().setAction(Intent.ACTION_SEND).putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName,
                    file
                )
            ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).setType("*/*")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            context.showToast(
                "Could not share this file."
            )
            Intent()
        }
    }
}