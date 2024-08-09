package com.devx.tictacpro.domain.game

import com.devx.tictacpro.domain.Result
import com.devx.tictacpro.presentation.game.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface GameRepository {
    val gameState: StateFlow<Result<GameState, GameError>>

    suspend fun createGame()

    fun deleteGame()

    suspend fun updateGame(gameState: GameState)

    suspend fun joinGame(gameCode: String): Result<Unit, GameError>
}