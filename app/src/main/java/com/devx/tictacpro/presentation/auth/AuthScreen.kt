package com.devx.tictacpro.presentation.auth

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    state: AuthState,
    loginAsGuest: ()-> Unit
) {
    val snackHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackHostState)
        }
    ) {innerPadding->
        state.error?.let {
            scope.launch { snackHostState.showSnackbar(it) }
        }
        Box(modifier = Modifier.padding(innerPadding)) {
            Image(
                painter = painterResource(R.drawable.bg_plane_tic_tac_pro_png),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name).uppercase(),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AuthButton(
                        icon = R.drawable.ic_google_icon,
                        text = "Continue with Google",
                        onClick = { /*TODO*/ }
                    )

                    AuthButton(
                        icon = R.drawable.ic_person_24,
                        text = "Continue as Guest",
                        onClick = { if(!state.isLoading) loginAsGuest() },
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
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Continue with Google",
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
                state = AuthState(),
                loginAsGuest = {}
            )
        }
    }
}