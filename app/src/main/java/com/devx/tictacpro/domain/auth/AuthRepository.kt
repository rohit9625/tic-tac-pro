package com.devx.tictacpro.domain.auth

interface AuthRepository {
    fun loginAsGuest(): Result<Unit, NetworkError>

    fun loginWithGoogle()
}