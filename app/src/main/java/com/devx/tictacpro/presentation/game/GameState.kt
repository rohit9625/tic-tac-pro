package com.devx.tictacpro.presentation.game

import androidx.compose.runtime.mutableStateMapOf
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player

data class GameState(
    val player1: Player,
    val player2: Player,
    val playerAtTurn: Char? = 'X',
    val boardValues: MutableMap<Int, Char?> = emptyField(),
    val winningPlayer: Char? = null,
    var draw: Int = 0,
    val isDraw: Boolean = false
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
