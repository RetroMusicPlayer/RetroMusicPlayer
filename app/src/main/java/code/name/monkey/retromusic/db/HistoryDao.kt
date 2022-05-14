/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    companion object {
        private const val HISTORY_LIMIT = 100
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongInHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity WHERE id = :songId LIMIT 1")
    suspend fun isSongPresentInHistory(songId: Long): HistoryEntity?

    @Update
    suspend fun updateHistorySong(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT $HISTORY_LIMIT")
    fun historySongs(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT $HISTORY_LIMIT")
    fun observableHistorySongs(): LiveData<List<HistoryEntity>>
}
