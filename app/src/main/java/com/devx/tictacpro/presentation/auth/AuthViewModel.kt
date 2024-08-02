package com.devx.tictacpro.presentation.auth

import androidx.lifecycle.ViewModel
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.auth.NetworkError
import com.devx.tictacpro.domain.auth.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(e: AuthEvent) {
        when(e) {
            is AuthEvent.OnEmailChange -> _uiState.update { it.copy(email = e.email) }
            is AuthEvent.OnPasswordChange -> _uiState.update { it.copy(password = e.password)}
            is AuthEvent.OnGuestLogin -> loginAsGuest()
            is AuthEvent.OnSubmit -> TODO("Not Implemented")
        }
    }

    private fun loginAsGuest() {
        _uiState.update { it.copy(isLoading = true) }

        val error = when(val res = authRepository.loginAsGuest()) {
            is Result.Error -> when(res.error) {
                NetworkError.NO_INTERNET -> "Internet not available"
                NetworkError.UNKNOWN -> "Please after some time"
                NetworkError.TOO_MANY_REQUEST -> "Too many login requests"
            }

            is Result.Success -> {
                null
            }
        }

        _uiState.update { it.copy(isLoading = false, error = error) }
    }
}