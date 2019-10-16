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

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.widget.Toast
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.model.Artist
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class CustomArtistImageUtil private constructor(context: Context) {

    private val mPreferences: SharedPreferences

    init {
        mPreferences = context.applicationContext.getSharedPreferences(CUSTOM_ARTIST_IMAGE_PREFS, Context.MODE_PRIVATE)
    }

    fun setCustomArtistImage(artist: Artist, uri: Uri) {
        Glide.with(App.getContext())
                .load(uri)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        e!!.printStackTrace()
                        Toast.makeText(App.getContext(), e.toString(), Toast.LENGTH_LONG).show()
                    }

                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        object : AsyncTask<Void, Void, Void>() {
                            @SuppressLint("ApplySharedPref")
                            override fun doInBackground(vararg params: Void): Void? {
                                val dir = File(App.getContext().filesDir, FOLDER_NAME)
                                if (!dir.exists()) {
                                    if (!dir.mkdirs()) { // create the folder
                                        return null
                                    }
                                }
                                val file = File(dir, getFileName(artist))

                                var succesful = false
                                try {
                                    val os = BufferedOutputStream(FileOutputStream(file))
                                    succesful = ImageUtil.resizeBitmap(resource, 2048).compress(Bitmap.CompressFormat.JPEG, 100, os)
                                    os.close()
                                } catch (e: IOException) {
                                    Toast.makeText(App.getContext(), e.toString(), Toast.LENGTH_LONG).show()
                                }

                                if (succesful) {
                                    mPreferences.edit().putBoolean(getFileName(artist), true).commit()
                                    ArtistSignatureUtil.getInstance(App.getContext()).updateArtistSignature(artist.name)
                                    App.getContext().getContentResolver().notifyChange(Uri.parse("content://media"), null) // trigger media store changed to force artist image reload
                                }
                                return null
                            }
                        }.execute()
                    }
                })
    }

    fun resetCustomArtistImage(artist: Artist) {
        object : AsyncTask<Void, Void, Void>() {
            @SuppressLint("ApplySharedPref")
            override fun doInBackground(vararg params: Void): Void? {
                mPreferences.edit().putBoolean(getFileName(artist), false).commit()
                ArtistSignatureUtil.getInstance(App.getContext()).updateArtistSignature(artist.name)
                App.getContext().contentResolver.notifyChange(Uri.parse("content://media"), null) // trigger media store changed to force artist image reload

                val file = getFile(artist)
                if (!file.exists()) {
                    return null
                } else {
                    file.delete()
                }
                return null
            }
        }.execute()
    }

    // shared prefs saves us many IO operations
    fun hasCustomArtistImage(artist: Artist): Boolean {
        return mPreferences.getBoolean(getFileName(artist), false)
    }

    companion object {
        private const val CUSTOM_ARTIST_IMAGE_PREFS = "custom_artist_image"
        private const val FOLDER_NAME = "/custom_artist_images/"

        private var sInstance: CustomArtistImageUtil? = null

        fun getInstance(context: Context): CustomArtistImageUtil {
            if (sInstance == null) {
                sInstance = CustomArtistImageUtil(context.applicationContext)
            }
            return sInstance!!
        }

        fun getFileName(artist: Artist): String {
            var artistName = artist.name
            // replace everything that is not a letter or a number with _
            artistName = artistName.replace("[^a-zA-Z0-9]".toRegex(), "_")
            return String.format(Locale.US, "#%d#%s.jpeg", artist.id, artistName)
        }

        @JvmStatic
        fun getFile(artist: Artist): File {
            val dir = File(App.getContext().filesDir, FOLDER_NAME)
            return File(dir, getFileName(artist))
        }
    }
}