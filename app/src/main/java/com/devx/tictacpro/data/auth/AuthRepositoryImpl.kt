package com.devx.tictacpro.data.auth

import android.util.Log
import com.devx.tictacpro.data.game.User
import com.devx.tictacpro.domain.Result
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.auth.NetworkError
import com.devx.tictacpro.domain.mapper.DrawableResourceMapper
import com.devx.tictacpro.prefs.UserPrefs
import com.devx.tictacpro.utils.Constants
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val userPrefs: UserPrefs,
    private val resourceMapper: DrawableResourceMapper
): AuthRepository {
    override suspend fun loginAsGuest(): Result<Unit, NetworkError> {
        val deferredResult = CompletableDeferred<Result<Unit, NetworkError>>()
        return try {
            auth.signInAnonymously().addOnCompleteListener { task->
                if(task.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch {
                        userPrefs.setIsGuestUser(true)
                        userPrefs.setIsNewUser(true)
                    }
                    deferredResult.complete(Result.Success(Unit))
                } else {
                    Log.e(Constants.AUTH_TAG, "Error login user anonymously", task.exception)
                    deferredResult.complete(Result.Error(NetworkError.AuthError.SERVER_ERROR))
                }
            }
            deferredResult.await()
        } catch (e: Exception) {
            Log.e(Constants.AUTH_TAG, "Error during anonymous sing-in", e)
            Result.Error(NetworkError.AuthError.ERROR_UNKNOWN)
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
                        when(task.exception) {
                            is FirebaseAuthWeakPasswordException -> {
                                deferredResult.complete(Result.Error(NetworkError.AuthError.WEAK_PASSWORD))
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                deferredResult.complete(Result.Error(NetworkError.AuthError.INVALID_CREDENTIALS))
                            }
                            is FirebaseAuthUserCollisionException -> {
                                deferredResult.complete(Result.Error(NetworkError.AuthError.USER_ALREADY_EXISTS))
                            }
                            else -> deferredResult.complete(Result.Error(NetworkError.AuthError.SERVER_ERROR))
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
            Log.e(Constants.AUTH_TAG, "Unknown error occurred while signing-up user", e)
            Result.Error(NetworkError.AuthError.ERROR_UNKNOWN)
        }
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): Result<User, NetworkError> {
        val deferredResult = CompletableDeferred<Result<User, NetworkError>>()

        if(email.isBlank() || password.isBlank()) {
            return Result.Error(NetworkError.AuthError.EMPTY_FIELDS)
        }
        return try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result->
                    val userRef = db.getReference("users/${result.user!!.uid}")

                    userRef.get().addOnSuccessListener { dataSnapshot->
                        if(dataSnapshot.exists()) {
                            val user = dataSnapshot.getValue(User::class.java) as User
                            CoroutineScope(Dispatchers.IO).launch {
                                userPrefs.setName(user.name!!)
                                userPrefs.setAvatar(resourceMapper.getResourceId(user.avatar!!)!!)
                                userPrefs.setIsNewUser(false)
                                userPrefs.setIsGuestUser(false)
                            }
                            deferredResult.complete(Result.Success(user))
                        }
                    }.addOnFailureListener { e->
                        Log.e(Constants.AUTH_TAG, "Error getting user information", e)
                        deferredResult.complete(Result.Error(NetworkError.AuthError.ERROR_UNKNOWN))
                    }
                    Log.d(Constants.AUTH_TAG, "User signed-in with ${result.credential?.provider}")
                }
                .addOnFailureListener { e->
                    Log.e(Constants.AUTH_TAG, "Error signing-in user", e)
                    val error: Result<User, NetworkError> = when(e) {
                        is FirebaseNetworkException -> Result.Error(NetworkError.AuthError.CONNECTION_ERROR)
                        is FirebaseAuthInvalidCredentialsException -> Result.Error(NetworkError.AuthError.INVALID_CREDENTIALS)
                        is FirebaseTooManyRequestsException -> Result.Error(NetworkError.AuthError.TOO_MANY_REQUEST)

                        else -> Result.Error(NetworkError.AuthError.SERVER_ERROR)
                    }
                    deferredResult.complete(error)
                }

            deferredResult.await()
        } catch (e: Exception) {
            Log.e(Constants.AUTH_TAG, "Unknown error occurred while signing-in user", e)
            Result.Error(NetworkError.AuthError.ERROR_UNKNOWN)
        }
    }

    override suspend fun updateProfile(name: String, avatar: String): Result<Unit, NetworkError> {
        return try {
            val ref = db.getReference("users/${auth.currentUser!!.uid}")
            ref.child("name").setValue(name)
            ref.child("avatar").setValue(avatar)

            Result.Success(Unit)
        } catch(e: Exception) {
            Log.e(Constants.AUTH_TAG, "Error updating user profile", e)
            Result.Error(NetworkError.AuthError.ERROR_UNKNOWN)
        }
    }

    override fun logout() {
        auth.signOut()
    }
}