package com.devx.tictacpro.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devx.tictacpro.R
import com.devx.tictacpro.Route
import com.devx.tictacpro.TicTacProApp
import com.devx.tictacpro.presentation.PlayersDialog
import com.devx.tictacpro.ui.theme.TicTacProTheme


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = viewModel<HomeViewModel>{
        HomeViewModel(TicTacProApp.appModule.authRepository)
    }
    HomeScreen(
        onEvent = viewModel::onEvent,
        navController = navController
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEvent: (HomeEvent)-> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.boy_avatar1),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
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
                    text = "Tic Tac Pro",
                    style = MaterialTheme.typography.displaySmall
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Play Online")
                    }

                    Button(onClick = { showDialog = true }) {
                        Text(text = "Play Offline")
                    }
                }
            }
            if(showDialog) {
                PlayersDialog(
                    onDismiss = { showDialog = false }
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
            onEvent = {},
            navController = rememberNavController()
        )
    }
}