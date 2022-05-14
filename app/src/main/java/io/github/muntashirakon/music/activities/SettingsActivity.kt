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
package io.github.muntashirakon.music.activities

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.activities.base.AbsBaseActivity
import io.github.muntashirakon.music.appshortcuts.DynamicShortcutManager
import io.github.muntashirakon.music.databinding.ActivitySettingsBinding
import io.github.muntashirakon.music.extensions.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorCallback

class SettingsActivity : AbsBaseActivity(), ColorCallback, OnThemeChangedListener {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val mSavedInstanceState = extra<Bundle>(TAG).value ?: savedInstanceState
        super.onCreate(mSavedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setPermissionDeniedMessage(getString(R.string.permission_bluetooth_denied))
    }

    override fun onResume() {
        super.onResume()
        setNavigationBarColorPreOreo(surfaceColor())
    }

    private fun setupToolbar() {
        applyToolbar(binding.toolbar)
        val navController: NavController = findNavController(R.id.contentFrame)
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.collapsingToolbarLayout.title =
                navController.currentDestination?.let { getStringFromDestination(it) }
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
            R.id.backup_fragment -> R.string.backup_restore_title
            else -> R.id.action_settings
        }
        return getString(idRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.contentFrame).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getPermissionsToRequest(): Array<String> {
        return if (VersionUtils.hasS()) {
            arrayOf(BLUETOOTH_CONNECT)
        } else {
            arrayOf()
        }
    }

    override fun invoke(dialog: MaterialDialog, color: Int) {
        ThemeStore.editTheme(this).accentColor(color).commit()
        if (VersionUtils.hasNougatMR())
            DynamicShortcutManager(this).updateDynamicShortcuts()
        restart()
    }

    override fun onThemeValuesChanged() {
        restart()
    }

    private fun restart() {
        val savedInstanceState = Bundle().apply {
            onSaveInstanceState(this)
        }
        finish()
        val intent = Intent(this, this::class.java).putExtra(TAG, savedInstanceState)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        val TAG: String = SettingsActivity::class.java.simpleName
    }
}

interface OnThemeChangedListener {
    fun onThemeValuesChanged()
}
