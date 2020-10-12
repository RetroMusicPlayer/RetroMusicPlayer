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
package code.name.monkey.retromusic.appshortcuts

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import code.name.monkey.retromusic.appshortcuts.shortcuttype.LastAddedShortcutType
import code.name.monkey.retromusic.appshortcuts.shortcuttype.ShuffleAllShortcutType
import code.name.monkey.retromusic.appshortcuts.shortcuttype.TopTracksShortcutType
import java.util.*

@TargetApi(Build.VERSION_CODES.N_MR1)
class DynamicShortcutManager(private val context: Context) {
    private val shortcutManager: ShortcutManager =
        this.context.getSystemService(ShortcutManager::class.java)

    private val defaultShortcuts: List<ShortcutInfo>
        get() = listOf(
            ShuffleAllShortcutType(context).shortcutInfo,
            TopTracksShortcutType(context).shortcutInfo,
            LastAddedShortcutType(context).shortcutInfo
        )

    fun initDynamicShortcuts() {
        // if (shortcutManager.dynamicShortcuts.size == 0) {
        shortcutManager.dynamicShortcuts = defaultShortcuts
        // }
    }

    fun updateDynamicShortcuts() {
        shortcutManager.updateShortcuts(defaultShortcuts)
    }

    companion object {

        fun createShortcut(
            context: Context,
            id: String,
            shortLabel: String,
            longLabel: String,
            icon: Icon,
            intent: Intent
        ): ShortcutInfo {
            return ShortcutInfo.Builder(context, id)
                .setShortLabel(shortLabel)
                .setLongLabel(longLabel)
                .setIcon(icon)
                .setIntent(intent)
                .build()
        }

        fun reportShortcutUsed(context: Context, shortcutId: String) {
            context.getSystemService(ShortcutManager::class.java).reportShortcutUsed(shortcutId)
        }
    }
}
