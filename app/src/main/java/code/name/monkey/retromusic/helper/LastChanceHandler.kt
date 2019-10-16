/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.helper

import android.content.Intent
import androidx.core.app.ActivityCompat
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.activities.bugreport.ErrorHandlerActivity

class TopExceptionHandler() : Thread.UncaughtExceptionHandler {
    private val defaultUEH: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        var arr = e.stackTrace
        var report = e.toString() + "\n\n"

        report += "--------- Stack trace ---------\n\n"
        for (i in arr.indices) {
            report += "    " + arr[i].toString() + "\n"
        }
        report += "-------------------------------\n\n"

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause

        report += "--------- Cause ---------\n\n"
        val cause = e.cause
        if (cause != null) {
            report += cause.toString() + "\n\n"
            arr = cause.stackTrace
            for (i in arr.indices) {
                report += "    " + arr[i].toString() + "\n"
            }
        }
        report += "-------------------------------\n\n"
        ActivityCompat.startActivity(App.getContext(), Intent(App.getContext(), ErrorHandlerActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("error", report), null)
        defaultUEH.uncaughtException(t, e)
    }
}