package com.devx.tictacpro.presentation.home.state

import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player

data class HomeState(
    val name: String? = null,
    val avatar: Int? = null,
    val showAvatarSelectionDialog: Boolean = false,
    val playOnlineState: PlayOnlineState = PlayOnlineState(),
    val playOfflineState: PlayOfflineState = offlineState,
    val availableAvatars: List<Int> = avatars
) {
    companion object {
        private val offlineState = PlayOfflineState(
            isSheetVisible = false,
            player1 = Player(id = "1", name = "Player 1", avatar = R.drawable.boy_avatar_1, turn = "X"),
            player2 = Player(id = "2", name = "Player 2", avatar = R.drawable.girl_avatar_2, turn = "O")
        )

        private val avatars = listOf(
            R.drawable.boy_avatar_1,
            R.drawable.boy_avatar_2,
            R.drawable.boy_avatar_3,
            R.drawable.boy_avatar_4,
            R.drawable.girl_avatar_1,
            R.drawable.girl_avatar_2,
            R.drawable.girl_avatar_3,
            R.drawable.girl_avatar_4
        )
    }
}