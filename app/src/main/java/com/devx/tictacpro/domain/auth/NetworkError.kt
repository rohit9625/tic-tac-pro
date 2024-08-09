package com.devx.tictacpro.domain.auth

import com.devx.tictacpro.domain.Error

sealed interface NetworkError : Error {
    enum class AuthError: NetworkError {
        INVALID_CREDENTIALS,
        WEAK_PASSWORD,
        EMPTY_FIELDS,
        USER_ALREADY_EXISTS,
        CONNECTION_ERROR,
        SERVER_ERROR,
        TOO_MANY_REQUEST,
        ERROR_UNKNOWN
    }
}