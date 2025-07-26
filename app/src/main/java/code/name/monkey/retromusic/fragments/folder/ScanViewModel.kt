package code.name.monkey.retromusic.fragments.folder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData

/**
 * @Author by Pinankh Patel
 * Created on Date = 15-06-2025  13:02
 * Github = https://github.com/Pinankh
 * LinkdIN = https://www.linkedin.com/in/pinankh-patel-19400350/
 * Stack Overflow = https://stackoverflow.com/users/4564376/pinankh
 * Medium = https://medium.com/@pinankhpatel
 * Email = pinankhpatel@gmail.com
 */

sealed class ScanResult {
    object InProgress : ScanResult()
    data class Success(val message: String) : ScanResult()
    data class Path(val path: String) : ScanResult()
    object NotStarted : ScanResult()
}

class ScanViewModel : ViewModel() {

    private val _scanStatus = MutableLiveData<ScanResult>(ScanResult.NotStarted)
    val scanStatus: LiveData<ScanResult> = _scanStatus

    fun notifyScanStarted() {
        _scanStatus.postValue(ScanResult.InProgress)
    }

    fun notifyScanFinishedSuccessfully(message: String) {
        _scanStatus.postValue(ScanResult.Success(message))
    }


    fun pathScanned(filePath: String) {
        _scanStatus.postValue(ScanResult.Path(filePath))
    }

    fun resetScanStatus() {
        _scanStatus.postValue(ScanResult.NotStarted)
    }
}