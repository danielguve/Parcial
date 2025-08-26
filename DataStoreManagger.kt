package com.example.wordlebien

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreManager {

    private val Context.dataStore by preferencesDataStore("settings_prefs")

    private val KEY_MAX_ATTEMPTS = intPreferencesKey("max_attempts")
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    private val KEY_SHOW_TIMER = booleanPreferencesKey("show_timer")

    suspend fun setMaxAttempts(context: Context, attempts: Int) {
        context.dataStore.edit { it[KEY_MAX_ATTEMPTS] = attempts }
    }

    fun maxAttemptsFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { it[KEY_MAX_ATTEMPTS] ?: 6 }

    suspend fun setDarkMode(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[KEY_DARK_MODE] = enabled }
    }

    fun darkModeFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[KEY_DARK_MODE] ?: false }

    suspend fun setShowTimer(context: Context, show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_TIMER] = show }
    }

    fun showTimerFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[KEY_SHOW_TIMER] ?: true }
}
