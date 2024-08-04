package com.devx.tictacpro.presentation.home

sealed interface HomeEvent {
    data object PlayOffline: HomeEvent
    data class OnLogout(val onSuccess: ()-> Unit): HomeEvent
    sealed interface PlayerSelectionEvent: HomeEvent {
        data class NameChange(val id: Int, val name: String): PlayerSelectionEvent
        data class TurnChange(val id: Int, val turn: Char): PlayerSelectionEvent
        data class AvatarChange(val id: Int, val icon: Int): PlayerSelectionEvent
        data object Dismiss: PlayerSelectionEvent
    }
}