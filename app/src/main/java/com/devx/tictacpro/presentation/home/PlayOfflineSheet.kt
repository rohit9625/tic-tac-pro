package com.devx.tictacpro.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.presentation.components.PlayerSelection
import com.devx.tictacpro.presentation.home.event.HomeEvent
import com.devx.tictacpro.presentation.home.state.PlayOfflineState
import com.devx.tictacpro.ui.theme.TicTacProTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayOfflineSheet(
    uiState: PlayOfflineState,
    onEvent: (HomeEvent.PlayOffline)-> Unit,
    sheetState: SheetState,
    navigateToGame: ()-> Unit,
    onDismiss: ()-> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerSelection(
                player = uiState.player1,
                onNameChange = { id, name ->
                    onEvent(HomeEvent.PlayOffline.NameChange(id, name))
                },
                onTurnSelect = {id, turn ->
                    onEvent(HomeEvent.PlayOffline.TurnChange(id, turn))
                },
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider()

            PlayerSelection(
                player = uiState.player2,
                onNameChange = { id, name ->
                    onEvent(HomeEvent.PlayOffline.NameChange(id, name))
                },
                onTurnSelect = {id, turn ->
                    onEvent(HomeEvent.PlayOffline.TurnChange(id, turn))
                },
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = navigateToGame,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(text = "Enter Game", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PlayOfflineSheetPreview() {
    val sheetState = rememberModalBottomSheetState()

    TicTacProTheme {
        PlayOfflineSheet(
            uiState = PlayOfflineState(
                isSheetVisible = false,
                player1 = Player(id = "1", name = "Player 1", avatar = R.drawable.boy_avatar_1, turn = "X"),
                player2 = Player(id = "2", name = "Player 2", avatar = R.drawable.girl_avatar_2, turn = "O")
            ),
            onEvent = {},
            sheetState = sheetState,
            navigateToGame = {},
            onDismiss = { }
        )
    }
}