package com.devx.tictacpro.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.mapper.DrawableResourceMapper
import com.devx.tictacpro.prefs.UserPrefs
import com.devx.tictacpro.presentation.home.event.ProfileEvent
import com.devx.tictacpro.presentation.home.state.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    initialState: ProfileState,
    private val userPrefs: UserPrefs,
    private val authRepository: AuthRepository,
    private val resourceMapper: DrawableResourceMapper
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
            ProfileEvent.OnSave -> {
                viewModelScope.launch {
                    userPrefs.setName(_uiState.value.name)
                    userPrefs.setAvatar(_uiState.value.avatar)

                    authRepository.updateProfile(
                        _uiState.value.name,
                        resourceMapper.getResourceString(_uiState.value.avatar)!!
                    )
                }
            }
        }
    }
}