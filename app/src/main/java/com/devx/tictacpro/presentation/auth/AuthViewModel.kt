package com.devx.tictacpro.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.auth.NetworkError
import com.devx.tictacpro.domain.auth.Result
import com.devx.tictacpro.domain.mapper.AuthErrorMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            is AuthEvent.OnSubmit -> {
                viewModelScope.launch {
                    loginUser(e.onSuccess)
                }
            }
        }
    }

    private suspend fun loginUser(onSuccess: ()-> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        val signUpResult = authRepository.signUpWithEmailPassword(uiState.value.email, uiState.value.password)

        val error = when(signUpResult) {
            is Result.Error -> {
                if(signUpResult.error == NetworkError.AuthError.USER_ALREADY_EXISTS) {
                    val res = authRepository.signInWithEmailPassword(_uiState.value.email, _uiState.value.password)

                    when(res) {
                        is Result.Error -> AuthErrorMapper.mapToMessage(res.error)
                        is Result.Success -> null
                    }

                } else {
                    AuthErrorMapper.mapToMessage(signUpResult.error)
                }
            }

            is Result.Success -> null
        }
        _uiState.update { it.copy(isLoading = false, error = error) }
        if(error == null) {
            onSuccess()
        }
    }

    private fun loginAsGuest() {
        _uiState.update { it.copy(isLoading = true) }

        val error = when(val res = authRepository.loginAsGuest()) {
            is Result.Error -> when(res.error) {
                NetworkError.AuthError.CONNECTION_ERROR -> "Internet not available"
                NetworkError.AuthError.SERVER_ERROR -> "Internal server error"
                NetworkError.AuthError.TOO_MANY_REQUEST -> "Too many requests"
                else -> null
            }

            is Result.Success -> {
                null
            }
        }

        _uiState.update { it.copy(isLoading = false, error = error) }
    }
}