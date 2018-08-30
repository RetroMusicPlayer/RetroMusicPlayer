/*
* Copyright (C) 2014 The CyanogenMod Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package code.name.monkey.retromusic.loaders;

import android.database.AbstractCursor;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This cursor basically wraps a song cursor and is given a list of the order of the ids of the
 * contents of the cursor. It wraps the Cursor and simulates the internal cursor being sorted
 * by moving the point to the appropriate spot
 */
public class SortedCursor extends AbstractCursor {
    // cursor to wrap
    private final Cursor mCursor;
    // the map of external indices to internal indices
    private ArrayList<Integer> mOrderedPositions;
    // this contains the ids that weren't found in the underlying cursor
    private ArrayList<String> mMissingValues;
    // this contains the mapped cursor positions and afterwards the extra ids that weren't found
    private HashMap<String, Integer> mMapCursorPositions;

    /**
     * @param cursor     to wrap
     * @param order      the list of unique ids in sorted order to display
     * @param columnName the column name of the id to look up in the internal cursor
     */
    public SortedCursor(@NonNull final Cursor cursor, @Nullable final String[] order, final String columnName) {
        mCursor = cursor;
        mMissingValues = buildCursorPositionMapping(order, columnName);
    }

    /**
     * This function populates mOrderedPositions with the cursor positions in the order based
     * on the order passed in
     *
     * @param order the target order of the internal cursor
     * @return returns the ids that aren't found in the underlying cursor
     */
    @NonNull
    private ArrayList<String> buildCursorPositionMapping(@Nullable final String[] order, final String columnName) {
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

    /**
     * @return the list of ids that weren't found in the underlying cursor
     */
    public ArrayList<String> getMissingValues() {
        return mMissingValues;
    }

    /**
     * @return the list of ids that were in the underlying cursor but not part of the ordered list
     */
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
