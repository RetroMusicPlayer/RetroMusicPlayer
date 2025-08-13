package code.name.monkey.retromusic.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import code.name.monkey.retromusic.helper.ENHANCEMENT_NOTIFICATION_ID
import androidx.core.app.NotificationCompat

object InternetConnection {
    // Helper function to check for internet connection
    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun waitForConnection(context: Context, notificationBuilder: NotificationCompat.Builder, notificationManager: android.app.NotificationManager){
        if (!hasInternetConnection(context)) {
            var totalWaitTimeMillis = 0L
            val initialSleepTimeMillis = 2 * 60 * 1000L
            val thirtyMinThresholdSleepTimeMillis = 11000L * 60L * 15L // 9,900,000 ms = 165 minutes
            val oneHourThresholdSleepTimeMillis = 20 * 60 * 1000L     // 1,200,000 ms = 20 minutes
            val thirtyMinutesMillis = 30 * 60 * 1000L
            val oneHourMillis = 60 * 60 * 1000L

            var sleepDurationForThisIterationMillis: Long // Declare type

            while (!hasInternetConnection(context)) {
                // Determine sleep duration for this iteration based on total time waited so far
                if (totalWaitTimeMillis >= oneHourMillis) {
                    sleepDurationForThisIterationMillis = oneHourThresholdSleepTimeMillis
                } else if (totalWaitTimeMillis >= thirtyMinutesMillis) {
                    sleepDurationForThisIterationMillis = thirtyMinThresholdSleepTimeMillis
                } else {
                    sleepDurationForThisIterationMillis = initialSleepTimeMillis
                }

                val nextCheckInMinutes = sleepDurationForThisIterationMillis / (60 * 1000)
                val totalWaitTimeSoFarMinutes = totalWaitTimeMillis / (60 * 1000)
                val waitMsg = if (totalWaitTimeMillis == 0L) {
                    "Waiting for internet. Retrying in $nextCheckInMinutes min."
                } else {
                    "Still no internet. Retrying in $nextCheckInMinutes min. Total wait: $totalWaitTimeSoFarMinutes min."
                }

                notificationBuilder
                    .setContentText(waitMsg)
                    .setProgress(0, 0, true) // Indeterminate progress
                    .setOngoing(true)
                notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())

                try {
                    Thread.sleep(sleepDurationForThisIterationMillis)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt() // Restore interrupted status
                    notificationBuilder
                        .setContentText("Enhancement interrupted while waiting for internet.")
                        .setProgress(0, 0, false)
                        .setOngoing(false)
                    notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
                    return
                }
                totalWaitTimeMillis += sleepDurationForThisIterationMillis // Accumulate wait time
            }
                notificationBuilder
                    .setContentText("Continuing Enhancement: Internet connection restored.")
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                notificationManager.notify(ENHANCEMENT_NOTIFICATION_ID, notificationBuilder.build())
        }
    }
}