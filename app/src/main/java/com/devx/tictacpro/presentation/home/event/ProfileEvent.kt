package com.devx.tictacpro.presentation.home.event

sealed interface ProfileEvent {
    data class OnAvatarChange(val avatar: Int): ProfileEvent
    data class OnNameChange(val name: String): ProfileEvent
    data object OnSave: ProfileEvent
}