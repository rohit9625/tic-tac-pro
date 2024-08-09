package com.devx.tictacpro.data.auth

import android.util.Log
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.auth.NetworkError
import com.devx.tictacpro.domain.auth.Result
import com.devx.tictacpro.utils.Constants
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException
import kotlinx.coroutines.CompletableDeferred

class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {
    override fun loginAsGuest(): Result<Unit, NetworkError> {
        return try {
            auth.signInAnonymously()

            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(Constants.AUTH_TAG, "Error during anonymous sing-in", e)

            when(e) {
                is FirebaseAuthWebException -> Result.Error(NetworkError.AuthError.CONNECTION_ERROR)
                is FirebaseTooManyRequestsException -> Result.Error(NetworkError.AuthError.TOO_MANY_REQUEST)
                else -> Result.Error(NetworkError.AuthError.SERVER_ERROR)
            }
        }
    }

    override suspend fun signUpWithEmailPassword(email: String, password: String): Result<Unit, NetworkError> {
        val deferredResult = CompletableDeferred<Result<Unit, NetworkError>>()

        if(email.isBlank() || password.isBlank()) {
            return Result.Error(NetworkError.AuthError.EMPTY_FIELDS)
        }

        return auth.currentUser?.let { user->
            if(user.isAnonymous) {
                val credential = EmailAuthProvider.getCredential(email, password)
                auth.currentUser!!.linkWithCredential(credential).addOnCompleteListener { task->
                    if(task.isSuccessful) {
                        Log.d(Constants.TAG, "LinkWithCredential: success")
                        CoroutineScope(Dispatchers.IO).launch {
                            db.getReference("users").child(task.result.user!!.uid).setValue(
                                User(
                                    email = task.result.user!!.email!!,
                                    name = userPrefs.getName().first(),
                                    avatar = userPrefs.getAvatar().first()?.let {
                                        resourceMapper.getResourceString(it)
                                    }
                                )
                            )
                            userPrefs.setIsNewUser(false)
                            userPrefs.setIsGuestUser(false)
                        }

                        deferredResult.complete(Result.Success(Unit))
                    } else {
                        Log.e(Constants.TAG, "LinkWithCredential: failed", task.exception)
                        if(task.exception is FirebaseAuthUserCollisionException) {
                            deferredResult.complete(Result.Error(NetworkError.AuthError.USER_ALREADY_EXISTS))
                        } else {
                            deferredResult.complete(Result.Error(NetworkError.AuthError.SERVER_ERROR))
                        }
                    }
                }
            }
            deferredResult.await()
        } ?: try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result->
                    db.getReference("users").child(result.user!!.uid).setValue(
                        User(email = result.user!!.email!!)
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        userPrefs.setIsNewUser(true)
                        userPrefs.setIsGuestUser(false)
                    }

                    Log.d(Constants.AUTH_TAG, "User signed-up with ${result.credential?.provider}")
                    deferredResult.complete(Result.Success(Unit))
                }
                .addOnFailureListener { e->
                    Log.e(Constants.AUTH_TAG, "Error signing-up user", e)
                    val error: Result<Unit, NetworkError> = when(e) {
                        is FirebaseNetworkException -> Result.Error(NetworkError.AuthError.CONNECTION_ERROR)
                        is FirebaseAuthWeakPasswordException -> Result.Error(NetworkError.AuthError.WEAK_PASSWORD)
                        is FirebaseAuthUserCollisionException -> Result.Error(NetworkError.AuthError.USER_ALREADY_EXISTS)
                        is FirebaseAuthInvalidCredentialsException -> Result.Error(NetworkError.AuthError.INVALID_CREDENTIALS)
                        is FirebaseTooManyRequestsException -> Result.Error(NetworkError.AuthError.TOO_MANY_REQUEST)

                        else -> Result.Error(NetworkError.AuthError.SERVER_ERROR)
                    }
                    deferredResult.complete(error)
                }

            deferredResult.await()
        } catch (e: Exception) {
            Log.e(Constants.AUTH_TAG, "Error signing-up user", e)
            Result.Error(NetworkError.AuthError.ERROR_UNKNOWN)
        }
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): Result<Unit, NetworkError> {
        val deferredResult = CompletableDeferred<Result<Unit, NetworkError>>()

        if(email.isBlank() || password.isBlank()) {
            return Result.Error(NetworkError.AuthError.EMPTY_FIELDS)
        }

        return try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result->
                    Log.d(Constants.AUTH_TAG, "User signed-in with ${result.credential?.provider}")
                    deferredResult.complete(Result.Success(Unit))
                }
                .addOnFailureListener { e->
                    Log.e(Constants.AUTH_TAG, "Error signing-in user", e)
                    val error: Result<Unit, NetworkError> = when(e) {
                        is FirebaseNetworkException -> Result.Error(NetworkError.AuthError.CONNECTION_ERROR)
                        is FirebaseAuthInvalidCredentialsException -> Result.Error(NetworkError.AuthError.INVALID_CREDENTIALS)
                        is FirebaseTooManyRequestsException -> Result.Error(NetworkError.AuthError.TOO_MANY_REQUEST)

                        else -> Result.Error(NetworkError.AuthError.SERVER_ERROR)
                    }
                    deferredResult.complete(error)
                }

            deferredResult.await()
        } catch (e: Exception) {
            Log.e(Constants.AUTH_TAG, "Error signing-in user", e)
            Result.Error(NetworkError.AuthError.ERROR_UNKNOWN)
        }
    }

    override fun logout() {
        auth.signOut()
    }
}