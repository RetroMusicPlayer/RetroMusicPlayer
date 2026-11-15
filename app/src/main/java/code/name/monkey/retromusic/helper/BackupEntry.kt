package code.name.monkey.retromusic.helper

import android.net.Uri
import java.io.File

/**
 * One backup file. [file] is set only for the old default folder; backups in a folder the user
 * picked are reached through [uri] (Storage Access Framework).
 */
data class BackupEntry(val name: String, val uri: Uri, val file: File?)
