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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HistoryStore extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "history.db";
  private static final int MAX_ITEMS_IN_DB = 100;
  private static final int VERSION = 1;
  @Nullable private static HistoryStore sInstance = null;

  public HistoryStore(final Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  @NonNull
  public static synchronized HistoryStore getInstance(@NonNull final Context context) {
    if (sInstance == null) {
      sInstance = new HistoryStore(context.getApplicationContext());
    }
    return sInstance;
  }

  @Override
  public void onCreate(@NonNull final SQLiteDatabase db) {
    db.execSQL(
        "CREATE TABLE IF NOT EXISTS "
            + RecentStoreColumns.NAME
            + " ("
            + RecentStoreColumns.ID
            + " LONG NOT NULL,"
            + RecentStoreColumns.TIME_PLAYED
            + " LONG NOT NULL);");
  }

  @Override
  public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + RecentStoreColumns.NAME);
    onCreate(db);
  }

  @Override
  public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + RecentStoreColumns.NAME);
    onCreate(db);
  }

  public void addSongId(final long songId) {
    if (songId == -1) {
      return;
    }

    final SQLiteDatabase database = getWritableDatabase();
    database.beginTransaction();

    try {
      // remove previous entries
      removeSongId(songId);

      // add the entry
      final ContentValues values = new ContentValues(2);
      values.put(RecentStoreColumns.ID, songId);
      values.put(RecentStoreColumns.TIME_PLAYED, System.currentTimeMillis());
      database.insert(RecentStoreColumns.NAME, null, values);

      // if our db is too large, delete the extra items
      try (Cursor oldest = database.query(
              RecentStoreColumns.NAME,
              new String[]{RecentStoreColumns.TIME_PLAYED},
              null,
              null,
              null,
              null,
              RecentStoreColumns.TIME_PLAYED + " ASC")) {

        if (oldest != null && oldest.getCount() > MAX_ITEMS_IN_DB) {
          oldest.moveToPosition(oldest.getCount() - MAX_ITEMS_IN_DB);
          long timeOfRecordToKeep = oldest.getLong(0);

          database.delete(
                  RecentStoreColumns.NAME,
                  RecentStoreColumns.TIME_PLAYED + " < ?",
                  new String[]{String.valueOf(timeOfRecordToKeep)});
        }
      }
    } finally {
      database.setTransactionSuccessful();
      database.endTransaction();
    }
  }

  public void removeSongId(final long songId) {
    final SQLiteDatabase database = getWritableDatabase();
    database.delete(
        RecentStoreColumns.NAME,
        RecentStoreColumns.ID + " = ?",
        new String[] {String.valueOf(songId)});
  }

  public void clear() {
    final SQLiteDatabase database = getWritableDatabase();
    database.delete(RecentStoreColumns.NAME, null, null);
  }

  public boolean contains(long id) {
    final SQLiteDatabase database = getReadableDatabase();
    Cursor cursor =
        database.query(
            RecentStoreColumns.NAME,
            new String[] {RecentStoreColumns.ID},
            RecentStoreColumns.ID + "=?",
            new String[] {String.valueOf(id)},
            null,
            null,
            null,
            null);

    boolean containsId = cursor != null && cursor.moveToFirst();
    if (cursor != null) {
      cursor.close();
    }
    return containsId;
  }

  public Cursor queryRecentIds() {
    final SQLiteDatabase database = getReadableDatabase();
    return database.query(
        RecentStoreColumns.NAME,
        new String[] {RecentStoreColumns.ID},
        null,
        null,
        null,
        null,
        RecentStoreColumns.TIME_PLAYED + " DESC");
  }

  public Cursor queryRecentIds(long cutoff) {
    final boolean noCutoffTime = (cutoff == 0);
    final boolean reverseOrder = (cutoff < 0);
    if (reverseOrder) cutoff = -cutoff;

    final SQLiteDatabase database = getReadableDatabase();

    return database.query(
        RecentStoreColumns.NAME,
        new String[] {RecentStoreColumns.ID},
        noCutoffTime ? null : RecentStoreColumns.TIME_PLAYED + (reverseOrder ? "<?" : ">?"),
        noCutoffTime ? null : new String[] {String.valueOf(cutoff)},
        null,
        null,
        RecentStoreColumns.TIME_PLAYED + (reverseOrder ? " ASC" : " DESC"));
  }

  public interface RecentStoreColumns {
    String NAME = "recent_history";

    String ID = "song_id";

    String TIME_PLAYED = "time_played";
  }
}
