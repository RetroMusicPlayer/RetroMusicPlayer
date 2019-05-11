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

import android.os.Build
import android.os.Bundle
import androidx.preference.TwoStatePreference
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.util.PreferenceUtil

/**
 * @author Hemanth S (h4h13).
 */

class NotificationSettingsFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val classicNotification: TwoStatePreference = findPreference("classic_notification")!!
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            classicNotification.isVisible = false
        } else {
            classicNotification.isChecked = PreferenceUtil.getInstance().classicNotification()
            classicNotification.setOnPreferenceChangeListener { _, newValue ->
                // Save preference
                PreferenceUtil.getInstance().setClassicNotification(newValue as Boolean)

                val service = MusicPlayerRemote.musicService
                if (service != null) {
                    service.initNotification()
                    service.updateNotification()
                }

                true
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_notification)
    }
}
