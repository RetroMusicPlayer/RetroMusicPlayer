package code.name.monkey.retromusic.fragments.backup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.activities.MainActivity
import code.name.monkey.retromusic.helper.BackupContent
import code.name.monkey.retromusic.helper.BackupEntry
import code.name.monkey.retromusic.helper.BackupHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.system.exitProcess


class BackupViewModel : ViewModel() {
    private val backupsMutableLiveData = MutableLiveData<List<BackupEntry>>()
    val backupsLiveData: LiveData<List<BackupEntry>> = backupsMutableLiveData

    fun loadBackups(context: Context) {
        val appContext = context.applicationContext
        // Listing a picked folder goes through a content provider, so keep it off the main thread.
        viewModelScope.launch(Dispatchers.IO) {
            backupsMutableLiveData.postValue(BackupHelper.listBackups(appContext))
        }
    }

    suspend fun restoreBackup(activity: Activity, inputStream: InputStream?, contents: List<BackupContent>) {
        BackupHelper.restoreBackup(activity, inputStream, contents)
        if (contents.contains(BackupContent.SETTINGS) or contents.contains(BackupContent.CUSTOM_ARTIST_IMAGES)) {
            // We have to restart App when Preferences i.e. Settings or Artist Images are to be restored
            withContext(Dispatchers.Main) {
                val intent = Intent(
                    activity,
                    MainActivity::class.java
                )
                activity.startActivity(intent)
                exitProcess(0)
            }
        }
    }
}