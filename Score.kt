package com.example.wordlebien

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerName: String,
    val points: Int,
    val date: Long = System.currentTimeMillis()
)