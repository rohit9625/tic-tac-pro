package com.devx.tictacpro.prefs

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devx.tictacpro.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class UserPrefsImpl(private val dataStore: DataStore<Preferences>): UserPrefs {
    private val isNewUser = booleanPreferencesKey("is_new_user")
    private val isGuestUser = booleanPreferencesKey("is_guest_user")
    private val playerAvatar = intPreferencesKey("player_avatar")
    private val playerName = stringPreferencesKey("player_name")

    override fun isNewUser(): Flow<Boolean> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[isNewUser] ?: true
        }
    }

    override fun isGuestUser(): Flow<Boolean?> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            Log.d(Constants.TAG, "Guest User: ${it[isGuestUser]}")
            it[isGuestUser]
        }
    }

    override fun getAvatar(): Flow<Int?> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[playerAvatar]
        }
    }

    override fun getName(): Flow<String?> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[playerName]
        }
    }

    override suspend fun setIsNewUser(value: Boolean) {
        dataStore.edit { it[isNewUser] = value }
    }

    override suspend fun setIsGuestUser(value: Boolean) {
        dataStore.edit { it[isGuestUser] = value }
    }

    override suspend fun setAvatar(avatar: Int) {
        dataStore.edit { it[playerAvatar] = avatar }
    }

    override suspend fun setName(name: String) {
        dataStore.edit { it[playerName] = name }
    }
}