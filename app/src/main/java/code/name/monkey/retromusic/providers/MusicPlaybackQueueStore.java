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
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.repository.RealSongRepository;

/**
 * @author Andrew Neal, modified for Phonograph by Karim Abou Zeid
 *     <p>This keeps track of the music playback and history state of the playback service
 */
public class MusicPlaybackQueueStore extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "music_playback_state.db";

  public static final String PLAYING_QUEUE_TABLE_NAME = "playing_queue";

  public static final String ORIGINAL_PLAYING_QUEUE_TABLE_NAME = "original_playing_queue";

  private static final int VERSION = 12;

  @Nullable private static MusicPlaybackQueueStore sInstance = null;

  /**
   * Constructor of <code>MusicPlaybackState</code>
   *
   * @param context The {@link Context} to use
   */
  public MusicPlaybackQueueStore(final @NonNull Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  /**
   * @param context The {@link Context} to use
   * @return A new instance of this class.
   */
  @NonNull
  public static synchronized MusicPlaybackQueueStore getInstance(@NonNull final Context context) {
    if (sInstance == null) {
      sInstance = new MusicPlaybackQueueStore(context.getApplicationContext());
    }
    return sInstance;
  }

  @Override
  public void onCreate(@NonNull final SQLiteDatabase db) {
    createTable(db, PLAYING_QUEUE_TABLE_NAME);
    createTable(db, ORIGINAL_PLAYING_QUEUE_TABLE_NAME);
  }

  @NonNull
  public List<Song> getSavedOriginalPlayingQueue() {
    return getQueue(ORIGINAL_PLAYING_QUEUE_TABLE_NAME);
  }

  @NonNull
  public List<Song> getSavedPlayingQueue() {
    return getQueue(PLAYING_QUEUE_TABLE_NAME);
  }

  @Override
  public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    // If we ever have downgrade, drop the table to be safe
    db.execSQL("DROP TABLE IF EXISTS " + PLAYING_QUEUE_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + ORIGINAL_PLAYING_QUEUE_TABLE_NAME);
    onCreate(db);
  }

  @Override
  public void onUpgrade(
      @NonNull final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    // not necessary yet
    db.execSQL("DROP TABLE IF EXISTS " + PLAYING_QUEUE_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + ORIGINAL_PLAYING_QUEUE_TABLE_NAME);
    onCreate(db);
  }

  public synchronized void saveQueues(
      @NonNull final List<Song> playingQueue, @NonNull final List<Song> originalPlayingQueue) {
    saveQueue(PLAYING_QUEUE_TABLE_NAME, playingQueue);
    saveQueue(ORIGINAL_PLAYING_QUEUE_TABLE_NAME, originalPlayingQueue);
  }

  private void createTable(@NonNull final SQLiteDatabase db, final String tableName) {
    //noinspection StringBufferReplaceableByString
    StringBuilder builder = new StringBuilder();
    builder.append("CREATE TABLE IF NOT EXISTS ");
    builder.append(tableName);
    builder.append("(");

    builder.append(BaseColumns._ID);
    builder.append(" INT NOT NULL,");

    builder.append(AudioColumns.TITLE);
    builder.append(" STRING NOT NULL,");

    builder.append(AudioColumns.TRACK);
    builder.append(" INT NOT NULL,");

    builder.append(AudioColumns.YEAR);
    builder.append(" INT NOT NULL,");

    builder.append(AudioColumns.DURATION);
    builder.append(" LONG NOT NULL,");

    builder.append(Constants.DATA);
    builder.append(" STRING NOT NULL,");

    builder.append(AudioColumns.DATE_MODIFIED);
    builder.append(" LONG NOT NULL,");

    builder.append(AudioColumns.ALBUM_ID);
    builder.append(" INT NOT NULL,");

    builder.append(AudioColumns.ALBUM);
    builder.append(" STRING NOT NULL,");

    builder.append(AudioColumns.ARTIST_ID);
    builder.append(" INT NOT NULL,");

    builder.append(AudioColumns.ARTIST);
    builder.append(" STRING NOT NULL,");

    builder.append(AudioColumns.COMPOSER);
    builder.append(" STRING,");

    builder.append("album_artist");
    builder.append(" STRING);");

    db.execSQL(builder.toString());
  }

  @NonNull
  private List<Song> getQueue(@NonNull final String tableName) {
    Cursor cursor = getReadableDatabase().query(tableName, null, null, null, null, null, null);
    return new RealSongRepository(App.Companion.getContext()).songs(cursor);
  }

  /**
   * Clears the existing database and saves the queue into the db so that when the app is restarted,
   * the tracks you were listening to is restored
   *
   * @param queue the queue to save
   */
  private synchronized void saveQueue(final String tableName, @NonNull final List<Song> queue) {
    final SQLiteDatabase database = getWritableDatabase();
    database.beginTransaction();

    try {
      database.delete(tableName, null, null);
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    final int NUM_PROCESS = 20;
    int position = 0;
    while (position < queue.size()) {
      database.beginTransaction();
      try {
        for (int i = position; i < queue.size() && i < position + NUM_PROCESS; i++) {
          Song song = queue.get(i);
          ContentValues values = new ContentValues(4);

          values.put(BaseColumns._ID, song.getId());
          values.put(AudioColumns.TITLE, song.getTitle());
          values.put(AudioColumns.TRACK, song.getTrackNumber());
          values.put(AudioColumns.YEAR, song.getYear());
          values.put(AudioColumns.DURATION, song.getDuration());
          values.put(Constants.DATA, song.getData());
          values.put(AudioColumns.DATE_MODIFIED, song.getDateModified());
          values.put(AudioColumns.ALBUM_ID, song.getAlbumId());
          values.put(AudioColumns.ALBUM, song.getAlbumName());
          values.put(AudioColumns.ARTIST_ID, song.getArtistId());
          values.put(AudioColumns.ARTIST, song.getArtistName());
          values.put(AudioColumns.COMPOSER, song.getComposer());

          database.insert(tableName, null, values);
        }
        database.setTransactionSuccessful();
      } finally {
        database.endTransaction();
        position += NUM_PROCESS;
      }
    }
  }
}
