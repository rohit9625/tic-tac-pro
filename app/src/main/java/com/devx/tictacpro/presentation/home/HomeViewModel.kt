package com.devx.tictacpro.presentation.home

import androidx.lifecycle.ViewModel
import com.devx.tictacpro.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val _playerSelectionState = MutableStateFlow(_uiState.value.playerSelectionState)
    val playerSelectionState = _playerSelectionState.asStateFlow()

    fun onEvent(e: HomeEvent) {
        when(e) {
            is HomeEvent.OnLogout -> logout(e.onSuccess)

            HomeEvent.PlayOffline -> {
                _playerSelectionState.update { it.copy(isDialogVisible = true) }
            }
            is HomeEvent.PlayerSelectionEvent.Dismiss -> {
                _playerSelectionState.update { it.copy(isDialogVisible = false) }
            }
            is HomeEvent.PlayerSelectionEvent.NameChange -> {
                if(e.id == _playerSelectionState.value.firstPlayer.id) {
                    _playerSelectionState.update {
                        it.copy(firstPlayer = it.firstPlayer.copy(name = e.name))
                    }
                } else {
                    _playerSelectionState.update {
                        it.copy(secondPlayer = it.secondPlayer.copy(name = e.name))
                    }
                }
            }
            is HomeEvent.PlayerSelectionEvent.TurnChange -> {
                if(e.id == _playerSelectionState.value.firstPlayer.id) {
                    _playerSelectionState.update {
                        it.copy(
                            firstPlayer = it.firstPlayer.copy(turn = e.turn),
                            secondPlayer = it.secondPlayer.copy(turn = if(e.turn == 'X') 'O' else 'X')
                        )
                    }
                } else {
                    _playerSelectionState.update {
                        it.copy(
                            firstPlayer = it.firstPlayer.copy(turn = if(e.turn == 'X') 'O' else 'X'),
                            secondPlayer = it.secondPlayer.copy(turn = e.turn)
                        )
                    }
                }
            }
            is HomeEvent.PlayerSelectionEvent.AvatarChange -> TODO("Not Implemented")
        }
    }

    private fun logout(onSuccess: ()-> Unit) {
        authRepository.logout()
        onSuccess()
    }
}