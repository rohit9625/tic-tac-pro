package com.devx.tictacpro.presentation.auth

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.components.AuthTextField
import com.devx.tictacpro.presentation.components.PasswordTextField
import com.devx.tictacpro.presentation.components.TriangleShape
import com.devx.tictacpro.ui.theme.TicTacProTheme

@Composable
fun AuthScreen(
    uiState: AuthState,
    onEvent: (AuthEvent)-> Unit,
    onSuccess: ()-> Unit
) {
    Scaffold { innerPadding->
        Box(modifier = Modifier.padding(innerPadding)) {
            Image(
                painter = painterResource(R.drawable.bg_tic_tac_pro),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(TriangleShape()),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name).uppercase(),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.error ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    AuthTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(AuthEvent.OnEmailChange(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.email),
                        icon = R.drawable.ic_round_mail_24
                    )

                    PasswordTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(AuthEvent.OnPasswordChange(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.password)
                    )

                    Button(
                        onClick = { onEvent(AuthEvent.OnSubmit(onSuccess)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    ) {
                        if(uiState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = stringResource(R.string.lets_go),
                                modifier = Modifier.padding(vertical = 8.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.or),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    AuthButton(
                        icon = R.drawable.ic_person_24,
                        text = stringResource(R.string.continue_as_guest),
                        onClick = { if(!uiState.isLoading) onEvent(AuthEvent.OnGuestLogin(onSuccess)) },
                        iconColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun AuthButton(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.Unspecified,
    onClick: ()-> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = text,
            modifier = Modifier.size(28.dp),
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@PreviewLightDark
@Composable
private fun AuthScreenPreview() {
    TicTacProTheme {
        Surface {
            AuthScreen(
                uiState = AuthState(),
                onEvent = {},
                onSuccess = {}
            )
        }
    }
}