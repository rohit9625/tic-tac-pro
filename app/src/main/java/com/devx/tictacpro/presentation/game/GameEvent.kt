package com.devx.tictacpro.presentation.game

sealed interface GameEvent {
    data class UpdateGame(val position: Int): GameEvent
    data object ResetGame: GameEvent
}