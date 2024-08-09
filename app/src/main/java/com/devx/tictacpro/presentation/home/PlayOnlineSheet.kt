package com.devx.tictacpro.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.home.event.HomeEvent
import com.devx.tictacpro.presentation.home.state.PlayOnlineState
import com.devx.tictacpro.ui.theme.TicTacProTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayOnlineSheet(
    uiState: PlayOnlineState,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onEvent: (HomeEvent.PlayOnline)-> Unit,
    navigateToGame: (turn: String)-> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        if(uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.error ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Button(
                onClick = { onEvent(HomeEvent.PlayOnline.CreateGame(navigateToGame)) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_game),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = stringResource(R.string.or),
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = uiState.gameCode,
                onValueChange = { onEvent(HomeEvent.PlayOnline.OnGameCodeChange(it)) },
                modifier = Modifier
                    .height(54.dp),
                placeholder = { Text(text = stringResource(R.string.enter_game_code)) },
                singleLine = true,
                trailingIcon = {
                    Button(
                        onClick = { onEvent(HomeEvent.PlayOnline.JoinGame(navigateToGame, uiState.gameCode)) },
                        modifier = Modifier.fillMaxHeight(),
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PlayOnlineSheetPreview() {
    val sheetState = rememberModalBottomSheetState()

    TicTacProTheme {
        PlayOnlineSheet(
            uiState = PlayOnlineState(error = "Please try after some time"),
            sheetState = sheetState,
            onDismissRequest = { },
            navigateToGame = { },
            onEvent = {  }
        )
    }
}