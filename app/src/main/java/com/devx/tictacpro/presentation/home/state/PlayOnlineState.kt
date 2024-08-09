package com.devx.tictacpro.presentation.home.state

data class PlayOnlineState(
    val isSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val gameCode: String = "",
    val error: String? = null
)