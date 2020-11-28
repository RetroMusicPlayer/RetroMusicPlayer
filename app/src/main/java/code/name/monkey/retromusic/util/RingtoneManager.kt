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

package code.name.monkey.retromusic.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil.getSongFileUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RingtoneManager(val context: Context) {
    fun setRingtone(song: Song) {
        val resolver = context.contentResolver
        val uri = getSongFileUri(song.id)
        try {
            val values = ContentValues(2)
            values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, "1")
            values.put(MediaStore.Audio.AudioColumns.IS_ALARM, "1")
            resolver.update(uri, values, null, null)
        } catch (ignored: UnsupportedOperationException) {
            return
        }

        try {
            val cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.MediaColumns.TITLE),
                BaseColumns._ID + "=?",
                arrayOf(song.id.toString()), null
            )
            cursor.use { cursorSong ->
                if (cursorSong != null && cursorSong.count == 1) {
                    cursorSong.moveToFirst()
                    Settings.System.putString(resolver, Settings.System.RINGTONE, uri.toString())
                    val message = context
                        .getString(R.string.x_has_been_set_as_ringtone, cursorSong.getString(0))
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (ignored: SecurityException) {
        }
    }

    companion object {

        fun requiresDialog(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(context)) {
                    return true
                }
            }
            return false
        }

        fun getDialog(context: Context) {
            return MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogTheme)
                .setTitle(R.string.dialog_title_set_ringtone)
                .setMessage(R.string.dialog_message_set_ringtone)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = Uri.parse("package:" + context.applicationContext.packageName)
                    context.startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create().show()
        }
    }
}