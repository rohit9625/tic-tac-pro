package com.devx.tictacpro.presentation.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devx.tictacpro.domain.Result
import com.devx.tictacpro.domain.game.GameRepository
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    player1: Player?, player2: Player?,
    private val isOnlineGame: Boolean,
    private val gameRepository: GameRepository
): ViewModel() {
    private var _gameState = MutableStateFlow(GameState(
        player1 = player1, player2 = player2
    ))
    val gameState = _gameState.asStateFlow()

    init {
        if(isOnlineGame) {
            viewModelScope.launch {
                gameRepository.gameState.collect { result->
                    when(result) {
                        is Result.Error -> {
                            Log.e(Constants.TAG, "Error occurred while creating game, message: ${result.error.name}")
                        }
                        is Result.Success -> _gameState.value = result.data
                        Result.Loading -> {
                            Log.d(Constants.TAG, "Game Screen Initialized")
                        }
                    }
                }
            }
        }
    }

    fun onEvent(e: GameEvent) {
        when(e) {
            is GameEvent.UpdateGame -> updateGame(e.position)
            GameEvent.ResetGame -> resetBoard()
        }
        if(isOnlineGame) {
            viewModelScope.launch { gameRepository.updateGame(_gameState.value) }
        }
    }

    private fun updateGame(position: Int) {
        if(_gameState.value.boardValues[position] != null) {
            return
        }
        val playerAtTurn = _gameState.value.playerAtTurn

        _gameState.update { gameState ->
            val updatedBoard = gameState.boardValues.toMutableMap()
            updatedBoard[position] = playerAtTurn

            val winner = checkWinner(updatedBoard)

            val updatedPlayer1 = if(winner?.first == gameState.player1!!.turn)
                gameState.player1.copy(score = gameState.player1.score + 1)
            else
                gameState.player1

            val updatedPlayer2 = if(winner?.first == gameState.player2!!.turn)
                gameState.player2.copy(score = gameState.player2.score + 1)
            else
                gameState.player2

            val updatedDraws = if (checkDraw(updatedBoard, winner?.first)) gameState.draws + 1 else gameState.draws

            gameState.copy(
                boardValues = updatedBoard,
                playerAtTurn = if (playerAtTurn == "X") "O" else "X",
                winner = winner?.first,
                winningLine = winner?.second ?: emptyList(),
                player1 = updatedPlayer1,
                player2 = updatedPlayer2,
                draws = updatedDraws
            )
        }
    }

    private fun checkWinner(board: Map<Int, String?>): Pair<String, List<Int>>? {
        val winningCombinations = listOf(
            listOf(0, 1, 2), listOf(2, 5, 8), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(0, 4, 8), listOf(2, 4, 6),
            listOf(1, 4, 7), listOf(3, 4, 5)
        )

        for(combination in winningCombinations) {
            val (a, b, c) = combination
            if(board[a] != null && board[a] == board[b] && board[b] == board[c]) {
                return board[a]!! to combination
            }
        }

        return null
    }

    private fun resetBoard() {
        _gameState.update { it.copy(boardValues = GameState.emptyField(), winner = null) }
    }

    private fun checkDraw(board: Map<Int, String?>, winner: String?): Boolean {
        return (winner == null && board.values.all { it != null })
    }

    override fun onCleared() {
        super.onCleared()
        if(isOnlineGame) {
            Log.d(Constants.TAG, "Game Cleared")
            gameRepository.deleteGame()
        }
    }
}