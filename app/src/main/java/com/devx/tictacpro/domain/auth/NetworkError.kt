package com.devx.tictacpro.domain.auth

enum class NetworkError : Error{
    NO_INTERNET,
    UNKNOWN,
    TOO_MANY_REQUEST
}