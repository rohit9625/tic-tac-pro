package com.devx.tictacpro.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devx.tictacpro.R
import com.devx.tictacpro.Route
import com.devx.tictacpro.presentation.components.AvatarSelectionDialog
import com.devx.tictacpro.presentation.components.ConfirmationDialog
import com.devx.tictacpro.presentation.components.PlayerAvatar
import com.devx.tictacpro.presentation.home.event.HomeEvent
import com.devx.tictacpro.presentation.home.state.HomeState
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    uiState: HomeState,
    isGuestUser: Boolean,
    navigateToProfile: ()-> Unit = {},
    onEvent: (HomeEvent)-> Unit,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val playOnlineSheetState = rememberModalBottomSheetState()
    val playOfflineSheetState = rememberModalBottomSheetState()
    val snackHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    /** This will be used for avatar selection dialog only **/
    var name by rememberSaveable { mutableStateOf("") }
    var avatar: Int? by rememberSaveable { mutableStateOf(null) }
    var showLogoutConfirmation by remember { mutableStateOf(false) }

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        uiState.name?.let {
                            Text(
                                text = stringResource(R.string.hello_message, it),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    },
                    navigationIcon = {
                        uiState.avatar?.let {
                            IconButton(
                                onClick = navigateToProfile,
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "bounds"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = {_,_->
                                        spring(0.8f, 360f)
                                    }
                                )
                            ) {
                                PlayerAvatar(
                                    image = it,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .sharedElement(
                                            state = rememberSharedContentState(key = it),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            boundsTransform = {_,_->
                                                spring(0.8f, 360f)
                                            }
                                        )
                                )
                            }
                        }
                    },
                    actions = {
                        if(isGuestUser) {
                            Text(
                                text = stringResource(R.string.login),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clickable {
                                        navController.navigate(Route.AuthScreen)
                                    }
                            )
                        } else {
                            IconButton(onClick = { showLogoutConfirmation = true}) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ExitToApp,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackHostState) }
        ) { innerPadding->
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
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = {
                        if(isGuestUser) {
                            scope.launch {
                                val result = snackHostState.showSnackbar(
                                    message = "Please login before playing online",
                                    actionLabel = "Login",
                                    duration = SnackbarDuration.Short
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    navController.navigate(Route.AuthScreen)
                                }
                            }
                        } else {
                            onEvent(HomeEvent.PlayOnline.ShowSheet)
                            scope.launch { playOnlineSheetState.show() }
                        }
                    }) {
                        Text(
                            text = stringResource(R.string.play_online),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Button(onClick = {
                        onEvent(HomeEvent.PlayOffline.ShowSheet)
                        scope.launch { playOfflineSheetState.show() }
                    }) {
                        Text(
                            text = stringResource(R.string.play_offline),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
    if(showLogoutConfirmation) {
        ConfirmationDialog(
            message = stringResource(R.string.logout_confirmation),
            onDismiss = { showLogoutConfirmation = false },
            onConfirm = {
                showLogoutConfirmation = false
                onEvent(HomeEvent.OnLogout {
                    navController.navigate(Route.AuthScreen) {
                        popUpTo<Route.HomeScreen> { inclusive = true }
                    }
                })
            }
        )
    }
    if(uiState.showAvatarSelectionDialog) {
        var isError by remember { mutableStateOf(false) }
        AvatarSelectionDialog(
            name = name,
            onNameChange = { name = it },
            avatars = uiState.availableAvatars,
            selectedAvatar = avatar,
            onAvatarSelect = { avatar = it },
            onDone = {
                isError = name.isBlank() || avatar == null
                if(!isError){
                    onEvent(HomeEvent.OnSaveUserPref(name, avatar!!))
                }
            },
            error = if(isError) stringResource(R.string.avatar_selection_error) else null
        )
    }
    if(uiState.playOnlineState.isSheetVisible) {
        PlayOnlineSheet(
            uiState = uiState.playOnlineState,
            sheetState = playOnlineSheetState,
            onDismissRequest = {
                scope.launch{ playOnlineSheetState.hide() }
                onEvent(HomeEvent.PlayOnline.HideSheet)
            },
            navigateToGame = {turn->
                scope.launch { playOnlineSheetState.hide() }
                onEvent(HomeEvent.PlayOnline.HideSheet)
                navController.navigate(Route.Game(
                    myTurn = turn,
                    isOnlineGame = true
                ))
            },
            onEvent = onEvent
        )
    }
    if(uiState.playOfflineState.isSheetVisible) {
        PlayOfflineSheet(
            uiState = uiState.playOfflineState,
            onEvent = onEvent,
            sheetState = playOfflineSheetState,
            navigateToGame = {
                scope.launch{ playOfflineSheetState.hide() }
                onEvent(HomeEvent.PlayOffline.HideSheet)
                navController.navigate(Route.Game(
                    player1 = uiState.playOfflineState.player1,
                    player2 = uiState.playOfflineState.player2
                ))
            },
            onDismiss = {
                scope.launch{ playOfflineSheetState.hide() }
                onEvent(HomeEvent.PlayOffline.HideSheet)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun HomeScreenPreview() {
    TicTacProTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = 1) {
                it
                HomeScreen(
                    uiState = HomeState(),
                    isGuestUser = true,
                    onEvent = {},
                    navController = rememberNavController(),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}