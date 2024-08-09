package com.devx.tictacpro.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.devx.tictacpro.data.auth.AuthRepositoryImpl
import com.devx.tictacpro.data.game.GameRepositoryImpl
import com.devx.tictacpro.domain.auth.AuthRepository
import com.devx.tictacpro.domain.game.GameRepository
import com.devx.tictacpro.domain.mapper.DrawableResourceMapper
import com.devx.tictacpro.domain.use_case.GenerateGameCode
import com.devx.tictacpro.prefs.UserPrefs
import com.devx.tictacpro.prefs.UserPrefsImpl
import com.devx.tictacpro.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
interface AppModule {
    val firebaseAuth: FirebaseAuth
    val firebaseDatabase: FirebaseDatabase
    val generateGameCode: GenerateGameCode
    val gameRepository: GameRepository
    val dataStore: DataStore<Preferences>
    val drawableResourceMapper: DrawableResourceMapper
    val userPrefs: UserPrefs
    val authRepository: AuthRepository
}

class AppModuleImpl(context: Context): AppModule {
    override val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override val firebaseDatabase: FirebaseDatabase by lazy {
        Firebase.database(Constants.DATABASE_URL)
    }

    override val generateGameCode: GenerateGameCode by lazy {
        GenerateGameCode()
    }

    override val gameRepository: GameRepository by lazy {
        GameRepositoryImpl(firebaseDatabase, generateGameCode, firebaseAuth)
    }

    override val dataStore by lazy { PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ), produceFile = { context.preferencesDataStoreFile(Constants.USER_PREFS)}
        )
    }
    override val drawableResourceMapper: DrawableResourceMapper by lazy {
        DrawableResourceMapper()
    }

    override val userPrefs: UserPrefs by lazy {
        UserPrefsImpl(dataStore)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(firebaseAuth, firebaseDatabase, userPrefs, drawableResourceMapper)
    }
}