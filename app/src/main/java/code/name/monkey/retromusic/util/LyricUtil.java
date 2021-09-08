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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import code.name.monkey.retromusic.model.Song;

/**
 * Created by hefuyi on 2016/11/8.
 */
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

    //So in Retro, Lrc file can be same folder as Music File or in RetroMusic Folder
    // In this case we pass location of the file and Contents to write to file
    public static void writeLrc(@NonNull Song song, @NonNull String lrcContext) {
        FileWriter writer = null;
        File location;
        try {
                if (isLrcOriginalFileExist(song.getData())) {
                    location = getLocalLyricOriginalFile(song.getData());
                } else if (isLrcFileExist(song.getTitle(), song.getArtistName())) {
                    location = getLocalLyricFile(song.getTitle(), song.getArtistName());
                } else {
                    location = new File(getLrcPath(song.getTitle(), song.getArtistName()));
                    if (!location.getParentFile().exists()) {
                        location.getParentFile().mkdirs();
                    }
                }
                writer = new FileWriter(location);
                writer.write(lrcContext);

        } catch (IOException e) {
            e.printStackTrace();
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
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromLrc(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            Log.i("Error", "Error Occurred");
        }
        return "";
    }

}
