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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Song;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.Utils;

public class SAFUtil {

  public static final String TAG = SAFUtil.class.getSimpleName();
  public static final String SEPARATOR = "###/SAF/###";

  public static final int REQUEST_SAF_PICK_FILE = 42;
  public static final int REQUEST_SAF_PICK_TREE = 43;

  public static boolean isSAFRequired(File file) {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !file.canWrite();
  }

  public static boolean isSAFRequired(String path) {
    return isSAFRequired(new File(path));
  }

  public static boolean isSAFRequired(AudioFile audio) {
    return isSAFRequired(audio.getFile());
  }

  public static boolean isSAFRequired(Song song) {
    return isSAFRequired(song.getData());
  }

  public static boolean isSAFRequired(List<String> paths) {
    for (String path : paths) {
      if (isSAFRequired(path)) return true;
    }
    return false;
  }

  public static boolean isSAFRequiredForSongs(List<Song> songs) {
    for (Song song : songs) {
      if (isSAFRequired(song)) return true;
    }
    return false;
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static void openFilePicker(Activity activity) {
    Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
    i.addCategory(Intent.CATEGORY_OPENABLE);
    i.setType("audio/*");
    i.putExtra("android.content.extra.SHOW_ADVANCED", true);
    activity.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_FILE);
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static void openFilePicker(Fragment fragment) {
    Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
    i.addCategory(Intent.CATEGORY_OPENABLE);
    i.setType("audio/*");
    i.putExtra("android.content.extra.SHOW_ADVANCED", true);
    fragment.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_FILE);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void openTreePicker(Activity activity) {
    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    i.putExtra("android.content.extra.SHOW_ADVANCED", true);
    activity.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_TREE);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void openTreePicker(Fragment fragment) {
    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    i.putExtra("android.content.extra.SHOW_ADVANCED", true);
    fragment.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_TREE);
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static void saveTreeUri(Context context, Intent data) {
    Uri uri = data.getData();
    context
        .getContentResolver()
        .takePersistableUriPermission(
            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    PreferenceUtil.INSTANCE.setSafSdCardUri(uri.toString());
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static boolean isTreeUriSaved(Context context) {
    return !TextUtils.isEmpty(PreferenceUtil.INSTANCE.getSafSdCardUri());
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static boolean isSDCardAccessGranted(Context context) {
    if (!isTreeUriSaved(context)) return false;

    String sdcardUri = PreferenceUtil.INSTANCE.getSafSdCardUri();

    List<UriPermission> perms = context.getContentResolver().getPersistedUriPermissions();
    for (UriPermission perm : perms) {
      if (perm.getUri().toString().equals(sdcardUri) && perm.isWritePermission()) return true;
    }

    return false;
  }

  /**
   * https://github.com/vanilla-music/vanilla-music-tag-editor/commit/e00e87fef289f463b6682674aa54be834179ccf0#diff-d436417358d5dfbb06846746d43c47a5R359
   * Finds needed file through Document API for SAF. It's not optimized yet - you can still gain
   * wrong URI on files such as "/a/b/c.mp3" and "/b/a/c.mp3", but I consider it complete enough to
   * be usable.
   *
   * @param dir - document file representing current dir of search
   * @param segments - path segments that are left to find
   * @return URI for found file. Null if nothing found.
   */
  @Nullable
  public static Uri findDocument(DocumentFile dir, List<String> segments) {
    for (DocumentFile file : dir.listFiles()) {
      int index = segments.indexOf(file.getName());
      if (index == -1) {
        continue;
      }

      if (file.isDirectory()) {
        segments.remove(file.getName());
        return findDocument(file, segments);
      }

      if (file.isFile() && index == segments.size() - 1) {
        // got to the last part
        return file.getUri();
      }
    }

    return null;
  }

  public static void write(Context context, AudioFile audio, Uri safUri) {
    if (isSAFRequired(audio)) {
      writeSAF(context, audio, safUri);
    } else {
      try {
        writeFile(audio);
      } catch (CannotWriteException e) {
        e.printStackTrace();
      }
    }
  }

  public static void writeFile(AudioFile audio) throws CannotWriteException {
    audio.commit();
  }

  public static void writeSAF(Context context, AudioFile audio, Uri safUri) {
    Uri uri = null;

    if (context == null) {
      Log.e(TAG, "writeSAF: context == null");
      return;
    }

    if (isTreeUriSaved(context)) {
      List<String> pathSegments =
          new ArrayList<>(Arrays.asList(audio.getFile().getAbsolutePath().split("/")));
      Uri sdcard = Uri.parse(PreferenceUtil.INSTANCE.getSafSdCardUri());
      uri = findDocument(DocumentFile.fromTreeUri(context, sdcard), pathSegments);
    }

    if (uri == null) {
      uri = safUri;
    }

    if (uri == null) {
      Log.e(TAG, "writeSAF: Can't get SAF URI");
      toast(context, context.getString(R.string.saf_error_uri));
      return;
    }

    try {
      // copy file to app folder to use jaudiotagger
      final File original = audio.getFile();
      File temp = File.createTempFile("tmp-media", '.' + Utils.getExtension(original));
      Utils.copy(original, temp);
      temp.deleteOnExit();
      audio.setFile(temp);
      writeFile(audio);

      ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "rw");
      if (pfd == null) {
        Log.e(TAG, "writeSAF: SAF provided incorrect URI: " + uri);
        return;
      }

      // now read persisted data and write it to real FD provided by SAF
      FileInputStream fis = new FileInputStream(temp);
      byte[] audioContent = FileUtil.readBytes(fis);
      FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
      fos.write(audioContent);
      fos.close();

      temp.delete();
    } catch (final Exception e) {
      Log.e(TAG, "writeSAF: Failed to write to file descriptor provided by SAF", e);

      toast(
          context,
          String.format(context.getString(R.string.saf_write_failed), e.getLocalizedMessage()));
    }
  }

  public static void delete(Context context, String path, Uri safUri) {
    if (isSAFRequired(path)) {
      deleteSAF(context, path, safUri);
    } else {
      try {
        deleteFile(path);
      } catch (NullPointerException e) {
        Log.e("MusicUtils", "Failed to find file " + path);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void deleteFile(String path) {
    new File(path).delete();
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static void deleteSAF(Context context, String path, Uri safUri) {
    Uri uri = null;

    if (context == null) {
      Log.e(TAG, "deleteSAF: context == null");
      return;
    }

    if (isTreeUriSaved(context)) {
      List<String> pathSegments = new ArrayList<>(Arrays.asList(path.split("/")));
      Uri sdcard = Uri.parse(PreferenceUtil.INSTANCE.getSafSdCardUri());
      uri = findDocument(DocumentFile.fromTreeUri(context, sdcard), pathSegments);
    }

    if (uri == null) {
      uri = safUri;
    }

    if (uri == null) {
      Log.e(TAG, "deleteSAF: Can't get SAF URI");
      toast(context, context.getString(R.string.saf_error_uri));
      return;
    }

    try {
      DocumentsContract.deleteDocument(context.getContentResolver(), uri);
    } catch (final Exception e) {
      Log.e(TAG, "deleteSAF: Failed to delete a file descriptor provided by SAF", e);

      toast(
          context,
          String.format(context.getString(R.string.saf_delete_failed), e.getLocalizedMessage()));
    }
  }

  private static void toast(final Context context, final String message) {
    if (context instanceof Activity) {
      ((Activity) context)
          .runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
  }
}
