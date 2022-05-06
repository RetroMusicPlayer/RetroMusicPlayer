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

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.bumptech.glide.signature.ObjectKey

/** @author Karim Abou Zeid (kabouzeid)
 */
class ArtistSignatureUtil private constructor(context: Context) {
    private val mPreferences: SharedPreferences =
        context.getSharedPreferences(ARTIST_SIGNATURE_PREFS, Context.MODE_PRIVATE)

    fun updateArtistSignature(artistName: String?) {
        mPreferences.edit { putLong(artistName, System.currentTimeMillis()) }
    }

    private fun getArtistSignatureRaw(artistName: String?): Long {
        return mPreferences.getLong(artistName, 0)
    }

    fun getArtistSignature(artistName: String?): ObjectKey {
        return ObjectKey(getArtistSignatureRaw(artistName).toString())
    }

    companion object {
        private const val ARTIST_SIGNATURE_PREFS = "artist_signatures"
        private var INSTANCE: ArtistSignatureUtil? = null
        fun getInstance(context: Context): ArtistSignatureUtil {
            if (INSTANCE == null) {
                INSTANCE = ArtistSignatureUtil(context.applicationContext)
            }
            return INSTANCE!!
        }
    }

}