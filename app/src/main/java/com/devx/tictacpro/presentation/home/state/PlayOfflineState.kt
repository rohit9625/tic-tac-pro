package com.devx.tictacpro.presentation.home.state

import com.devx.tictacpro.presentation.Player

data class PlayOfflineState(
    val isSheetVisible: Boolean,
    val player1: Player,
    val player2: Player
)
