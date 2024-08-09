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
    private var board = _gameState.value.boardValues

    init {
        if(isOnlineGame) {
            viewModelScope.launch {
                gameRepository.gameState.collect { result->
                    when(result) {
                        is Result.Error -> {
                            Log.e(Constants.TAG, "Error occurred while creating game, message: ${result.error.name}")
                        }
                        is Result.Success -> {
                            Log.d(Constants.TAG, "GameState: ${result.data}")
                            _gameState.value = result.data
                        }
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
            GameEvent.DismissDialog -> { _gameState.update { it.copy(showDialog = false) } }
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

            val updatedPlayer1 = if (winner == gameState.player1!!.turn) {
                gameState.player1.copy(score = gameState.player1.score + 1)} else {
                gameState.player1
            }

            val updatedPlayer2 = if (winner == gameState.player2?.turn) {
                gameState.player2?.copy(score = gameState.player2.score + 1)
            } else {
                gameState.player2
            }

            val updatedDraws = if (checkDraw(updatedBoard, winner)) gameState.draws + 1 else gameState.draws

            gameState.copy(
                boardValues = updatedBoard,
                playerAtTurn = if (playerAtTurn == "X") "O" else "X",
                winner = winner,
                player1 = updatedPlayer1,
                player2 = updatedPlayer2,
                draws = updatedDraws,
                showDialog = winner != null
            )
        }
    }

    private fun checkWinner(board: Map<Int, String?>): String? {
        return when {
            // Corner Cases
            board[0] != null && board[0] == board[1] && board[1] == board[2] -> board[0]
            board[2] != null && board[2] == board[5] && board[5] == board[8] -> board[2]
            board[6] != null && board[6] == board[7] && board[7] == board[8] -> board[6]
            board[0] != null && board[0] == board[3] && board[3] == board[6] -> board[0]

            // Diagonal Cases
            board[0] != null && board[0] == board[4] && board[4] == board[8] -> board[0]
            board[2] != null && board[2] == board[4] && board[4] == board[6] -> board[2]

            // Middle Cases
            board[1] != null && board[1] == board[4] && board[4] == board[7] -> board[1]
            board[3] != null && board[3] == board[4] && board[4] == board[5] -> board[3]

            else -> null
        }
    }

    private fun resetBoard() {
        board = GameState.emptyField()
        _gameState.update { it.copy(boardValues = board, winner = null) }
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