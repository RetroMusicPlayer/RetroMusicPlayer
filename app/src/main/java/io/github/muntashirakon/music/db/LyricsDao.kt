package io.github.muntashirakon.music.db

import androidx.room.*

@Dao
interface LyricsDao {
    @Query("SELECT * FROM LyricsEntity WHERE songId =:songId LIMIT 1")
    fun lyricsWithSongId(songId: Int): LyricsEntity?

    @Insert
    fun insertLyrics(lyricsEntity: LyricsEntity)

    @Delete
    fun deleteLyrics(lyricsEntity: LyricsEntity)

    @Update
    fun updateLyrics(lyricsEntity: LyricsEntity)
}