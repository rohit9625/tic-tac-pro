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

    fun loginAsGuest(onSuccess: ()-> Unit) {
        _uiState.update { it.copy(isLoading = true) }

        val error = when(val res = authRepository.loginAsGuest()) {
            is Result.Error -> when(res.error) {
                NetworkError.NO_INTERNET -> "Internet not available"
                NetworkError.UNKNOWN -> "Please after some time"
                NetworkError.TOO_MANY_REQUEST -> "Too many login requests"
            }

            is Result.Success -> {
                onSuccess()
                null
            }
        }

        _uiState.update { it.copy(isLoading = false, error = error) }
    }
}