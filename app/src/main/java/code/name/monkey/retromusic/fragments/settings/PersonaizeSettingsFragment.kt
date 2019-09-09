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

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.TwoStatePreference
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil


class PersonaizeSettingsFragment : AbsSettingsFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun invalidateSettings() {
        val cornerWindow: TwoStatePreference = findPreference("corner_window")!!
        cornerWindow.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean && !App.isProVersion()) {
                showProToastAndNavigate(activity!!.getString(R.string.pref_title_round_corners))
                return@setOnPreferenceChangeListener false
            }
            activity!!.recreate()
            return@setOnPreferenceChangeListener true
        }
        val toggleFullScreen: TwoStatePreference = findPreference("toggle_full_screen")!!
        toggleFullScreen.setOnPreferenceChangeListener { _, _ ->
            activity!!.recreate()
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_ui)
        addPreferencesFromResource(R.xml.pref_window)
        addPreferencesFromResource(R.xml.pref_lockscreen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PreferenceUtil.getInstance(requireContext()).registerOnSharedPreferenceChangedListener(this)

        var preference: Preference? = findPreference("album_grid_style")
        setSummary(preference!!)
        preference = findPreference("artist_grid_style")
        setSummary(preference!!)
        preference = findPreference("home_artist_grid_style")
        setSummary(preference!!)
        preference = findPreference("tab_text_mode")
        setSummary(preference!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PreferenceUtil.getInstance(requireContext()).unregisterOnSharedPreferenceChangedListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            PreferenceUtil.CAROUSEL_EFFECT -> invalidateSettings()
        }
    }
}
