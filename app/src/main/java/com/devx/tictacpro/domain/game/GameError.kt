package com.devx.tictacpro.domain.game

import com.devx.tictacpro.domain.Error

enum class GameError: Error {
    SERVER_ERROR,
    CONNECTION_LOST,
    REQUEST_TIMEOUT,
    GAME_NOT_FOUND,
    ERROR_UNKNOWN,
    GAME_CODE_MANDATORY
}