package com.devx.tictacpro.data.game

import com.devx.tictacpro.presentation.game.GameState
import kotlinx.serialization.Serializable

@Serializable
data class OnlineGameState(
    val player1: OnlinePlayer? = null,
    val player2: OnlinePlayer? = null,
    val gameCode: String? = null,
    val boardValues: String = "---------",
    val playerAtTurn: String = "X",
    val winner: String? = null,
    val winningLine: List<Int> = emptyList(),
    val draws: Int = 0
)

fun OnlineGameState.toGameState() = GameState(
    player1 = player1?.toPlayer(),
    player2 = player2?.toPlayer(),
    playerAtTurn = playerAtTurn,
    gameCode = gameCode,
    boardValues = boardValues.toBoardMap(),
    winner = winner,
    winningLine = winningLine,
    draws = draws
)

fun GameState.toOnlineGameState() = OnlineGameState(
    player1 = player1?.toOnlinePlayer(),
    player2 = player2?.toOnlinePlayer(),
    playerAtTurn = playerAtTurn,
    gameCode = gameCode,
    boardValues = boardValues.toBoardString(),
    winner = winner,
    winningLine = winningLine,
    draws = draws
)

private fun Map<Int, String?>.toBoardString(): String {
    val boardString = StringBuilder("---------")
    for ((index, value) in this) {
        if (value != null) {
            boardString.setCharAt(index, value[0])
        }
    }
    return boardString.toString()
}

private fun String.toBoardMap(): MutableMap<Int, String?> {
    val boardMap = mutableMapOf<Int, String?>()
    for(i in indices) {
        boardMap[i] = this[i].toString().takeIf { it != "-" }
    }
    return boardMap
}