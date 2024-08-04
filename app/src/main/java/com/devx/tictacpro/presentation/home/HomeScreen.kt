package com.devx.tictacpro.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devx.tictacpro.R
import com.devx.tictacpro.Route
import com.devx.tictacpro.TicTacProApp
import com.devx.tictacpro.presentation.components.PlayerAvatar
import com.devx.tictacpro.presentation.components.PlayerSelectionDialog
import com.devx.tictacpro.ui.theme.TicTacProTheme


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = viewModel<HomeViewModel>{
        HomeViewModel(TicTacProApp.appModule.authRepository)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playerSelectionState by viewModel.playerSelectionState.collectAsStateWithLifecycle()

    HomeScreen(
        state = uiState,
        playerSelectionState = playerSelectionState,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    playerSelectionState: PlayerSelectionState,
    onEvent: (HomeEvent)-> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    PlayerAvatar(
                        image = R.drawable.boy_avatar1,
                        contentDescription = "Player"
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onEvent(HomeEvent.OnLogout{
                            navController.navigate(Route.AuthScreen)
                        })
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_logout),
                            contentDescription = "Logout",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Unspecified
                )
            )
        }
    ) { innerPadding->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = stringResource(R.string.app_name).uppercase(),
                    style = MaterialTheme.typography.displaySmall
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Play Online")
                    }

                    Button(onClick = { onEvent(HomeEvent.PlayOffline) }) {
                        Text(text = "Play Offline")
                    }
                }
            }

            if(playerSelectionState.isDialogVisible) {
                PlayerSelectionDialog(
                    firstPlayer = playerSelectionState.firstPlayer,
                    secondPlayer = playerSelectionState.secondPlayer,
                    onNameChange = {id, name->
                        onEvent(HomeEvent.PlayerSelectionEvent.NameChange(id, name))
                    },
                    onTurnSelect = {id, turn->
                        onEvent(HomeEvent.PlayerSelectionEvent.TurnChange(id, turn))
                    },
                    onDismiss = { onEvent(HomeEvent.PlayerSelectionEvent.Dismiss) },
                    onConfirm = {
                        onEvent(HomeEvent.PlayerSelectionEvent.Dismiss)
                        navController.navigate(Route.Game(
                            player1 = playerSelectionState.firstPlayer,
                            player2 = playerSelectionState.secondPlayer
                        ))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    TicTacProTheme {
        HomeScreen(
            state = HomeState(),
            playerSelectionState = PlayerSelectionState(),
            onEvent = {},
            navController = rememberNavController()
        )
    }
}