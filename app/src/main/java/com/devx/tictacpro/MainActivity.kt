package com.devx.tictacpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.presentation.PlayerNavType
import com.devx.tictacpro.presentation.auth.AuthScreen
import com.devx.tictacpro.presentation.auth.AuthViewModel
import com.devx.tictacpro.presentation.game.GameScreen
import com.devx.tictacpro.presentation.game.GameViewModel
import com.devx.tictacpro.presentation.home.HomeScreen
import com.devx.tictacpro.presentation.home.HomeViewModel
import com.devx.tictacpro.presentation.home.ProfileScreen
import com.devx.tictacpro.presentation.home.ProfileViewModel
import com.devx.tictacpro.presentation.home.state.ProfileState
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TicTacProNav(
    navController: NavHostController
) {
    val auth = TicTacProApp.appModule.firebaseAuth
    val startDest = if(auth.currentUser != null) Route.HomeScreen else Route.AuthScreen

    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = startDest) {
            composable<Route.AuthScreen> {
                val viewModel = viewModel<AuthViewModel> {
                    AuthViewModel(
                        TicTacProApp.appModule.userPrefs,
                        TicTacProApp.appModule.authRepository
                    )
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AuthScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onSuccess = { navController.navigate(Route.HomeScreen) {
                        popUpTo<Route.AuthScreen> { inclusive = true }
                    } }
                )
            }

            composable<Route.HomeScreen> {
                val viewModel = viewModel<HomeViewModel>{
                    HomeViewModel(
                        TicTacProApp.appModule.userPrefs,
                        TicTacProApp.appModule.authRepository,
                        TicTacProApp.appModule.gameRepository,
                        TicTacProApp.appModule.drawableResourceMapper
                    )
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                HomeScreen(
                    uiState = uiState,
                    isGuestUser = auth.currentUser?.isAnonymous ?: false,
                    onEvent = viewModel::onEvent,
                    navigateToProfile = {
                        navController.navigate(Route.ProfileScreen(
                            avatar = uiState.avatar!!,
                            name = uiState.name!!,
                            email = auth.currentUser?.email
                        ))
                    },
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }

            composable<Route.ProfileScreen> {
                val args = it.toRoute<Route.ProfileScreen>()
                val viewModel = viewModel<ProfileViewModel> {
                    ProfileViewModel(
                        initialState = ProfileState(
                            avatar = args.avatar,
                            name = args.name,
                            email = args.email
                        ),
                        userPrefs = TicTacProApp.appModule.userPrefs,
                        authRepository = TicTacProApp.appModule.authRepository,
                        resourceMapper = TicTacProApp.appModule.drawableResourceMapper
                    )
                }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                ProfileScreen(
                    uiState = uiState, onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.navigateUp() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }

            composable<Route.Game>(typeMap = mapOf(typeOf<Player?>() to PlayerNavType)) {
                val args = it.toRoute<Route.Game>()
                val viewModel = viewModel<GameViewModel> {
                    GameViewModel(
                        player1 = args.player1, player2 = args.player2,
                        isOnlineGame = args.isOnlineGame,
                        gameRepository = TicTacProApp.appModule.gameRepository
                    )
                }
                val gameState by viewModel.gameState.collectAsStateWithLifecycle()

                GameScreen(
                    gameState = gameState,
                    onEvent = viewModel::onEvent,
                    onNavigateBack = { navController.navigateUp() },
                    myTurn = args.myTurn
                )
            }
        }
    }
}

sealed interface Route {
    @Serializable
    data object AuthScreen: Route

    @Serializable
    data object HomeScreen: Route

    @Serializable
    data class ProfileScreen(val avatar: Int, val name: String, val email: String?): Route

    @Serializable
    data class Game(
        val player1: Player? = null,
        val player2: Player? = null,
        val myTurn: String? = null,
        val isOnlineGame: Boolean = false
    ): Route
}