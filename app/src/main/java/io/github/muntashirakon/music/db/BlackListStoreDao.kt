package code.name.monkey.retromusic.db

import androidx.room.*

@Dao
interface BlackListStoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlacklistPath(blackListStoreEntity: BlackListStoreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlacklistPath(blackListStoreEntities: List<BlackListStoreEntity>)

    @Delete
    suspend fun deleteBlacklistPath(blackListStoreEntity: BlackListStoreEntity)

    @Query("DELETE FROM BlackListStoreEntity")
    suspend fun clearBlacklist()

    @Query("SELECT * FROM BlackListStoreEntity")
    fun blackListPaths(): List<BlackListStoreEntity>
}