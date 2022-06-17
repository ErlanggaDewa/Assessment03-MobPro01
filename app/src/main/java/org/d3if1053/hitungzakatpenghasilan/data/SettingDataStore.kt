package org.d3if1053.hitungzakatpenghasilan.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val SAVING_PREFERENCES_NAME = "saving_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SAVING_PREFERENCES_NAME
)

class SettingDataStore(preference_datastore: DataStore<Preferences>) {
    private val IS_INCOGNITO =
        booleanPreferencesKey("is_incognito")

    val preferenceFlow: Flow<Boolean> = preference_datastore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
        // On the first run, use save to database is active
            preferences[IS_INCOGNITO] ?: false
        }

    suspend fun saveSavingToPreferencesStore(
        isIncognito: Boolean,
        context: Context
    ) {
        context.dataStore.edit { preferences ->
            preferences[IS_INCOGNITO] = isIncognito
        }
    }
}