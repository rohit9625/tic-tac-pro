package com.devx.tictacpro.presentation

import androidx.annotation.DrawableRes

data class Player(
    val name: String,
    @DrawableRes val avatar: Int,
    val turn: Char,
    var score: Int = 0
)
