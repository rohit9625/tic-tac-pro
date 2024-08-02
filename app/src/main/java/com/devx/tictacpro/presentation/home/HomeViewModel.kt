package com.devx.tictacpro.presentation.home

import androidx.lifecycle.ViewModel
import com.devx.tictacpro.domain.auth.AuthRepository

class HomeViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    fun onEvent(e: HomeEvent) {
        when(e) {
            is HomeEvent.OnLogout -> {
                logout(e.onSuccess)
            }
        }
    }

    private fun logout(onSuccess: ()-> Unit) {
        authRepository.logout()
        onSuccess()
    }
}