/*
 * Copyright (c) 2020 Hemanth Savarala.
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

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song

/**
 * Created by hemanths on 2020-02-02.
 */

object Share {

    private const val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"

    fun shareFileToInstagram(context: Context, song: Song, uri: Uri) {
        val feedIntent = Intent(Intent.ACTION_SEND)
        feedIntent.type = "image/*"
        feedIntent.putExtra(Intent.EXTRA_TITLE, song.title)
        feedIntent.putExtra(Intent.EXTRA_TEXT, song.artistName)
        feedIntent.putExtra(Intent.EXTRA_STREAM, uri)
        feedIntent.setPackage(INSTAGRAM_PACKAGE_NAME)

        val storiesIntent = Intent("com.instagram.share.ADD_TO_STORY")
        storiesIntent.setDataAndType(uri, "jpg")
        storiesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        storiesIntent.setPackage(INSTAGRAM_PACKAGE_NAME)
        storiesIntent.putExtra(Intent.EXTRA_TITLE, song.title)
        storiesIntent.putExtra(Intent.EXTRA_TEXT, song.artistName)
        context.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooserIntent = Intent.createChooser(feedIntent, context.getString(R.string.social_instagram))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(storiesIntent))
        ActivityCompat.startActivity(context, chooserIntent, null)
    }
}