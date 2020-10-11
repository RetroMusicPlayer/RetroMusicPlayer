/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import code.name.monkey.retromusic.extensions.applyToolbar
import code.name.monkey.retromusic.extensions.findNavController
import com.afollestad.materialdialogs.color.ColorChooserDialog
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AbsBaseActivity(), ColorChooserDialog.ColorCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)
        setupToolbar()
    }

    private fun setupToolbar() {
        setTitle(R.string.action_settings)
        applyToolbar(toolbar)
        val navController: NavController = findNavController(R.id.contentFrame)
        navController.addOnDestinationChangedListener { _, _, _ ->
            toolbar.title = navController.currentDestination?.let { getStringFromDestination(it) }
        }
    }

    private fun getStringFromDestination(currentDestination: NavDestination): String {
        val idRes = when (currentDestination.id) {
            R.id.mainSettingsFragment -> R.string.action_settings
            R.id.audioSettings -> R.string.pref_header_audio
            R.id.imageSettingFragment -> R.string.pref_header_images
            R.id.notificationSettingsFragment -> R.string.notification
            R.id.nowPlayingSettingsFragment -> R.string.now_playing
            R.id.otherSettingsFragment -> R.string.others
            R.id.personalizeSettingsFragment -> R.string.personalize
            R.id.themeSettingsFragment -> R.string.general_settings_title
            R.id.aboutActivity -> R.string.action_about
            else -> R.id.action_settings
        }
        return getString(idRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.contentFrame).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        when (dialog.title) {
            R.string.accent_color -> {
                ThemeStore.editTheme(this).accentColor(selectedColor).commit()
                if (VersionUtils.hasNougatMR())
                    DynamicShortcutManager(this).updateDynamicShortcuts()
            }
        }
        recreate()
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
