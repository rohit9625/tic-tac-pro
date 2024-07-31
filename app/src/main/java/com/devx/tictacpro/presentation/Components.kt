package com.devx.tictacpro.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersDialog(
    onDismiss: ()-> Unit
) {

    var player1Turn by remember {
        mutableStateOf('X')
    }

    val player2Turn by remember {
        derivedStateOf {
            if(player1Turn == 'X') 'O'
            else 'X'
        }
    }

    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            PlayerOptions(
                name = player1Name,
                onNameChange = { player1Name = it },
                avatar = R.drawable.boy_avatar1,
                onSelectTurn = { player1Turn = it},
                modifier = Modifier.padding(16.dp),
                turn = player1Turn
            )

            HorizontalDivider()

            PlayerOptions(
                name = player2Name,
                onNameChange = { player2Name = it },
                avatar = R.drawable.boy_avatar1,
                onSelectTurn = {},
                modifier = Modifier.padding(16.dp),
                turn = player2Turn
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Start Game")
                }
            }
        }
    }
}

@Composable
fun PlayerOptions(
    name: String,
    onNameChange: (String) -> Unit,
    @DrawableRes avatar: Int,
    onSelectTurn: (Char)-> Unit,
    modifier: Modifier = Modifier,
    turn: Char? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlayerAvatar(
                image = avatar,
                contentDescription = name
            )

            BasicTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectableIcon(
                icon = {
                    CircleIcon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(10.dp),
                    )
                },
                onClick = { onSelectTurn('O') },
                selected = turn == 'O'
            )

            SelectableIcon(
                icon = {
                    CrossIcon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                    )
                },
                onClick = { onSelectTurn('X') },
                selected = turn == 'X'
            )
        }
    }
}

@Composable
fun PlayerAvatar(
    @DrawableRes image: Int,
    contentDescription: String,
    size: Dp = 48.dp,
    shape: Shape = CircleShape
) {
    Image(
        painter = painterResource(image),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .clip(shape)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SelectableIcon(
    icon: @Composable (BoxScope.() -> Unit),
    selected: Boolean,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = if(selected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}

@Composable
fun CrossIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    strokeWidth: Dp = 4.dp
) {
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height),
            cap = StrokeCap.Round,
            strokeWidth = strokeWidth.toPx()
        )

        drawLine(
            color = color,
            start = Offset(size.width, 0f),
            end = Offset(0f, size.height),
            cap = StrokeCap.Round,
            strokeWidth = strokeWidth.toPx()
        )
    }
}

@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Magenta,
    strokeWidth: Dp = 4.dp
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = color,
            center = Offset(
                this.size.width/2f,
                this.size.height/2f
            ),
            style = Stroke(
                width = strokeWidth.toPx()
            ),
            radius = this.size.width/2f
        )
    }
}

@PreviewLightDark
@Composable
private fun PlayersDialogPreview() {
    PlayersDialog(onDismiss = {})
}