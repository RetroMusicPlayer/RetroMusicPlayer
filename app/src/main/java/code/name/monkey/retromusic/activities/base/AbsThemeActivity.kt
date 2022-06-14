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
package code.name.monkey.retromusic.activities.base

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.ConfigurationCompat
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.LanguageContextWrapper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.maybeShowAnnoyingToasts
import code.name.monkey.retromusic.util.theme.getNightMode
import code.name.monkey.retromusic.util.theme.getThemeResValue
import java.util.*

abstract class AbsThemeActivity : ATHToolbarActivity(), Runnable {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        hideStatusBar()
        super.onCreate(savedInstanceState)
        setEdgeToEdgeOrImmersive()
        maybeSetScreenOn()
        setLightNavigationBarAuto()
        setLightStatusBarAuto(surfaceColor())
        if (VersionUtils.hasQ()) {
            window.decorView.isForceDarkAllowed = false
        }
        maybeShowAnnoyingToasts()
    }

    private fun updateTheme() {
        setTheme(getThemeResValue())
        if (PreferenceUtil.materialYou) {
            setDefaultNightMode(getNightMode())
        }

        if (PreferenceUtil.isCustomFont) {
            setTheme(R.style.FontThemeOverlay)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideStatusBar()
            handler.removeCallbacks(this)
            handler.postDelayed(this, 300)
        } else {
            handler.removeCallbacks(this)
        }
    }

    override fun run() {
        setImmersiveFullscreen()
    }

    override fun onStop() {
        handler.removeCallbacks(this)
        super.onStop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        exitFullscreen()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            handler.removeCallbacks(this)
            handler.postDelayed(this, 500)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun attachBaseContext(newBase: Context?) {
        val code = PreferenceUtil.languageCode
        val locale = if (code == "auto") {
            // Get the device default locale
            ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]
        } else {
            Locale.forLanguageTag(code)
        }
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, locale))
        installSplitCompat()
    }
}
