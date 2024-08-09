package com.devx.tictacpro.presentation.game

import androidx.compose.runtime.mutableStateMapOf
import com.devx.tictacpro.presentation.Player
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val player1: Player? = null,
    val player2: Player? = null,
    val gameCode: String? = null,
    val playerAtTurn: String = "X",
    val boardValues: MutableMap<Int, String?> = emptyField(),
    val winner: String? = null,
    var draws: Int = 0,
    val showDialog: Boolean = false
) {
    val isBoardFull: Boolean
        get() = boardValues.values.all { it != null }

    companion object {
        fun emptyField(): MutableMap<Int, String?> {
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
