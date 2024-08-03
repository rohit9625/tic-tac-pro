package com.devx.tictacpro.domain.auth

interface AuthRepository {
    fun loginAsGuest(): Result<Unit, NetworkError>

    suspend fun signUpWithEmailPassword(email: String, password: String): Result<Unit, NetworkError>

    suspend fun signInWithEmailPassword(email: String, password: String): Result<Unit, NetworkError>

    fun logout()
}