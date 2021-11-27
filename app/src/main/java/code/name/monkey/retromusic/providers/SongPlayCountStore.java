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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This database tracks the number of play counts for an individual song. This is used to drive the
 * top played tracks as well as the playlist images
 */
public class SongPlayCountStore extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "song_play_count.db";
  private static final int VERSION = 3;
  // how many weeks worth of playback to track
  private static final int NUM_WEEKS = 52;
  @Nullable
  private static SongPlayCountStore sInstance = null;
  // interpolator curve applied for measuring the curve
  @NonNull
  private static final Interpolator sInterpolator = new AccelerateInterpolator(1.5f);
  // how high to multiply the interpolation curve
  private static final int INTERPOLATOR_HEIGHT = 50;

  // how high the base value is. The ratio of the Height to Base is what really matters
  private static final int INTERPOLATOR_BASE = 25;

  private static final int ONE_WEEK_IN_MS = 1000 * 60 * 60 * 24 * 7;

  @NonNull
  private static final String WHERE_ID_EQUALS = SongPlayCountColumns.ID + "=?";

  // number of weeks since epoch time
  private final int mNumberOfWeeksSinceEpoch;

  // used to track if we've walked through the db and updated all the rows
  private boolean mDatabaseUpdated;

  public SongPlayCountStore(final Context context) {
    super(context, DATABASE_NAME, null, VERSION);

    long msSinceEpoch = System.currentTimeMillis();
    mNumberOfWeeksSinceEpoch = (int) (msSinceEpoch / ONE_WEEK_IN_MS);

    mDatabaseUpdated = false;
  }

  /**
   * @param context The {@link Context} to use
   * @return A new instance of this class.
   */
  @NonNull
  public static synchronized SongPlayCountStore getInstance(@NonNull final Context context) {
    if (sInstance == null) {
      sInstance = new SongPlayCountStore(context.getApplicationContext());
    }
    return sInstance;
  }

  /**
   * Calculates the score of the song given the play counts
   *
   * @param playCounts an array of the # of times a song has been played for each week where
   *     playCounts[N] is the # of times it was played N weeks ago
   * @return the score
   */
  private static float calculateScore(@Nullable final int[] playCounts) {
    if (playCounts == null) {
      return 0;
    }

    float score = 0;
    for (int i = 0; i < Math.min(playCounts.length, NUM_WEEKS); i++) {
      score += playCounts[i] * getScoreMultiplierForWeek(i);
    }

    return score;
  }

  /**
   * Gets the column name for each week #
   *
   * @param week number
   * @return the column name
   */
  @NonNull
  private static String getColumnNameForWeek(final int week) {
    return SongPlayCountColumns.WEEK_PLAY_COUNT + week;
  }

  /**
   * Gets the score multiplier for each week
   *
   * @param week number
   * @return the multiplier to apply
   */
  private static float getScoreMultiplierForWeek(final int week) {
    return sInterpolator.getInterpolation(1 - (week / (float) NUM_WEEKS)) * INTERPOLATOR_HEIGHT
        + INTERPOLATOR_BASE;
  }

  /**
   * For some performance gain, return a static value for the column index for a week WARNING: This
   * function assumes you have selected all columns for it to work
   *
   * @param week number
   * @return column index of that week
   */
  private static int getColumnIndexForWeek(final int week) {
    // ID, followed by the weeks columns
    return 1 + week;
  }

  @Override
  public void onCreate(@NonNull final SQLiteDatabase db) {
    // create the play count table
    // WARNING: If you change the order of these columns
    // please update getColumnIndexForWeek
    StringBuilder builder = new StringBuilder();
    builder.append("CREATE TABLE IF NOT EXISTS ");
    builder.append(SongPlayCountColumns.NAME);
    builder.append("(");
    builder.append(SongPlayCountColumns.ID);
    builder.append(" INT UNIQUE,");

    for (int i = 0; i < NUM_WEEKS; i++) {
      builder.append(getColumnNameForWeek(i));
      builder.append(" INT DEFAULT 0,");
    }

    builder.append(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
    builder.append(" INT NOT NULL,");

    builder.append(SongPlayCountColumns.PLAY_COUNT_SCORE);
    builder.append(" REAL DEFAULT 0);");

    db.execSQL(builder.toString());
  }

  @Override
  public void onUpgrade(
      @NonNull final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + SongPlayCountColumns.NAME);
    onCreate(db);
  }

  @Override
  public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    // If we ever have downgrade, drop the table to be safe
    db.execSQL("DROP TABLE IF EXISTS " + SongPlayCountColumns.NAME);
    onCreate(db);
  }

  /**
   * Increases the play count of a song by 1
   *
   * @param songId The song id to increase the play count
   */
  public void bumpPlayCount(final long songId) {
    if (songId == -1) {
      return;
    }

    final SQLiteDatabase database = getWritableDatabase();
    updateExistingRow(database, songId, true);
  }

  /**
   * This creates a new entry that indicates a song has been played once as well as its score
   *
   * @param database a write able database
   * @param songId the id of the track
   */
  private void createNewPlayedEntry(@NonNull final SQLiteDatabase database, final long songId) {
    // no row exists, create a new one
    float newScore = getScoreMultiplierForWeek(0);
    int newPlayCount = 1;

    final ContentValues values = new ContentValues(3);
    values.put(SongPlayCountColumns.ID, songId);
    values.put(SongPlayCountColumns.PLAY_COUNT_SCORE, newScore);
    values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeeksSinceEpoch);
    values.put(getColumnNameForWeek(0), newPlayCount);

    database.insert(SongPlayCountColumns.NAME, null, values);
  }

  /**
   * This function will take a song entry and update it to the latest week and increase the count
   * for the current week by 1 if necessary
   *
   * @param database a writeable database
   * @param id the id of the track to bump
   * @param bumpCount whether to bump the current's week play count by 1 and adjust the score
   */
  private void updateExistingRow(
      @NonNull final SQLiteDatabase database, final long id, boolean bumpCount) {
    String stringId = String.valueOf(id);

    // begin the transaction
    database.beginTransaction();

    // get the cursor of this content inside the transaction
    final Cursor cursor =
        database.query(
            SongPlayCountColumns.NAME,
            null,
            WHERE_ID_EQUALS,
            new String[] {stringId},
            null,
            null,
            null);

    // if we have a result
    if (cursor != null && cursor.moveToFirst()) {
      // figure how many weeks since we last updated
      int lastUpdatedIndex = cursor.getColumnIndex(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
      int lastUpdatedWeek = cursor.getInt(lastUpdatedIndex);
      int weekDiff = mNumberOfWeeksSinceEpoch - lastUpdatedWeek;

      // if it's more than the number of weeks we track, delete it and create a new entry
      if (Math.abs(weekDiff) >= NUM_WEEKS) {
        // this entry needs to be dropped since it is too outdated
        deleteEntry(database, stringId);
        if (bumpCount) {
          createNewPlayedEntry(database, id);
        }
      } else if (weekDiff != 0) {
        // else, shift the weeks
        int[] playCounts = new int[NUM_WEEKS];

        if (weekDiff > 0) {
          // time is shifted forwards
          for (int i = 0; i < NUM_WEEKS - weekDiff; i++) {
            playCounts[i + weekDiff] = cursor.getInt(getColumnIndexForWeek(i));
          }
        } else {
          // time is shifted backwards (by user) - nor typical behavior but we
          // will still handle it

          // since weekDiff is -ve, NUM_WEEKS + weekDiff is the real # of weeks we have to
          // transfer.  Then we transfer the old week i - weekDiff to week i
          // for example if the user shifted back 2 weeks, ie -2, then for 0 to
          // NUM_WEEKS + (-2) we set the new week i = old week i - (-2) or i+2
          for (int i = 0; i < NUM_WEEKS + weekDiff; i++) {
            playCounts[i] = cursor.getInt(getColumnIndexForWeek(i - weekDiff));
          }
        }

        // bump the count
        if (bumpCount) {
          playCounts[0]++;
        }

        float score = calculateScore(playCounts);

        // if the score is non-existant, then delete it
        if (score < .01f) {
          deleteEntry(database, stringId);
        } else {
          // create the content values
          ContentValues values = new ContentValues(NUM_WEEKS + 2);
          values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeeksSinceEpoch);
          values.put(SongPlayCountColumns.PLAY_COUNT_SCORE, score);

          for (int i = 0; i < NUM_WEEKS; i++) {
            values.put(getColumnNameForWeek(i), playCounts[i]);
          }

          // update the entry
          database.update(
              SongPlayCountColumns.NAME, values, WHERE_ID_EQUALS, new String[] {stringId});
        }
      } else if (bumpCount) {
        // else no shifting, just update the scores
        ContentValues values = new ContentValues(2);

        // increase the score by a single score amount
        int scoreIndex = cursor.getColumnIndex(SongPlayCountColumns.PLAY_COUNT_SCORE);
        float score = cursor.getFloat(scoreIndex) + getScoreMultiplierForWeek(0);
        values.put(SongPlayCountColumns.PLAY_COUNT_SCORE, score);

        // increase the play count by 1
        values.put(getColumnNameForWeek(0), cursor.getInt(getColumnIndexForWeek(0)) + 1);

        // update the entry
        database.update(
            SongPlayCountColumns.NAME, values, WHERE_ID_EQUALS, new String[] {stringId});
      }

      cursor.close();
    } else if (bumpCount) {
      // if we have no existing results, create a new one
      createNewPlayedEntry(database, id);
    }

    database.setTransactionSuccessful();
    database.endTransaction();
  }

  public void clear() {
    final SQLiteDatabase database = getWritableDatabase();
    database.delete(SongPlayCountColumns.NAME, null, null);
  }

  /**
   * Gets a cursor containing the top songs played. Note this only returns songs that have been
   * played at least once in the past NUM_WEEKS
   *
   * @param numResults number of results to limit by. If <= 0 it returns all results
   * @return the top tracks
   */
  public Cursor getTopPlayedResults(int numResults) {
    updateResults();

    final SQLiteDatabase database = getReadableDatabase();
    return database.query(
        SongPlayCountColumns.NAME,
        new String[] {SongPlayCountColumns.ID},
        null,
        null,
        null,
        null,
        SongPlayCountColumns.PLAY_COUNT_SCORE + " DESC",
        (numResults <= 0 ? null : String.valueOf(numResults)));
  }

  /**
   * This updates all the results for the getTopPlayedResults so that we can get an accurate list of
   * the top played results
   */
  private synchronized void updateResults() {
    if (mDatabaseUpdated) {
      return;
    }

    final SQLiteDatabase database = getWritableDatabase();

    database.beginTransaction();

    int oldestWeekWeCareAbout = mNumberOfWeeksSinceEpoch - NUM_WEEKS + 1;
    // delete rows we don't care about anymore
    database.delete(
        SongPlayCountColumns.NAME,
        SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX + " < " + oldestWeekWeCareAbout,
        null);

    // get the remaining rows
    Cursor cursor =
        database.query(
            SongPlayCountColumns.NAME,
            new String[] {SongPlayCountColumns.ID},
            null,
            null,
            null,
            null,
            null);

    if (cursor != null && cursor.moveToFirst()) {
      // for each row, update it
      do {
        updateExistingRow(database, cursor.getLong(0), false);
      } while (cursor.moveToNext());

      cursor.close();
    }

    mDatabaseUpdated = true;
    database.setTransactionSuccessful();
    database.endTransaction();
  }

  /** @param songId The song Id to remove. */
  public void removeItem(final long songId) {
    final SQLiteDatabase database = getWritableDatabase();
    deleteEntry(database, String.valueOf(songId));
  }

  /**
   * Deletes the entry
   *
   * @param database database to use
   * @param stringId id to delete
   */
  private void deleteEntry(@NonNull final SQLiteDatabase database, final String stringId) {
    database.delete(SongPlayCountColumns.NAME, WHERE_ID_EQUALS, new String[] {stringId});
  }

  public interface SongPlayCountColumns {

    String NAME = "song_play_count";

    String ID = "song_id";

    String WEEK_PLAY_COUNT = "week";

    String LAST_UPDATED_WEEK_INDEX = "week_index";

    String PLAY_COUNT_SCORE = "play_count_score";
  }
}
