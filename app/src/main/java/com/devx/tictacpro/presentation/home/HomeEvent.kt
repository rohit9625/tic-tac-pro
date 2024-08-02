package com.devx.tictacpro.presentation.home

sealed interface HomeEvent {
    data class OnLogout(val onSuccess: ()-> Unit): HomeEvent
}