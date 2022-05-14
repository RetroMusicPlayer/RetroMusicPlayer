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
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialDialogsUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.LanguageContextWrapper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.util.theme.ThemeManager
import java.util.*

abstract class AbsThemeActivity : ATHToolbarActivity(), Runnable {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        hideStatusBar()
        super.onCreate(savedInstanceState)
        setImmersiveFullscreen()
        registerSystemUiVisibility()
        toggleScreenOn()
        MaterialDialogsUtil.updateMaterialDialogsThemeSingleton(this)
    }

    private fun updateTheme() {
        setTheme(ThemeManager.getThemeResValue(this))
        setDefaultNightMode(ThemeManager.getNightMode(this))
    }

    private fun toggleScreenOn() {
        if (PreferenceUtil.isScreenOnEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

    fun hideStatusBar() {
        hideStatusBar(PreferenceUtil.isFullScreenMode)
    }

    private fun hideStatusBar(fullscreen: Boolean) {
        val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
        if (statusBar != null) {
            statusBar.visibility = if (fullscreen) View.GONE else View.VISIBLE
        }
    }

    fun setDrawUnderStatusBar() {
        RetroUtil.setAllowDrawUnderStatusBar(window)
    }

    fun setDrawUnderNavigationBar() {
        RetroUtil.setAllowDrawUnderNavigationBar(window)
    }

    /**
     * This will set the color of the view with the id "status_bar" on KitKat and Lollipop. On
     * Lollipop if no such view is found it will set the statusbar color using the native method.
     *
     * @param color the new statusbar color (will be shifted down on Lollipop and above)
     */
    fun setStatusbarColor(color: Int) {
        val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
        if (statusBar != null) {
            when {
                VersionUtils.hasMarshmallow() -> statusBar.setBackgroundColor(color)
                VersionUtils.hasLollipop() -> statusBar.setBackgroundColor(
                    ColorUtil.darkenColor(
                        color
                    )
                )
                else -> statusBar.setBackgroundColor(color)
            }
        } else {
            when {
                VersionUtils.hasMarshmallow() -> window.statusBarColor = color
                else -> window.statusBarColor = ColorUtil.darkenColor(color)
            }
        }
        setLightStatusbarAuto(ATHUtil.resolveColor(this, R.attr.colorSurface))
    }

    fun setStatusbarColorAuto() {
        // we don't want to use statusbar color because we are doing the color darkening on our own to support KitKat
        setStatusbarColor(ATHUtil.resolveColor(this, R.attr.colorSurface))
        setLightStatusbarAuto(ATHUtil.resolveColor(this, R.attr.colorSurface))
    }

    open fun setTaskDescriptionColor(@ColorInt color: Int) {
        ATH.setTaskDescriptionColor(this, color)
    }

    fun setTaskDescriptionColorAuto() {
        setTaskDescriptionColor(ATHUtil.resolveColor(this, R.attr.colorSurface))
    }

    open fun setNavigationbarColor(color: Int) {
        if (ThemeStore.coloredNavigationBar(this)) {
            ATH.setNavigationbarColor(this, color)
        } else {
            ATH.setNavigationbarColor(this, Color.BLACK)
        }
    }

    fun setNavigationbarColorAuto() {
        setNavigationbarColor(ATHUtil.resolveColor(this, R.attr.colorSurface))
    }

    open fun setLightStatusbar(enabled: Boolean) {
        ATH.setLightStatusbar(this, enabled)
    }

    fun setLightStatusbarAuto(bgColor: Int) {
        setLightStatusbar(ColorUtil.isColorLight(bgColor))
    }

    open fun setLightNavigationBar(enabled: Boolean) {
        if (!ATHUtil.isWindowBackgroundDark(this) and ThemeStore.coloredNavigationBar(this)) {
            ATH.setLightNavigationbar(this, enabled)
        }
    }

    private fun registerSystemUiVisibility() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                setImmersiveFullscreen()
            }
        }
    }

    private fun unregisterSystemUiVisibility() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener(null)
    }

    private fun setImmersiveFullscreen() {
        val flags =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        if (PreferenceUtil.isFullScreenMode) {
            window.decorView.systemUiVisibility = flags
        }
    }

    private fun exitFullscreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
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
        unregisterSystemUiVisibility()
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
        if (code != "auto") {
            super.attachBaseContext(LanguageContextWrapper.wrap(newBase, Locale(code)))
        } else super.attachBaseContext(newBase)
    }
}
