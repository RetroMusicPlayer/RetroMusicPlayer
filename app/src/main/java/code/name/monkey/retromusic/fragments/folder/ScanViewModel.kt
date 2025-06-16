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
// Sealed class to represent different states/results of the scan
sealed class ScanResult {
    object InProgress : ScanResult()
    data class Success(val message: String, val itemsScanned: Int = 0) : ScanResult()
    data class Error(val errorMessage: String) : ScanResult()
    data class Path(val path: String) : ScanResult()
    object NotStarted : ScanResult() // Could be the initial state
}

class ScanViewModel : ViewModel() {
    // LiveData now holds ScanResult
    private val _scanStatus = MutableLiveData<ScanResult>(ScanResult.NotStarted) // Initial state
    val scanStatus: LiveData<ScanResult> = _scanStatus

    fun notifyScanStarted() {
        //Log.d("ScanViewModel", "notifyScanStarted called on thread: ${Thread.currentThread().name}")
        // Post InProgress state when scan starts
        _scanStatus.postValue(ScanResult.InProgress)
    }

    /**
     * Call this when the scan has successfully finished.
     * @param message A success message to display.
     * @param count The number of items successfully scanned.
     */
    fun notifyScanFinishedSuccessfully(message: String, count: Int = 0) {
        //Log.d("ScanViewModel", "notifyScanFinishedSuccessfully called with message: '$message', count: $count on thread: ${Thread.currentThread().name}")
        _scanStatus.postValue(ScanResult.Success(message, count))
    }

    /**
     * Call this when the scan has finished with an error.
     * @param errorMessage A message describing the error.
     */
    fun notifyScanFinishedWithError(errorMessage: String) {
       // Log.d("ScanViewModel", "notifyScanFinishedWithError called with error: '$errorMessage' on thread: ${Thread.currentThread().name}")
        _scanStatus.postValue(ScanResult.Error(errorMessage))
    }

    /**
     *  Call this when path is scanning
     */
    fun pathScanned(filePath: String) {
        //Log.d("ScanViewModel", "pathScanned called on thread: ${Thread.currentThread().name}")
        _scanStatus.postValue(ScanResult.Path(filePath))
    }


    /**
     * Optional: Reset the scan status, e.g., to allow another scan or clear messages.
     */
    fun resetScanStatus() {
        //Log.d("ScanViewModel", "resetScanStatus called on thread: ${Thread.currentThread().name}")
        _scanStatus.postValue(ScanResult.NotStarted)
    }
}