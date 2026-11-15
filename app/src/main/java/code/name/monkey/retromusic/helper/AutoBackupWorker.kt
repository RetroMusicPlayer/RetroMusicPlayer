/*
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.helper

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import code.name.monkey.retromusic.util.PreferenceUtil
import java.util.concurrent.TimeUnit

/** Writes a full backup into the chosen backup folder, then trims the old automatic ones. */
class AutoBackupWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!PreferenceUtil.autoBackupEnabled) return Result.success()
        val done = BackupHelper.createBackup(
            applicationContext, BackupHelper.autoBackupName(), silent = true
        )
        if (!done) return Result.failure()
        BackupHelper.deleteOldAutoBackups(applicationContext, PreferenceUtil.autoBackupKeep)
        return Result.success()
    }
}

object AutoBackupScheduler {
    private const val WORK_NAME = "auto_backup"

    /** Call on app start and whenever the automatic backup settings change. */
    fun update(context: Context) {
        val manager = WorkManager.getInstance(context)
        if (!PreferenceUtil.autoBackupEnabled) {
            manager.cancelUniqueWork(WORK_NAME)
            return
        }
        val request = PeriodicWorkRequestBuilder<AutoBackupWorker>(
            PreferenceUtil.autoBackupHours.toLong(), TimeUnit.HOURS
        ).setConstraints(Constraints.Builder().setRequiresStorageNotLow(true).build()).build()
        manager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }
}
