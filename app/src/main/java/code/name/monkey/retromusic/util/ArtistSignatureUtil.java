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

package code.name.monkey.retromusic.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.bumptech.glide.signature.StringSignature;

/** @author Karim Abou Zeid (kabouzeid) */
public class ArtistSignatureUtil {
  private static final String ARTIST_SIGNATURE_PREFS = "artist_signatures";

  private static ArtistSignatureUtil sInstance;

  private final SharedPreferences mPreferences;

  private ArtistSignatureUtil(@NonNull final Context context) {
    mPreferences = context.getSharedPreferences(ARTIST_SIGNATURE_PREFS, Context.MODE_PRIVATE);
  }

  public static ArtistSignatureUtil getInstance(@NonNull final Context context) {
    if (sInstance == null) {
      sInstance = new ArtistSignatureUtil(context.getApplicationContext());
    }
    return sInstance;
  }

  @SuppressLint("CommitPrefEdits")
  public void updateArtistSignature(String artistName) {
    mPreferences.edit().putLong(artistName, System.currentTimeMillis()).commit();
  }

  public long getArtistSignatureRaw(String artistName) {
    return mPreferences.getLong(artistName, 0);
  }

  public StringSignature getArtistSignature(String artistName) {
    return new StringSignature(String.valueOf(getArtistSignatureRaw(artistName)));
  }
}
