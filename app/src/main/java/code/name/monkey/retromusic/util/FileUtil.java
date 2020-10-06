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

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.repository.RealSongRepository;
import code.name.monkey.retromusic.repository.SortedCursor;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class FileUtil {

  private FileUtil() {}

  public static byte[] readBytes(InputStream stream) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int count;
    while ((count = stream.read(buffer)) != -1) {
      baos.write(buffer, 0, count);
    }
    stream.close();
    return baos.toByteArray();
  }

  @NonNull
  public static List<Song> matchFilesWithMediaStore(
      @NonNull Context context, @Nullable List<File> files) {
    return new RealSongRepository(context).songs(makeSongCursor(context, files));
  }

  public static String safeGetCanonicalPath(File file) {
    try {
      return file.getCanonicalPath();
    } catch (IOException e) {
      e.printStackTrace();
      return file.getAbsolutePath();
    }
  }

  @Nullable
  public static SortedCursor makeSongCursor(
      @NonNull final Context context, @Nullable final List<File> files) {
    String selection = null;
    String[] paths = null;

    if (files != null) {
      paths = toPathArray(files);

      if (files.size() > 0
          && files.size() < 999) { // 999 is the max amount Androids SQL implementation can handle.
        selection =
            MediaStore.Audio.AudioColumns.DATA + " IN (" + makePlaceholders(files.size()) + ")";
      }
    }

    Cursor songCursor =
        new RealSongRepository(context).makeSongCursor(selection, selection == null ? null : paths);

    return songCursor == null
        ? null
        : new SortedCursor(songCursor, paths, MediaStore.Audio.AudioColumns.DATA);
  }

  private static String makePlaceholders(int len) {
    StringBuilder sb = new StringBuilder(len * 2 - 1);
    sb.append("?");
    for (int i = 1; i < len; i++) {
      sb.append(",?");
    }
    return sb.toString();
  }

  @Nullable
  private static String[] toPathArray(@Nullable List<File> files) {
    if (files != null) {
      String[] paths = new String[files.size()];
      for (int i = 0; i < files.size(); i++) {
        /*try {
            paths[i] = files.get(i).getCanonicalPath(); // canonical path is important here because we want to compare the path with the media store entry later
        } catch (IOException e) {
            e.printStackTrace();
            paths[i] = files.get(i).getPath();
        }*/
        paths[i] = safeGetCanonicalPath(files.get(i));
      }
      return paths;
    }
    return null;
  }

  @NonNull
  public static List<File> listFiles(@NonNull File directory, @Nullable FileFilter fileFilter) {
    List<File> fileList = new LinkedList<>();
    File[] found = directory.listFiles(fileFilter);
    if (found != null) {
      Collections.addAll(fileList, found);
    }
    return fileList;
  }

  @NonNull
  public static List<File> listFilesDeep(@NonNull File directory, @Nullable FileFilter fileFilter) {
    List<File> files = new LinkedList<>();
    internalListFilesDeep(files, directory, fileFilter);
    return files;
  }

  @NonNull
  public static List<File> listFilesDeep(
      @NonNull Collection<File> files, @Nullable FileFilter fileFilter) {
    List<File> resFiles = new LinkedList<>();
    for (File file : files) {
      if (file.isDirectory()) {
        internalListFilesDeep(resFiles, file, fileFilter);
      } else if (fileFilter == null || fileFilter.accept(file)) {
        resFiles.add(file);
      }
    }
    return resFiles;
  }

  private static void internalListFilesDeep(
      @NonNull Collection<File> files, @NonNull File directory, @Nullable FileFilter fileFilter) {
    File[] found = directory.listFiles(fileFilter);

    if (found != null) {
      for (File file : found) {
        if (file.isDirectory()) {
          internalListFilesDeep(files, file, fileFilter);
        } else {
          files.add(file);
        }
      }
    }
  }

  public static boolean fileIsMimeType(File file, String mimeType, MimeTypeMap mimeTypeMap) {
    if (mimeType == null || mimeType.equals("*/*")) {
      return true;
    } else {
      // get the file mime type
      String filename = file.toURI().toString();
      int dotPos = filename.lastIndexOf('.');
      if (dotPos == -1) {
        return false;
      }
      String fileExtension = filename.substring(dotPos + 1).toLowerCase();
      String fileType = mimeTypeMap.getMimeTypeFromExtension(fileExtension);
      if (fileType == null) {
        return false;
      }
      // check the 'type/subtype' pattern
      if (fileType.equals(mimeType)) {
        return true;
      }
      // check the 'type/*' pattern
      int mimeTypeDelimiter = mimeType.lastIndexOf('/');
      if (mimeTypeDelimiter == -1) {
        return false;
      }
      String mimeTypeMainType = mimeType.substring(0, mimeTypeDelimiter);
      String mimeTypeSubtype = mimeType.substring(mimeTypeDelimiter + 1);
      if (!mimeTypeSubtype.equals("*")) {
        return false;
      }
      int fileTypeDelimiter = fileType.lastIndexOf('/');
      if (fileTypeDelimiter == -1) {
        return false;
      }
      String fileTypeMainType = fileType.substring(0, fileTypeDelimiter);
      if (fileTypeMainType.equals(mimeTypeMainType)) {
        return true;
      }
      return fileTypeMainType.equals(mimeTypeMainType);
    }
  }

  public static String stripExtension(String str) {
    if (str == null) {
      return null;
    }
    int pos = str.lastIndexOf('.');
    if (pos == -1) {
      return str;
    }
    return str.substring(0, pos);
  }

  public static String readFromStream(InputStream is) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      if (sb.length() > 0) {
        sb.append("\n");
      }
      sb.append(line);
    }
    reader.close();
    return sb.toString();
  }

  public static String read(File file) throws Exception {
    FileInputStream fin = new FileInputStream(file);
    String ret = readFromStream(fin);
    fin.close();
    return ret;
  }

  public static boolean isExternalMemoryAvailable() {
    Boolean isSDPresent =
        Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

    // yes SD-card is present
    // Sorry
    return isSDSupportedDevice && isSDPresent;
  }

  public static File safeGetCanonicalFile(File file) {
    try {
      return file.getCanonicalFile();
    } catch (IOException e) {
      e.printStackTrace();
      return file.getAbsoluteFile();
    }
  }
}
