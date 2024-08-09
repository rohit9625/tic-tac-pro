package com.devx.tictacpro.presentation.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.components.ChooseAvatar
import com.devx.tictacpro.presentation.components.PlayerAvatar
import com.devx.tictacpro.presentation.home.event.ProfileEvent
import com.devx.tictacpro.presentation.home.state.ProfileState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ProfileScreen(
    uiState: ProfileState,
    onEvent: (ProfileEvent)-> Unit,
    onNavigateBack: ()-> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Navigate Back"
                            )
                        }
                    }
                )
            },
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "bounds"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = {_,_->
                    spring(0.8f, 360f)
                }
            )
        ) {innerPadding->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerAvatar(
                    image = uiState.avatar,
                    contentDescription = null,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = uiState.avatar),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = {_,_->
                            spring(0.8f, 360f)
                        }
                    ),
                    size = 128.dp
                )

                ChooseAvatar(
                    avatars = uiState.availableAvatars,
                    onClick = { onEvent(ProfileEvent.OnAvatarChange(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { onEvent(ProfileEvent.OnNameChange(it)) },
                        label = {
                            Text(text = stringResource(R.string.name))
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    uiState.email?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = {},
                            label = {
                                Text(text = stringResource(R.string.email))
                            },
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Save")
                }
            }
        }
    }
}