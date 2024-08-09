package com.devx.tictacpro.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devx.tictacpro.domain.Result
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.game.GameError
import com.devx.tictacpro.domain.game.GameRepository
import com.devx.tictacpro.domain.mapper.DrawableResourceMapper
import com.devx.tictacpro.prefs.UserPrefs
import com.devx.tictacpro.presentation.home.event.HomeEvent
import com.devx.tictacpro.presentation.home.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPrefs: UserPrefs,
    private val authRepository: AuthRepository,
    private val gameRepository: GameRepository,
    private val drawableResourceMapper: DrawableResourceMapper
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPrefs.isNewUser().collect{ value->
                _uiState.update { it.copy(showAvatarSelectionDialog = value) }
            }
        }
        viewModelScope.launch {
            userPrefs.getName().collect {name->
                _uiState.update { it.copy(name = name) }
            }
        }
        viewModelScope.launch{
            userPrefs.getAvatar().collect{avatar->
                _uiState.update { it.copy(avatar = avatar) }
            }
        }
    }

    fun onEvent(e: HomeEvent) {
        when(e) {
            is HomeEvent.OnLogout -> logout(e.onSuccess)
            is HomeEvent.PlayOnline -> onPlayOnlineEvent(e)
            is HomeEvent.PlayOffline -> onPlayOfflineEvent(e)

            is HomeEvent.OnSaveUserPref -> viewModelScope.launch {
                userPrefs.setName(e.name)
                userPrefs.setAvatar(e.avatar)
                userPrefs.setIsNewUser(false)

                if(userPrefs.isGuestUser().first() != true) {
                    authRepository.updateProfile(
                        e.name,
                        drawableResourceMapper.getResourceString(e.avatar)!!
                    )
                }
            }
        }
    }

    private fun onPlayOfflineEvent(e: HomeEvent.PlayOffline) {
        when(e) {
            is HomeEvent.PlayOffline.NameChange -> {
                if(e.id == _uiState.value.playOfflineState.player1.id) {
                    _uiState.update {
                        it.copy(playOfflineState = it.playOfflineState.copy(
                            player1 = it.playOfflineState.player1.copy(name = e.name))
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(playOfflineState = it.playOfflineState.copy(
                            player2 = it.playOfflineState.player2.copy(name = e.name))
                        )
                    }
                }
            }
            is HomeEvent.PlayOffline.TurnChange -> {
                if(e.id == _uiState.value.playOfflineState.player1.id) {
                    _uiState.update {
                        it.copy(playOfflineState = it.playOfflineState.copy(
                            player1 = it.playOfflineState.player1.copy(turn = e.turn),
                            player2 = it.playOfflineState.player2.copy(turn = if(e.turn == "X") "O" else "X"))
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(playOfflineState = it.playOfflineState.copy(
                            player2 = it.playOfflineState.player2.copy(turn = e.turn),
                            player1 = it.playOfflineState.player1.copy(turn = if(e.turn == "X") "O" else "X"))
                        )
                    }
                }
            }

            is HomeEvent.PlayOffline.AvatarChange -> TODO("Not Implemented")

            HomeEvent.PlayOffline.ShowSheet -> {
                _uiState.update { it.copy(playOfflineState = it.playOfflineState.copy(isSheetVisible = true)) }
            }
            HomeEvent.PlayOffline.HideSheet -> {
                _uiState.update { it.copy(playOfflineState = it.playOfflineState.copy(isSheetVisible = false)) }
            }
        }
    }

    private fun onPlayOnlineEvent(e: HomeEvent.PlayOnline) {
        when(e) {
            is HomeEvent.PlayOnline.CreateGame -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(playOnlineState = it.playOnlineState.copy(isLoading = true)) }
                    gameRepository.createGame()
                    e.onSuccess("X")
                    _uiState.update { it.copy(
                        playOnlineState = it.playOnlineState.copy(isLoading = false)
                    )}
                }
            }

            is HomeEvent.PlayOnline.JoinGame -> {
                _uiState.update {
                    it.copy(playOnlineState = it.playOnlineState.copy(isLoading = true))
                }
                viewModelScope.launch {
                    val error = when(val res = gameRepository.joinGame(e.gameCode)) {
                        is Result.Error -> when(res.error) {
                            GameError.SERVER_ERROR -> "Please try again later"
                            GameError.CONNECTION_LOST -> "No internet connection"
                            GameError.REQUEST_TIMEOUT -> "Request timeout"
                            GameError.GAME_NOT_FOUND -> "Game code is invalid"
                            GameError.ERROR_UNKNOWN -> "Unknown error occurred"
                            GameError.GAME_CODE_MANDATORY -> "Please enter game code first"
                        }
                        is Result.Success -> {
                            e.onSuccess("O")
                            null
                        }
                        Result.Loading -> null
                    }
                    _uiState.update {
                        it.copy(playOnlineState = it.playOnlineState.copy(
                            isLoading = false, error = error
                        ))
                    }
                }
            }

            is HomeEvent.PlayOnline.OnGameCodeChange -> {
                _uiState.update { it.copy(playOnlineState = it.playOnlineState.copy(gameCode = e.code)) }
            }

            HomeEvent.PlayOnline.ShowSheet -> {
                _uiState.update { it.copy(playOnlineState = it.playOnlineState.copy(isSheetVisible = true)) }
            }
            HomeEvent.PlayOnline.HideSheet -> {
                _uiState.update { it.copy(playOnlineState = it.playOnlineState.copy(isSheetVisible = false)) }
            }
        }
    }

    private fun logout(onSuccess: ()-> Unit) {
        authRepository.logout()
        onSuccess()
    }
}