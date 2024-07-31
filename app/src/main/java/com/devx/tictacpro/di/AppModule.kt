package com.devx.tictacpro.di

import com.devx.tictacpro.data.auth.AuthRepositoryImpl
import com.devx.tictacpro.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth

object AppModule {
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(firebaseAuth)
    }
}