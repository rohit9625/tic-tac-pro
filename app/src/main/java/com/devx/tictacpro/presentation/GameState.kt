package com.devx.tictacpro.presentation

import androidx.compose.runtime.mutableStateMapOf
import com.devx.tictacpro.R

data class GameState(
    val playerAtTurn: Char? = 'X',
    val boardValues: MutableMap<Int, Char?> = emptyField(),
    val player1: Player = Player("Player 1", R.drawable.boy_avatar1, 'X'),
    val player2: Player = Player("Player 2 ", R.drawable.boy_avatar_2, 'O'),
    val winningPlayer: Char? = null,
    var draw: Int = 0,
) {
    val isBoardFull: Boolean
        get() = boardValues.values.all { it != null }

    companion object {
        fun emptyField(): MutableMap<Int, Char?> {
            return mutableStateMapOf(
                0 to null,
                1 to null,
                2 to null,
                3 to null,
                4 to null,
                5 to null,
                6 to null,
                7 to null,
                8 to null
            )
        }
    }
}
