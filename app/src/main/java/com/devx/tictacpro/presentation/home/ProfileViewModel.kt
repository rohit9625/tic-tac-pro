package com.devx.tictacpro.presentation.home

import androidx.lifecycle.ViewModel
import com.devx.tictacpro.prefs.UserPrefs
import com.devx.tictacpro.presentation.home.event.ProfileEvent
import com.devx.tictacpro.presentation.home.state.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    initialState: ProfileState,
    private val userPrefs: UserPrefs
): ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: ProfileEvent) {
        when(e) {
            is ProfileEvent.OnAvatarChange -> {
                _uiState.update { it.copy(avatar = e.avatar) }
            }
            is ProfileEvent.OnNameChange -> {
                _uiState.update { it.copy(name = e.name) }
            }
            ProfileEvent.OnSave -> TODO()
        }
    }
}