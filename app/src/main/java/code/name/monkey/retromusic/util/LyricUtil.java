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

import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Created by hefuyi on 2016/11/8. */
public class LyricUtil {

  private static final String lrcRootPath =
      android.os.Environment.getExternalStorageDirectory().toString() + "/RetroMusic/lyrics/";
  private static final String TAG = "LyricUtil";

  @Nullable
  public static File writeLrcToLoc(
      @NonNull String title, @NonNull String artist, @NonNull String lrcContext) {
    FileWriter writer = null;
    try {
      File file = new File(getLrcPath(title, artist));
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      writer = new FileWriter(getLrcPath(title, artist));
      writer.write(lrcContext);
      return file;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        if (writer != null) writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean deleteLrcFile(@NonNull String title, @NonNull String artist) {
    File file = new File(getLrcPath(title, artist));
    return file.delete();
  }

  public static boolean isLrcFileExist(@NonNull String title, @NonNull String artist) {
    File file = new File(getLrcPath(title, artist));
    return file.exists();
  }

  public static boolean isLrcOriginalFileExist(@NonNull String path) {
    File file = new File(getLrcOriginalPath(path));
    return file.exists();
  }

  @Nullable
  public static File getLocalLyricFile(@NonNull String title, @NonNull String artist) {
    File file = new File(getLrcPath(title, artist));
    if (file.exists()) {
      return file;
    } else {
      return null;
    }
  }

  @Nullable
  public static File getLocalLyricOriginalFile(@NonNull String path) {
    File file = new File(getLrcOriginalPath(path));
    if (file.exists()) {
      return file;
    } else {
      return null;
    }
  }

  private static String getLrcPath(String title, String artist) {
    return lrcRootPath + title + " - " + artist + ".lrc";
  }

  private static String getLrcOriginalPath(String filePath) {
    return filePath.replace(filePath.substring(filePath.lastIndexOf(".") + 1), "lrc");
  }

  @NonNull
  public static String decryptBASE64(@NonNull String str) {
    if (str == null || str.length() == 0) {
      return null;
    }
    byte[] encode = str.getBytes(StandardCharsets.UTF_8);
    // base64 解密
    return new String(
        Base64.decode(encode, 0, encode.length, Base64.DEFAULT), StandardCharsets.UTF_8);
  }

  @NonNull
  public static String getStringFromFile(@NonNull String title, @NonNull String artist)
      throws Exception {
    File file = new File(getLrcPath(title, artist));
    FileInputStream fin = new FileInputStream(file);
    String ret = convertStreamToString(fin);
    fin.close();
    return ret;
  }

  private static String convertStreamToString(InputStream is) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    return sb.toString();
  }
}
