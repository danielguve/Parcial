package com.example.wordlebien

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var gridBoard: GridLayout
    private lateinit var etInput: EditText
    private lateinit var btnSubmit: Button
    private lateinit var chronometer: Chronometer
    private var currentRow = 0
    private var maxAttempts = 6
    private val wordToGuess = "ROGUE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gridBoard = findViewById(R.id.gridBoard)
        etInput = findViewById(R.id.etInput)
        btnSubmit = findViewById(R.id.btnSubmit)
        chronometer = findViewById(R.id.chronometer)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        CoroutineScope(Dispatchers.Main).launch {
            maxAttempts = DataStoreManager.maxAttemptsFlow(this@GameActivity).collect { it }
        }

        btnSubmit.setOnClickListener {
            val guess = etInput.text.toString().uppercase()
            if(guess.length != 5) {
                Toast.makeText(this,"La palabra debe tener 5 letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            checkGuess(guess)
            etInput.text.clear()
        }
    }

    private fun checkGuess(guess: String) {
        if(currentRow >= maxAttempts) {
            Toast.makeText(this,"Juego terminado. La palabra era $wordToGuess", Toast.LENGTH_LONG).show()
            return
        }

        for(i in guess.indices){
            val cell = gridBoard.getChildAt(currentRow*5 + i) as TextView
            cell.text = guess[i].toString()
            when{
                guess[i] == wordToGuess[i] -> cell.setBackgroundColor(0xFF4CAF50.toInt())
                wordToGuess.contains(guess[i]) -> cell.setBackgroundColor(0xFFFFC107.toInt())
                else -> cell.setBackgroundColor(0xFF616161.toInt())
            }
        }

        if(guess == wordToGuess){
            val score = (maxAttempts - currentRow) * 10
            Toast.makeText(this,"Ganaste! Puntaje: $score", Toast.LENGTH_LONG).show()
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(this@GameActivity).scoreDao()
                    .insertScore(Score(playerName="Player", points=score))
            }
        }
        currentRow++
    }
}
