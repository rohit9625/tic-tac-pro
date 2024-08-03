package com.devx.tictacpro.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.components.PlayerAvatar
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
                modifier = Modifier.padding(horizontal = 16.dp)
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

@PreviewLightDark
@Composable
private fun ResultDialogPreview() {
    TicTacProTheme {
        ResultDialog(
            icon = {
                PlayerAvatar(
                    image = R.drawable.boy_avatar1,
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