package com.devx.tictacpro.domain.auth

import com.devx.tictacpro.data.game.User
import com.devx.tictacpro.domain.Result

interface AuthRepository {
    suspend fun loginAsGuest(): Result<Unit, NetworkError>

    suspend fun signUpWithEmailPassword(email: String, password: String): Result<Unit, NetworkError>

    suspend fun signInWithEmailPassword(email: String, password: String): Result<User, NetworkError>

    suspend fun updateProfile(name: String, avatar: String): Result<Unit, NetworkError>

    fun logout()
}