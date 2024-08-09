package com.devx.tictacpro.data.game

import com.devx.tictacpro.TicTacProApp
import com.devx.tictacpro.presentation.Player
import kotlinx.serialization.Serializable

@Serializable
data class OnlinePlayer(
    val id: String = "",
    val name: String = "",
    val avatar: String = "",
    val turn: String = "",
    val score: Int = 0
)

val resourceMapper = TicTacProApp.appModule.drawableResourceMapper
fun OnlinePlayer.toPlayer() = Player(
    id = id,
    name = name,
    avatar = resourceMapper.getResourceId(avatar)!!,
    turn = turn,
    score = score
)

fun Player.toOnlinePlayer() = OnlinePlayer(
    id = id,
    name = name,
    avatar = resourceMapper.getResourceString(avatar)!!,
    turn = turn,
    score = score
)

