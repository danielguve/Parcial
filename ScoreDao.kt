package com.example.wordlebien

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {
    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM scores ORDER BY date DESC")
    suspend fun getScoresByDate(): List<Score>
}
