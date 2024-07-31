package com.devx.tictacpro


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devx.tictacpro.presentation.GameScreen
import com.devx.tictacpro.presentation.HomeScreen
import com.devx.tictacpro.presentation.auth.AuthScreen
import com.devx.tictacpro.presentation.auth.AuthViewModel
import com.devx.tictacpro.ui.theme.TicTacProTheme
import com.google.firebase.FirebaseApp
import kotlinx.serialization.Serializable

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
                loginAsGuest = { viewModel.loginAsGuest {
                    navController.navigate(Route.HomeScreen)
                } },
            )
        }

        composable<Route.HomeScreen> {
            HomeScreen()
        }

        composable<Route.GameScreen> {
            GameScreen()
        }
    }
}

sealed interface Route {
    @Serializable
    data object AuthScreen: Route
    @Serializable
    data object HomeScreen: Route
    @Serializable
    data object GameScreen: Route
}