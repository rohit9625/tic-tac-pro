package com.devx.tictacpro.data.auth

import android.util.Log
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.auth.NetworkError
import com.devx.tictacpro.domain.auth.Result
import com.devx.tictacpro.utils.Constants
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWebException

class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {
    override fun loginAsGuest(): Result<Unit, NetworkError> {
        return try {
            auth.signInAnonymously()

            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(Constants.AUTH_TAG, "Error during anonymous sing-in", e)

            when(e) {
                is FirebaseAuthWebException -> Result.Error(NetworkError.NO_INTERNET)
                is FirebaseTooManyRequestsException -> Result.Error(NetworkError.TOO_MANY_REQUEST)
                else -> Result.Error(NetworkError.UNKNOWN)
            }
        }
    }

    override fun loginWithGoogle() {
        TODO("Not yet implemented")
    }
}