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

package code.name.monkey.retromusic.fragments.settings

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.preference.TwoStatePreference
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil


/**
 * @author Hemanth S (h4h13).
 */

class NotificationSettingsFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {

        val classicNotification: TwoStatePreference? = findPreference("classic_notification")
        if (VERSION.SDK_INT < VERSION_CODES.N) {
            classicNotification?.isVisible = false
        } else {
            classicNotification?.apply {
                isChecked = PreferenceUtil.getInstance().classicNotification()
                setOnPreferenceChangeListener { _, newValue ->
                    // Save preference
                    PreferenceUtil.getInstance().setClassicNotification(newValue as Boolean)
                    invalidateSettings()
                    true
                }
            }
        }

        val coloredNotification: TwoStatePreference? = findPreference("colored_notification")
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            coloredNotification?.isEnabled = PreferenceUtil.getInstance().classicNotification()
        } else {
            coloredNotification?.apply {
                isChecked = PreferenceUtil.getInstance().coloredNotification()
                setOnPreferenceChangeListener { _, newValue ->
                    PreferenceUtil.getInstance().setColoredNotification(newValue as Boolean)
                    true
                }
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_notification)
    }
}
