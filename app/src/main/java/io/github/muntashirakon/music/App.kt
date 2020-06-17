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

package io.github.muntashirakon.music

import androidx.multidex.MultiDexApplication
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import io.github.muntashirakon.music.appshortcuts.DynamicShortcutManager
import io.github.muntashirakon.music.dagger.DaggerMusicComponent
import io.github.muntashirakon.music.dagger.MusicComponent
import io.github.muntashirakon.music.dagger.module.AppModule

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        musicComponent = initDagger(this)

        // default theme
        if (!ThemeStore.isConfigured(this, 3)) {
            ThemeStore.editTheme(this)
                .accentColorRes(R.color.md_deep_purple_A200)
                .coloredNavigationBar(true)
                .commit()
        }

        if (VersionUtils.hasNougatMR())
            DynamicShortcutManager(this).initDynamicShortcuts()
    }

    private fun initDagger(app: App): MusicComponent =
        DaggerMusicComponent.builder()
            .appModule(AppModule(app))
            .build()

    companion object {
        private var instance: App? = null

        fun getContext(): App {
            return instance!!
        }

        lateinit var musicComponent: MusicComponent
    }
}
