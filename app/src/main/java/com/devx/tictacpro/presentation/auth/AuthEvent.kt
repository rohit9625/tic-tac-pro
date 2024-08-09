package com.devx.tictacpro.presentation.auth

sealed interface AuthEvent {
    data class OnEmailChange(val email: String) : AuthEvent
    data class OnPasswordChange(val password: String) : AuthEvent
    data class OnSubmit(val onSuccess: ()-> Unit) : AuthEvent
    data class OnGuestLogin(val onSuccess: ()-> Unit) : AuthEvent
}