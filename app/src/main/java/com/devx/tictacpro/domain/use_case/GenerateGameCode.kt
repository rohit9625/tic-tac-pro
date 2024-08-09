package com.devx.tictacpro.domain.use_case

class GenerateGameCode {
    operator fun invoke(): String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..6).map { allowedChars.random() }
            .joinToString("")
    }
}