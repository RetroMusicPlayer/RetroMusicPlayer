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

package code.name.monkey.retromusic.providers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import code.name.monkey.retromusic.util.FileUtil;
import code.name.monkey.retromusic.util.PreferenceUtilKT;

import static code.name.monkey.retromusic.service.MusicService.MEDIA_STORE_CHANGED;

public class BlacklistStore extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "blacklist.db";
    private static final int VERSION = 2;
    private static BlacklistStore sInstance = null;
    private Context context;

    public BlacklistStore(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @NonNull
    public static synchronized BlacklistStore getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new BlacklistStore(context.getApplicationContext());
            if (!PreferenceUtilKT.INSTANCE.isInitializedBlacklist()) {
                // blacklisted by default
                sInstance.addPathImpl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
                sInstance.addPathImpl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
                sInstance.addPathImpl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));

                PreferenceUtilKT.INSTANCE.setInitializedBlacklist(true);
            }
        }
        return sInstance;
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BlacklistStoreColumns.NAME + " (" + BlacklistStoreColumns.PATH + " STRING NOT NULL);");
    }

    @Override
    public void onUpgrade(@NonNull final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BlacklistStoreColumns.NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BlacklistStoreColumns.NAME);
        onCreate(db);
    }

    public void addPath(File file) {
        addPathImpl(file);
        notifyMediaStoreChanged();
    }

    private void addPathImpl(File file) {
        if (file == null || contains(file)) {
            return;
        }
        String path = FileUtil.safeGetCanonicalPath(file);

        final SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            // add the entry
            final ContentValues values = new ContentValues(1);
            values.put(BlacklistStoreColumns.PATH, path);
            database.insert(BlacklistStoreColumns.NAME, null, values);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public boolean contains(File file) {
        if (file == null) {
            return false;
        }
        String path = FileUtil.safeGetCanonicalPath(file);

        final SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(BlacklistStoreColumns.NAME,
                new String[]{BlacklistStoreColumns.PATH},
                BlacklistStoreColumns.PATH + "=?",
                new String[]{path},
                null, null, null, null);

        boolean containsPath = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return containsPath;
    }

    public void removePath(File file) {
        final SQLiteDatabase database = getWritableDatabase();
        String path = FileUtil.safeGetCanonicalPath(file);

        database.delete(BlacklistStoreColumns.NAME,
                BlacklistStoreColumns.PATH + "=?",
                new String[]{path});

        notifyMediaStoreChanged();
    }

    public void clear() {
        final SQLiteDatabase database = getWritableDatabase();
        database.delete(BlacklistStoreColumns.NAME, null, null);

        notifyMediaStoreChanged();
    }

    private void notifyMediaStoreChanged() {
        context.sendBroadcast(new Intent(MEDIA_STORE_CHANGED));
    }

    @NonNull
    public ArrayList<String> getPaths() {
        Cursor cursor = getReadableDatabase().query(BlacklistStoreColumns.NAME,
                new String[]{BlacklistStoreColumns.PATH},
                null, null, null, null, null);

        ArrayList<String> paths = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                paths.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        if (cursor != null)
            cursor.close();
        return paths;
    }

    public interface BlacklistStoreColumns {
        String NAME = "blacklist";

        String PATH = "path";
    }
}