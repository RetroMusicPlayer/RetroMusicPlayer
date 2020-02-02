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

/**
 * Created by hemanths on 2020-02-02.
 */

object Share {

    private const val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"
    private const val MEDIA_TYPE_JPEG = "jpg"

    fun shareFileToInstagram(context: Context, uri: Uri) {
        val feedIntent = Intent(Intent.ACTION_SEND)
        feedIntent.type = "image/*"
        feedIntent.putExtra(Intent.EXTRA_STREAM, uri)
        feedIntent.setPackage(INSTAGRAM_PACKAGE_NAME)
        feedIntent.putExtra("top_background_color", "#33FF33")
        feedIntent.putExtra("bottom_background_color", "#FF00FF")

        val storiesIntent = Intent("com.instagram.share.ADD_TO_STORY")
        //storiesIntent.setDataAndType(uri, MEDIA_TYPE_JPEG)
        storiesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        storiesIntent.setPackage(INSTAGRAM_PACKAGE_NAME)
        storiesIntent.type = MEDIA_TYPE_JPEG
        storiesIntent.putExtra("top_background_color", "#33FF33")
        storiesIntent.putExtra("bottom_background_color", "#0000FF")
        storiesIntent.putExtra("content_url", "https://www.google.com")
        val chooserIntent = Intent.createChooser(feedIntent, context.getString(R.string.social_instagram))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(storiesIntent))
        ActivityCompat.startActivity(context, chooserIntent, null)
    }
}