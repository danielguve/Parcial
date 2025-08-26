package com.example.wordleParcial


import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {

    private lateinit var switchDarkMode: Switch
    private lateinit var switchShowTimer: Switch
    private lateinit var rbEasy: RadioButton
    private lateinit var rbMedium: RadioButton
    private lateinit var rbHard: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switchDarkMode = findViewById(R.id.switchDarkMode)
        switchShowTimer = findViewById(R.id.switchShowTimer)
        rbEasy = findViewById(R.id.rbEasy)
        rbMedium = findViewById(R.id.rbMedium)
        rbHard = findViewById(R.id.rbHard)

        CoroutineScope(Dispatchers.Main).launch {
            DataStoreManager.darkModeFlow(this@SettingActivity).collect { switchDarkMode.isChecked = it }
            DataStoreManager.showTimerFlow(this@SettingActivity).collect { switchShowTimer.isChecked = it }
            DataStoreManager.maxAttemptsFlow(this@SettingActivity).collect {
                when(it){
                    3 -> rbHard.isChecked = true
                    4 -> rbMedium.isChecked = true
                    6 -> rbEasy.isChecked = true
                }
            }
        }

        switchDarkMode.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            CoroutineScope(Dispatchers.IO).launch { DataStoreManager.setDarkMode(this@SettingActivity, isChecked) }
        }

        switchShowTimer.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            CoroutineScope(Dispatchers.IO).launch { DataStoreManager.setShowTimer(this@SettingActivity, isChecked) }
        }

        rbEasy.setOnCheckedChangeListener { _, isChecked -> if(isChecked) saveAttempts(6) }
        rbMedium.setOnCheckedChangeListener { _, isChecked -> if(isChecked) saveAttempts(4) }
        rbHard.setOnCheckedChangeListener { _, isChecked -> if(isChecked) saveAttempts(3) }
    }

    private fun saveAttempts(value: Int){
        CoroutineScope(Dispatchers.IO).launch { DataStoreManager.setMaxAttempts(this@SettingActivity, value) }
    }
}
