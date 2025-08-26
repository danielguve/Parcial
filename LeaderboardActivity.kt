package com.example.wordlebien

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaderboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val container = findViewById<LinearLayout>(R.id.leaderboardContainer)

        CoroutineScope(Dispatchers.Main).launch {
            val scores = AppDatabase.getDatabase(this@LeaderboardActivity)
                .scoreDao().getScoresByDate()

            for ((i, score) in scores.withIndex()) {
                val tv = TextView(this@LeaderboardActivity)
                tv.text = "${i+1}. ${score.playerName} - ${score.points}"
                tv.textSize = 18f
                tv.setTextColor(0xFFFFFFFF.toInt())
                container.addView(tv)
            }
        }
    }
}
