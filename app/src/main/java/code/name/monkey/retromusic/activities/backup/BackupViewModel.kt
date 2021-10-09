package code.name.monkey.retromusic.activities.backup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.helper.BackupHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.system.exitProcess


class BackupViewModel : ViewModel() {
    private val backupsMutableLiveData = MutableLiveData<List<File>>()
    val backupsLiveData = backupsMutableLiveData

    fun loadBackups() {
        viewModelScope.launch {
            File(BackupHelper.backupRootPath).listFiles { _, name ->
                return@listFiles name.endsWith(BackupHelper.BACKUP_EXTENSION)
            }?.toList()?.let {
                backupsMutableLiveData.value = it
            }
        }
    }

    suspend fun restoreBackup(activity: Activity, file: File) {
        BackupHelper.restoreBackup(activity, file)
        withContext(Dispatchers.Main) {
            val intent = Intent(
                activity,
                activity::class.java
            )
            activity.startActivity(intent)
            exitProcess(0)
        }
    }
}