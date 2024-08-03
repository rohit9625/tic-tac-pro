package com.devx.tictacpro.domain.mapper

import com.devx.tictacpro.domain.auth.NetworkError

object AuthErrorMapper {
    fun mapToMessage(error: NetworkError): String {
        return when(error) {
            NetworkError.AuthError.EMPTY_FIELDS -> "All fields are mandatory"
            NetworkError.AuthError.CONNECTION_ERROR -> "Internet not available"
            NetworkError.AuthError.INVALID_CREDENTIALS -> "Invalid user credentials"
            NetworkError.AuthError.WEAK_PASSWORD -> "Password is too weak"
            NetworkError.AuthError.USER_ALREADY_EXISTS -> "User already exists"
            NetworkError.AuthError.SERVER_ERROR -> "Internal server error"
            NetworkError.AuthError.TOO_MANY_REQUEST -> "Please try after some time"
            NetworkError.AuthError.ERROR_UNKNOWN -> "An unexpected error occurred"
        }
    }
}