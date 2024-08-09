package com.devx.tictacpro.prefs

import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.Flow

interface UserPrefs {
    fun isNewUser(): Flow<Boolean>
    fun isGuestUser(): Flow<Boolean?>
    fun getAvatar(): Flow<Int?>
    fun getName(): Flow<String?>

    suspend fun setIsNewUser(value: Boolean)
    suspend fun setIsGuestUser(value: Boolean)
    suspend fun setAvatar(@DrawableRes avatar: Int)
    suspend fun setName(name: String)
}