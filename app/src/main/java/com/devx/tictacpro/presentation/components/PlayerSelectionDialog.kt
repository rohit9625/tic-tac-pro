package com.devx.tictacpro.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.ui.theme.TicTacProTheme

@Composable
fun PlayerSelectionDialog(
    firstPlayer: Player,
    secondPlayer: Player,
    onNameChange: (id: Int, name: String) -> Unit,
    onTurnSelect: (id: Int, turn: Char)-> Unit,
    onDismiss: ()-> Unit,
    onConfirm: ()-> Unit,
    onAvatarChange: (id: Int, avatar: Int)-> Unit = {_,_->},
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            PlayerSelection(
                player = firstPlayer,
                onNameChange = onNameChange,
                onTurnSelect = onTurnSelect,
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider()

            PlayerSelection(
                player = secondPlayer,
                onNameChange = onNameChange,
                onTurnSelect = onTurnSelect,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = onConfirm,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(text = "Enter Game")
            }
        }
    }
}

@Composable
fun PlayerSelection(
    player: Player,
    onNameChange: (id: Int, name: String) -> Unit,
    onTurnSelect: (id: Int, turn: Char)-> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlayerAvatar(
                image = player.avatar,
                contentDescription = player.name
            )

            UnderlinedTextField(
                value = player.name,
                onValueChange = { onNameChange(player.id, it) }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectableIcon(
                icon = {
                    CircleIcon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(10.dp),
                    )
                },
                onClick = { onTurnSelect(player.id, 'O') },
                selected = player.turn == 'O'
            )

            SelectableIcon(
                icon = {
                    CrossIcon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                    )
                },
                onClick = { onTurnSelect(player.id, 'X') },
                selected = player.turn == 'X'
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PlayerSelectionDialogPreview() {
    val player1 = Player(
        id = 1,
        name = "Player 1",
        avatar = R.drawable.boy_avatar1,
        turn = 'X'
    )
    val player2 = Player(
        id = 2,
        name = "Player 1",
        avatar = R.drawable.boy_avatar_2,
        'O'
    )
    TicTacProTheme {
        PlayerSelectionDialog(
            firstPlayer = player1,
            secondPlayer = player2,
            onNameChange = {_,_->},
            onTurnSelect = {_,_->},
            onDismiss = {},
            onConfirm = {}
        )
    }
}