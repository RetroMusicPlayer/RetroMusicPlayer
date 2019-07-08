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

import android.graphics.Color
import android.graphics.Color.BLUE
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.TwoStatePreference
import code.name.monkey.appthemehelper.*
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEColorPreference
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.colorChooser


/**
 * @author Hemanth S (h4h13).
 */

class ThemeSettingsFragment : AbsSettingsFragment() {

    override fun invalidateSettings() {
        val primaryColorPref: ATEColorPreference? = findPreference("primary_color")
        primaryColorPref?.let {
            it.isVisible = PreferenceUtil.getInstance().generalTheme == R.style.Theme_RetroMusic_Color
            val primaryColor = ThemeStore.primaryColor(activity!!)
            it.setColor(primaryColor, ColorUtil.darkenColor(primaryColor))
            it.setOnPreferenceClickListener {
                MaterialDialog(activity!!, BottomSheet()).show {
                    title(R.string.primary_color)
                    positiveButton(R.string.set)
                    colorChooser(initialSelection = BLUE,
                            allowCustomArgb = true,
                            colors = PRIMARY_COLORS,
                            subColors = PRIMARY_COLORS_SUB) { _, color ->

                        val theme = if (ColorUtil.isColorLight(color))
                            PreferenceUtil.getThemeResFromPrefValue("light")
                        else
                            PreferenceUtil.getThemeResFromPrefValue("dark")

                        ThemeStore.editTheme(context).activityTheme(theme).primaryColor(color).commit()

                        if (VersionUtils.hasNougatMR())
                            DynamicShortcutManager(context).updateDynamicShortcuts()
                        activity!!.recreate()
                    }
                }
                true
            }
        }

        val generalTheme: Preference? = findPreference("general_theme")

        generalTheme?.let {
            setSummary(it)
            it.setOnPreferenceChangeListener { _, newValue ->
                val theme = newValue as String
                println(newValue)
                if (theme == "color" && !App.isProVersion) {
                    showProToastAndNavigate("Color theme")
                    return@setOnPreferenceChangeListener false
                }

                if (theme == "color") {
                    primaryColorPref?.isVisible = true
                }

                setSummary(generalTheme, newValue)

                when (theme) {
                    "light" -> ThemeStore.editTheme(context!!).primaryColor(Color.WHITE).commit()
                    "black" -> ThemeStore.editTheme(context!!).primaryColor(Color.BLACK).commit()
                    "dark" -> ThemeStore.editTheme(context!!).primaryColor(ContextCompat.getColor(context!!, R.color.md_grey_900)).commit()
                    "color" -> ThemeStore.editTheme(context!!).primaryColor(ContextCompat.getColor(context!!, R.color.md_blue_grey_800)).commit()
                }

                ThemeStore.editTheme(activity!!)
                        .activityTheme(PreferenceUtil.getThemeResFromPrefValue(theme))
                        .commit()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    activity?.setTheme(PreferenceUtil.getThemeResFromPrefValue(theme))
                    DynamicShortcutManager(activity!!).updateDynamicShortcuts()
                }
                activity?.recreate()
                true
            }
        }

        val accentColorPref: ATEColorPreference = findPreference("accent_color")!!
        val accentColor = ThemeStore.accentColor(activity!!)
        accentColorPref.setColor(accentColor, ColorUtil.darkenColor(accentColor))

        accentColorPref.setOnPreferenceClickListener {
            MaterialDialog(activity!!, BottomSheet()).show {
                title(R.string.accent_color)
                positiveButton(R.string.set)
                colorChooser(colors = ACCENT_COLORS, allowCustomArgb = true, subColors = ACCENT_COLORS_SUB) { _, color ->
                    /*var colorFinal = Color.BLACK;
                    if (!ColorUtil.isColorSaturated(color)) {
                        colorFinal = color
                    }*/
                    ThemeStore.editTheme(context).accentColor(color).commit()
                    if (VersionUtils.hasNougatMR())
                        DynamicShortcutManager(context).updateDynamicShortcuts()
                    activity!!.recreate()
                }
            }
            return@setOnPreferenceClickListener true
        }

        val colorAppShortcuts: TwoStatePreference = findPreference("should_color_app_shortcuts")!!
        if (!VersionUtils.hasNougatMR()) {
            colorAppShortcuts.isVisible = false
        } else {
            colorAppShortcuts.isChecked = PreferenceUtil.getInstance().coloredAppShortcuts()
            colorAppShortcuts.setOnPreferenceChangeListener { _, newValue ->
                // Save preference
                PreferenceUtil.getInstance().setColoredAppShortcuts(newValue as Boolean)
                DynamicShortcutManager(activity!!).updateDynamicShortcuts()
                true
            }
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_general)
    }
}
