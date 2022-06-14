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

package code.name.monkey.retromusic.util

import android.app.Activity
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.play.core.review.ReviewManagerFactory

object AppRater {
    private const val DO_NOT_SHOW_AGAIN = "do_not_show_again"// Package Name
    private const val APP_RATING = "app_rating"// Package Name
    private const val LAUNCH_COUNT = "launch_count"// Package Name
    private const val DATE_FIRST_LAUNCH = "date_first_launch"// Package Name

    private const val DAYS_UNTIL_PROMPT = 3//Min number of days
    private const val LAUNCHES_UNTIL_PROMPT = 5//Min number of launches

    fun appLaunched(context: Activity) {
        val prefs = context.getSharedPreferences(APP_RATING, 0)
        if (prefs.getBoolean(DO_NOT_SHOW_AGAIN, false)) {
            return
        }

        prefs.edit {

            // Increment launch counter
            val launchCount = prefs.getLong(LAUNCH_COUNT, 0) + 1
            putLong(LAUNCH_COUNT, launchCount)

            // Get date of first launch
            var dateFirstLaunch = prefs.getLong(DATE_FIRST_LAUNCH, 0)
            if (dateFirstLaunch == 0L) {
                dateFirstLaunch = System.currentTimeMillis()
                putLong(DATE_FIRST_LAUNCH, dateFirstLaunch)
            }

            // Wait at least n days before opening
            if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
                if (System.currentTimeMillis() >= dateFirstLaunch + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                    //showRateDialog(context, editor)
                    showPlayStoreReviewDialog(context, this)
                }
            }
        }
    }

    private fun showPlayStoreReviewDialog(context: Activity, editor: SharedPreferences.Editor) {
        val manager = ReviewManagerFactory.create(context)
        val flow = manager.requestReviewFlow()
        flow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flowManager = manager.launchReviewFlow(context, reviewInfo)
                flowManager.addOnCompleteListener {
                    if (it.isSuccessful) {
                        editor.putBoolean(DO_NOT_SHOW_AGAIN, true)
                        editor.commit()
                    }
                }
            }
        }
    }
}