package com.devx.tictacpro.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.presentation.ResultDialog
import com.devx.tictacpro.presentation.components.CircleIcon
import com.devx.tictacpro.presentation.components.CrossIcon
import com.devx.tictacpro.presentation.components.PlayerAvatar
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    gameState: GameState,
    onEvent: (GameEvent)-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerStatus(
                player = gameState.player1
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Draw",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${gameState.draw}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            PlayerStatus(
                player = gameState.player2
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TurnIndicator(
                turn = gameState.playerAtTurn,
                strokeWidth = 8.dp,
                innerPadding = 16.dp,
                borderWidth = 2.dp
            )
            Text(
                text = "Turn",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TicTacToeField(
            state = gameState,
            onTap = { onEvent(GameEvent.UpdateGame(it)) },
            modifier = Modifier
                .padding(32.dp)
        )

        Button(
            onClick = { onEvent(GameEvent.ResetGame) },
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(text = "Retry")
        }
    }

    if(gameState.winningPlayer != null) {
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
            onDismiss = { onEvent(GameEvent.ResetGame) },
            onConfirm = { onEvent(GameEvent.ResetGame) }
        )
    }

    if(gameState.isDraw) {
        ResultDialog(
            icon = {
                PlayerAvatar(
                    image = R.drawable.boy_avatar1,
                    contentDescription = "Draw",
                    size = 64.dp
                )
            },
            supportingText = "It's a",
            message = "Draw",
            onDismiss = { onEvent(GameEvent.DismissDialog) },
            onConfirm = { onEvent(GameEvent.ResetGame) }
        )
    }
}

@Composable
fun PlayerStatus(
    player: Player,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerAvatar(
            image = player.avatar,
            contentDescription = player.name
        )
        Text(
            text = player.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Score: ${player.score}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        TurnIndicator(
            turn = player.turn,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun TurnIndicator(
    turn: Char?,
    modifier: Modifier = Modifier,
    innerPadding: Dp = 12.dp,
    strokeWidth: Dp = 4.dp,
    borderWidth: Dp = 1.dp
) {
    Card(
        modifier = modifier.size(64.dp),
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.primaryContainer),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        if(turn == 'X') {
            CrossIcon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                strokeWidth = strokeWidth
            )
        } else {
            CircleIcon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                strokeWidth = strokeWidth
            )
        }
    }
}

@Composable
fun TicTacToeField(
    state: GameState,
    onTap: (Int)-> Unit,
    modifier: Modifier = Modifier,
    playerXColor: Color = Color.Blue,
    playerOColor: Color = Color.Magenta
) {
    val interactionSource = remember { MutableInteractionSource() }
    val winningPoints = remember {
        mutableStateListOf<Dp>()
    }

    BoxWithConstraints(modifier = modifier
        .aspectRatio(1f)
        .size(350.dp)
    ) {
        GameBoard(
            strokeWidth = 8.dp,
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(state.boardValues.size) { pos->
                BoxWithConstraints(
                    modifier = Modifier
                        .size(maxWidth / 3)
                        .clickable(interactionSource, indication = null) {
                            onTap(pos)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    for(i in 0..2) {
                        winningPoints.add(maxWidth/2)
                    }

                    if (state.boardValues[pos] == 'X') {
                        AnimatedCross(
                            color = playerXColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            strokeWidth = 8.dp
                        )
                    } else if(state.boardValues[pos] == 'O') {
                        AnimatedCircle(
                            color = playerOColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            strokeWidth = 8.dp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    animationDuration: Int = 500,
    color: Color = Color.Black,
    strokeWidth: Dp = 4.dp
) {
    val lineLength = remember { Animatable(1f) }
    LaunchedEffect(key1 = Unit) {
        lineLength.animateTo(targetValue = 1f, animationSpec = tween(animationDuration))
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        for (i in 1..2) {
            drawLine(
                color = color,
                start = Offset(
                    i*(width/3), 0f
                ),
                end = Offset(
                    i*(width/3), height * lineLength.value
                ),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = color,
                start = Offset(
                    0f, i*(height/3)
                ),
                end = Offset(
                    width * lineLength.value, i*(height/3)
                ),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun AnimatedCross(
    color: Color,
    modifier: Modifier = Modifier,
    animationDuration: Int = 500,
    strokeWidth: Dp = 4.dp
) {
    val lineLength = remember { Animatable(0f) }
    val line2Length = remember { Animatable(1f) }

    LaunchedEffect(key1 = Unit) {
        launch { lineLength.animateTo(1f, tween(animationDuration)) }
        launch { line2Length.animateTo(0f, tween(animationDuration)) }
    }

    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height) * lineLength.value,
            strokeWidth = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width, 0f),
            end = Offset(size.width * line2Length.value, size.height * lineLength.value),
            strokeWidth = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun AnimatedCircle(
    color: Color,
    modifier: Modifier = Modifier,
    animationDuration: Int = 500,
    strokeWidth: Dp = 4.dp,
) {
    val sweepAngle = remember { Animatable(0f) }
    LaunchedEffect(key1 = Unit) {
        sweepAngle.animateTo(360f, tween(animationDuration))
    }

    Canvas(modifier = modifier) {
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = sweepAngle.value,
            topLeft = Offset(
                0f,0f
            ),
            useCenter = false,
            size = this.size,
            style = Stroke(
                width = strokeWidth.toPx()
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun GameScreenPreview() {
    TicTacProTheme {
        GameScreen(
            gameState = GameState(
                player1 = Player(
                    id = 1,
                    name = "Player 1",
                    turn = 'X',
                    avatar = R.drawable.boy_avatar1
                ),
                player2 = Player(
                    id = 2,
                    name = "Player 2",
                    turn = 'O',
                    avatar = R.drawable.boy_avatar_2
                )
            ),
            onEvent = {}
        )
    }
}