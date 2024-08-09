package com.devx.tictacpro.presentation.home.event

sealed interface HomeEvent {
    data class OnLogout(val onSuccess: ()-> Unit): HomeEvent
    data class OnSaveUserPref(val name: String, val avatar: Int): HomeEvent

    sealed interface PlayOnline: HomeEvent {
        data class OnGameCodeChange(val code: String): PlayOnline
        data class JoinGame(val onSuccess: (turn: String)-> Unit, val gameCode: String): PlayOnline
        data class CreateGame(val onSuccess: (turn: String)-> Unit): PlayOnline
        data object ShowSheet: PlayOnline
        data object HideSheet: PlayOnline
    }

    sealed interface PlayOffline: HomeEvent {
        data class NameChange(val id: String, val name: String): PlayOffline
        data class TurnChange(val id: String, val turn: String): PlayOffline
        data class AvatarChange(val id: String, val icon: Int): PlayOffline
        data object ShowSheet: PlayOffline
        data object HideSheet: PlayOffline
    }
}