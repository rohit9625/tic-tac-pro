package com.devx.tictacpro.data.game

import android.util.Log
import com.devx.tictacpro.domain.Result
import com.devx.tictacpro.domain.game.GameError
import com.devx.tictacpro.domain.game.GameRepository
import com.devx.tictacpro.domain.use_case.GenerateGameCode
import com.devx.tictacpro.presentation.game.GameState
import com.devx.tictacpro.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GameRepositoryImpl(
    db: FirebaseDatabase,
    private val generateGameCode: GenerateGameCode,
    private val auth: FirebaseAuth
): GameRepository {
    private val gameRooms = db.getReference("game_rooms")
    private val userRef = db.getReference("users/${auth.currentUser!!.uid}")
    private val _gameState = MutableStateFlow<Result<GameState, GameError>>(Result.Loading)
    private lateinit var gameRef: DatabaseReference
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            CoroutineScope(Dispatchers.IO).launch {
                snapshot.getValue(OnlineGameState::class.java)?.let {
                    _gameState.emit(Result.Success(it.toGameState()))
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
            Log.e(Constants.TAG, "Creation of game cancelled", error.toException())
            CoroutineScope(Dispatchers.IO).launch {
                _gameState.emit(Result.Error(GameError.CONNECTION_LOST))
            }
        }
    }

    override val gameState: StateFlow<Result<GameState, GameError>> = _gameState.asStateFlow()
    
    override suspend fun createGame() {
        val gameCode = generateGameCode()
        try {
            userRef.get().addOnCompleteListener { task->
                if(task.isSuccessful) {
                    task.result.getValue(User::class.java)?.let { user->
                        gameRooms.child(gameCode).setValue(
                            OnlineGameState(
                                player1 = OnlinePlayer(auth.currentUser!!.uid,
                                    name = user.name!!,
                                    avatar = user.avatar!!,
                                    turn = "X"
                                ),
                                gameCode = gameCode
                            )
                        )
                    }
                    gameRef = gameRooms.child(gameCode)
                    gameRef.addValueEventListener(listener)
                }
            }
        } catch(e: Exception) {
            Log.e(Constants.TAG, "Error creating game", e)
            _gameState.emit(Result.Error(GameError.ERROR_UNKNOWN))
        }
    }

    override fun deleteGame() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val myId = auth.currentUser!!.uid
                val player1Id = gameRef.child("player1/id").get().await().getValue<String>()
                val player2Id = gameRef.child("player2/id").get().await().getValue<String>()

                if(player1Id != null && player2Id != null) {
                    val playerToRemove = if(player1Id == myId) "player1" else "player2"
                    gameRef.child(playerToRemove).removeValue().await()
                } else {
                    gameRef.removeValue().await()
                }
            }
            gameRef.removeEventListener(listener)
        } catch(e: Exception) {
            Log.e(Constants.TAG, "Error occurred while deleting game", e)
            throw e
        }
    }

    override suspend fun updateGame(gameState: GameState) {
        try {
            gameRef.setValue(gameState.toOnlineGameState())
        } catch(e: Exception) {
            Log.e(Constants.TAG, "Error occurred while updating game", e)
            _gameState.emit(Result.Error(GameError.ERROR_UNKNOWN))
        }
    }

    override suspend fun joinGame(gameCode: String): Result<Unit, GameError> {
        if(gameCode.isBlank()) {
            return Result.Error(GameError.GAME_CODE_MANDATORY)
        }
        return try {
            // Checking if game room exists associated with gameCode
            val gameRoomSnapshot = gameRooms.child(gameCode).get().await()
            if (!gameRoomSnapshot.exists()) {
                return Result.Error(GameError.GAME_NOT_FOUND)
            }

            userRef.get().addOnCompleteListener { task->
                if(task.isSuccessful) {
                    task.result.getValue(User::class.java)?.let { user->
                        gameRooms.child(gameCode).child("player2").setValue(
                            OnlinePlayer(auth.currentUser!!.uid,
                                name = user.name!!,
                                avatar = user.avatar!!,
                                turn = "O"
                            ),
                        )
                        gameRooms.child(gameCode).child("gameCode").removeValue()
                    }
                    gameRef = gameRooms.child(gameCode)
                    gameRef.addValueEventListener(listener)
                }
            }
            Result.Success(Unit)
        } catch(e: Exception) {
            Log.e(Constants.TAG, "Error occurred while joining game", e)
            Result.Error(GameError.ERROR_UNKNOWN)
        }
    }
}