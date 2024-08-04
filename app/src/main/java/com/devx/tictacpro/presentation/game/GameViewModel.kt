package com.devx.tictacpro.presentation.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.devx.tictacpro.presentation.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel(player1: Player, player2: Player): ViewModel() {
    private var _uiState = MutableStateFlow(GameState(
        player1 = player1, player2 = player2
    ))
    val uiState = _uiState.asStateFlow()
    private var board = _uiState.value.boardValues

    fun onEvent(e: GameEvent) {
        when(e) {
            is GameEvent.UpdateGame -> updateGame(e.position)
            is GameEvent.ResetGame -> resetBoard()
            GameEvent.DismissDialog -> {
                _uiState.update { it.copy(isDraw = false) }
            }
        }
    }

    private fun updateGame(position: Int) {
        if(board[position] != null) {
            return
        }
        val playerAtTurn = _uiState.value.playerAtTurn

        board[position] = playerAtTurn
        _uiState.update {
            it.copy(boardValues = board, playerAtTurn = if (playerAtTurn == 'X') 'O' else 'X')
        }

        val winner = checkWinner()

        winner?.let { turn->
            updateScore(turn)

            if(winner == _uiState.value.player1.turn) {
                _uiState.update {
                    it.copy(winningPlayer = turn)
                }

                Log.d("MyTag", "Player 1 Score: ${_uiState.value.player1.score}")
            } else {
                _uiState.update {
                    it.copy(winningPlayer = turn)
                }
                Log.d("MyTag", "Player 2 Score: ${_uiState.value.player2.score}")
            }
        }

        if(checkDraw()) {
            _uiState.update { it.copy(isDraw = true) }
            _uiState.value.draw++
        }
    }

    private fun checkWinner(): Char? {
        return when {
            //Corner Cases
            board[0] == board[1] && board[1] == board[2] ->  board[0]
            board[2] == board[5] && board[5] == board[8] ->  board[2]
            board[6] == board[7] && board[7] == board[8] ->  board[6]
            board[0] == board[3] && board[3] == board[6] ->  board[0]

            //Diagonal Cases
            board[0] == board[4] && board[4] == board[8] ->  board[0]
            board[2] == board[4] && board[4] == board[6] ->  board[2]

            //Middle Cases
            board[1] == board[4] && board[4] == board[7] ->  board[1]
            board[3] == board[4] && board[4] == board[5] ->  board[3]

            else -> null
        }
    }

    private fun resetBoard() {
        board = GameState.emptyField()
        _uiState.update { it.copy(boardValues = board, winningPlayer = null) }
    }

    private fun updateScore(winner: Char) {
        if(winner == _uiState.value.player1.turn) {
            val player = _uiState.value.player1
            _uiState.update {
                it.copy(player1 = player.copy(score = player.score + 1))
            }
        } else {
            val player = _uiState.value.player2
            _uiState.update { it.copy(player2 = player.copy(score = player.score + 1)) }
        }
    }

    private fun checkDraw(): Boolean {
        return (_uiState.value.winningPlayer == null && _uiState.value.isBoardFull)
    }
}