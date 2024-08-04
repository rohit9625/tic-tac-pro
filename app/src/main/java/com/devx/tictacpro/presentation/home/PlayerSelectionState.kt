package com.devx.tictacpro.presentation.home

import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player

data class PlayerSelectionState(
    val isDialogVisible: Boolean = false,
    val firstPlayer: Player = Player(id = 1, name = "Player 1", avatar = R.drawable.boy_avatar1, turn = 'X'),
    val secondPlayer: Player = Player(id = 2, name = "Player 2", avatar = R.drawable.boy_avatar_2, turn = 'O')
)
