package com.devx.tictacpro


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.presentation.PlayerNavType
import com.devx.tictacpro.presentation.game.GameScreen
import com.devx.tictacpro.presentation.home.HomeScreen
import com.devx.tictacpro.presentation.auth.AuthScreen
import com.devx.tictacpro.presentation.auth.AuthViewModel
import com.devx.tictacpro.presentation.game.GameViewModel
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacProTheme {
                TicTacProNav(
                    navController = rememberNavController()
                )
            }
        }
    }
}

@Composable
fun TicTacProNav(
    navController: NavHostController
) {
    val auth = TicTacProApp.appModule.firebaseAuth
    val startDest = if(auth.currentUser != null) Route.HomeScreen else Route.AuthScreen

    NavHost(navController = navController, startDestination = startDest) {
        composable<Route.AuthScreen> {
            val viewModel = viewModel<AuthViewModel> {
                AuthViewModel(TicTacProApp.appModule.authRepository)
            }
            val uiState by viewModel.uiState.collectAsState()

            AuthScreen(
                state = uiState,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<Route.HomeScreen> {
            HomeScreen(navController)
        }

        composable<Route.Game>(
            typeMap = mapOf(typeOf<Player>() to PlayerNavType)
        ) {
            val args = it.toRoute<Route.Game>()
            val viewModel = viewModel<GameViewModel>() {
                GameViewModel(player1 = args.player1, player2 = args.player2)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            GameScreen(
                gameState = uiState,
                onEvent = viewModel::onEvent
            )
        }
    }
}

sealed interface Route {
    @Serializable
    data object AuthScreen: Route

    @Serializable
    data object HomeScreen: Route

    @Serializable
    data class Game(val player1: Player, val player2: Player): Route
}