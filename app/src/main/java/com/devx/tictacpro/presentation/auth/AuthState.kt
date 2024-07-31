package com.devx.tictacpro.presentation.auth

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null
)
