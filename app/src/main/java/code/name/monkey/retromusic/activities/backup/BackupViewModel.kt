package code.name.monkey.retromusic.activities.backup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.helper.BackupHelper
import kotlinx.coroutines.launch
import java.io.File

class BackupViewModel : ViewModel() {
    private val backupsMutableLiveData = MutableLiveData<List<File>>()
    val backupsLiveData = backupsMutableLiveData

    fun loadBackups() {
        viewModelScope.launch {
            File(BackupHelper.backupRootPath).listFiles { _, name ->
                return@listFiles name.endsWith(BackupHelper.backupExt)
            }?.toList()?.let {
                backupsMutableLiveData.value = it
            }
        }
    }
}