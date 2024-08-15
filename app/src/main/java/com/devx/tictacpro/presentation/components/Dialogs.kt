package com.devx.tictacpro.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.home.state.HomeState
import com.devx.tictacpro.ui.theme.TicTacProTheme

@Composable
fun ResultDialog(
    icon: @Composable ()-> Unit,
    message: String,
    onDismiss: ()-> Unit,
    onConfirm: ()-> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                text = "Retry",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { onConfirm() },
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = modifier,
        dismissButton = {
            Text(
                text = "Exit",
                modifier = Modifier.clickable { onDismiss() },
                style = MaterialTheme.typography.titleMedium
            )
        },
        icon = icon,
        text = {
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        title = {
            Text(
                text = supportingText ?: "",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    )
}

@Composable
fun AvatarSelectionDialog(
    name: String,
    onNameChange: (String)-> Unit,
    avatars: List<Int>,
    onAvatarSelect: (Int)-> Unit,
    selectedAvatar: Int? = null,
    error: String? = null,
    onDone: ()-> Unit
) {
    Dialog(
        onDismissRequest = { TODO("Not Needed") },
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.complete_your_profile),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            ChooseAvatar(
                avatars = avatars,
                onClick = onAvatarSelect,
                selectedAvatar = selectedAvatar,
                avatarSize = 54.dp
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.height(50.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_your_name),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                Text(
                    text = error ?: "",
                    modifier = Modifier.align(Alignment.End),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(onClick = onDone) {
                Text(text = stringResource(R.string.done))
            }
        }
    }
}

@Composable
fun GameCodeDialog(
    gameCode: String,
    onAction: ()-> Unit,
    onDismissAction: () -> Unit
) {
    Dialog(
        onDismissRequest = { TODO("Not Needed") },
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(32.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.waiting_for_other_player),
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.your_game_code),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = gameCode,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primaryContainer
            )

           Row(
               modifier = Modifier.padding(top = 16.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(16.dp)
           ) {
               OutlinedButton(onClick = onDismissAction) {
                   Text(
                       text = stringResource(R.string.exit),
                       style = MaterialTheme.typography.titleMedium
                   )
               }
               Button(onClick = onAction) {
                   Text(
                       text = stringResource(R.string.share),
                       style = MaterialTheme.typography.titleMedium
                   )
               }
           }
        }
    }
}

@Composable
fun ConfirmationDialog(
    message: String,
    onDismiss: ()-> Unit,
    onConfirm: ()-> Unit
) {
    AlertDialog(
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                text = stringResource(R.string.yes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onConfirm() }
            )
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { onDismiss() }
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun GameDialogPreview() {
    TicTacProTheme {
        GameCodeDialog(
            gameCode = "ABC123",
            onAction = { /*TODO*/ },
            onDismissAction = {}
        )
    }
}

@Preview
@Composable
private fun AvatarSelectionDialogPreview() {
    var name by remember {
        mutableStateOf("")
    }
    var avatar: Int? by remember {
        mutableStateOf(null)
    }
    TicTacProTheme {
        AvatarSelectionDialog(
            selectedAvatar = avatar,
            name = name,
            onNameChange = { name = it },
            avatars = HomeState().availableAvatars,
            onAvatarSelect = { avatar = it },
            onDone = {},
            error = "Enter your name and select avatar"
        )
    }
}

@Preview
@Composable
private fun ResultDialogPreview() {
    TicTacProTheme {
        ResultDialog(
            icon = {
                PlayerAvatar(
                    image = R.drawable.boy_avatar_1,
                    contentDescription = "Player 1",
                    size = 64.dp
                )
            },
            message = "Winner!!",
            supportingText = "Player 1",
            onDismiss = { /*TODO*/ },
            onConfirm = {}
        )
    }
}