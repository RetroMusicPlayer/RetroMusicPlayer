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
package code.name.monkey.retromusic.repository;

import android.database.AbstractCursor;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This cursor basically wraps a song cursor and is given a list of the order of the ids of the
 * contents of the cursor. It wraps the Cursor and simulates the internal cursor being sorted by
 * moving the point to the appropriate spot
 */
public class SortedCursor extends AbstractCursor {
  // cursor to wrap
  private final Cursor mCursor;
  // the map of external indices to internal indices
  private ArrayList<Integer> mOrderedPositions;
  // this contains the ids that weren't found in the underlying cursor
  private final ArrayList<String> mMissingValues;
  // this contains the mapped cursor positions and afterwards the extra ids that weren't found
  private HashMap<String, Integer> mMapCursorPositions;

  /**
   * @param cursor to wrap
   * @param order the list of unique ids in sorted order to display
   * @param columnName the column name of the id to look up in the internal cursor
   */
  public SortedCursor(
      @NonNull final Cursor cursor, @Nullable final String[] order, final String columnName) {
    mCursor = cursor;
    mMissingValues = buildCursorPositionMapping(order, columnName);
  }

  /**
   * This function populates mOrderedPositions with the cursor positions in the order based on the
   * order passed in
   *
   * @param order the target order of the internal cursor
   * @return returns the ids that aren't found in the underlying cursor
   */
  @NonNull
  private ArrayList<String> buildCursorPositionMapping(
      @Nullable final String[] order, final String columnName) {
    ArrayList<String> missingValues = new ArrayList<>();

    mOrderedPositions = new ArrayList<>(mCursor.getCount());

    mMapCursorPositions = new HashMap<>(mCursor.getCount());
    final int valueColumnIndex = mCursor.getColumnIndex(columnName);

    if (mCursor.moveToFirst()) {
      // first figure out where each of the ids are in the cursor
      do {
        mMapCursorPositions.put(mCursor.getString(valueColumnIndex), mCursor.getPosition());
      } while (mCursor.moveToNext());

      if (order != null) {
        // now create the ordered positions to map to the internal cursor given the
        // external sort order
        for (final String value : order) {
          if (mMapCursorPositions.containsKey(value)) {
            mOrderedPositions.add(mMapCursorPositions.get(value));
            mMapCursorPositions.remove(value);
          } else {
            missingValues.add(value);
          }
        }
      }

      mCursor.moveToFirst();
    }

    return missingValues;
  }

  /** @return the list of ids that weren't found in the underlying cursor */
  public ArrayList<String> getMissingValues() {
    return mMissingValues;
  }

  /** @return the list of ids that were in the underlying cursor but not part of the ordered list */
  @NonNull
  public Collection<String> getExtraValues() {
    return mMapCursorPositions.keySet();
  }

  @Override
  public void close() {
    mCursor.close();

    super.close();
  }

  @Override
  public int getCount() {
    return mOrderedPositions.size();
  }

  @Override
  public String[] getColumnNames() {
    return mCursor.getColumnNames();
  }

  @Override
  public String getString(int column) {
    return mCursor.getString(column);
  }

  @Override
  public short getShort(int column) {
    return mCursor.getShort(column);
  }

  @Override
  public int getInt(int column) {
    return mCursor.getInt(column);
  }

  @Override
  public long getLong(int column) {
    return mCursor.getLong(column);
  }

  @Override
  public float getFloat(int column) {
    return mCursor.getFloat(column);
  }

  @Override
  public double getDouble(int column) {
    return mCursor.getDouble(column);
  }

  @Override
  public boolean isNull(int column) {
    return mCursor.isNull(column);
  }

  @Override
  public boolean onMove(int oldPosition, int newPosition) {
    if (newPosition >= 0 && newPosition < getCount()) {
      mCursor.moveToPosition(mOrderedPositions.get(newPosition));
      return true;
    }

    return false;
  }
}
