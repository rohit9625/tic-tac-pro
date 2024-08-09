package com.devx.tictacpro.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.home.state.HomeState
import com.devx.tictacpro.ui.theme.TicTacProTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseAvatar(
    avatars: List<Int>,
    onClick: (Int)-> Unit,
    modifier: Modifier = Modifier
) {
    LabeledBox(
        label = "Choose Avatar",
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier.padding(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = 4
        ) {
            avatars.forEach {
                PlayerAvatar(
                    onClick = { onClick(it) },
                    image = it,
                    contentDescription = null,
                    size = 64.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseAvatar(
    avatars: List<Int>,
    onClick: (Int)-> Unit,
    modifier: Modifier = Modifier,
    selectedAvatar: Int? = null,
    avatarSize: Dp = 64.dp
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        maxItemsInEachRow = 4
    ) {
        avatars.forEach {
            Box(modifier = Modifier.size(avatarSize)) {
                PlayerAvatar(
                    onClick = { onClick(it) },
                    image = it,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize()
                )
                if(selectedAvatar == it) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.matchParentSize()
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ChooseAvatarPreview() {
    var avatar by remember { mutableIntStateOf(R.drawable.boy_avatar_1) }

    TicTacProTheme {
        Surface {
            ChooseAvatar(
                selectedAvatar = avatar,
                avatars = HomeState().availableAvatars,
                modifier = Modifier.padding(16.dp),
                onClick = { avatar = it }
            )
        }
    }
}