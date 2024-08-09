package com.devx.tictacpro.presentation.game

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.R
import com.devx.tictacpro.presentation.Player
import com.devx.tictacpro.presentation.components.CircleIcon
import com.devx.tictacpro.presentation.components.ConfirmationDialog
import com.devx.tictacpro.presentation.components.CrossIcon
import com.devx.tictacpro.presentation.components.GameCodeDialog
import com.devx.tictacpro.presentation.components.PlayerAvatar
import com.devx.tictacpro.presentation.components.ResultDialog
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    gameState: GameState,
    onEvent: (GameEvent)-> Unit,
    onNavigateBack: ()-> Unit = {},
    myTurn: String? = null
) {
    var showExitConfirmation by remember { mutableStateOf(false) }
    var isBackPressedTwice by remember { mutableStateOf(false) }
    var showGameCodeDialog by remember { mutableStateOf(true) }
    val context = LocalContext.current
    BackHandler {
        if(isBackPressedTwice) {
            showExitConfirmation = true
            return@BackHandler
        }
        isBackPressedTwice = true
        Toast.makeText(context, "Press Back again to exit the game", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ isBackPressedTwice = false }, 2000)
    }

    if(showExitConfirmation) {
        ConfirmationDialog(
            message = stringResource(R.string.game_exit_confirmation),
            onDismiss = { showExitConfirmation = false },
            onConfirm = {
                showExitConfirmation = false
                onNavigateBack()
            }
        )
    }

    Scaffold { innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                gameState.player1?.let {
                    PlayerStatus(player = it, modifier = Modifier.weight(0.3f))
                } ?: Spacer(modifier = Modifier.weight(0.3f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.draw),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${gameState.draws}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                gameState.player2?.let {
                    PlayerStatus(player = it, modifier = Modifier.weight(0.3f))
                } ?: Spacer(modifier = Modifier.weight(0.3f))
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(gameState.player1 != null && gameState.player2 != null) {
                    Text(
                        text = stringResource(R.string.yours_turn,
                            if(gameState.playerAtTurn == gameState.player1.turn) gameState.player1.name
                            else gameState.player2.name
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = stringResource(R.string.other_player_left),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                TurnIndicator(
                    turn = gameState.playerAtTurn,
                    strokeWidth = 8.dp,
                    innerPadding = 16.dp,
                    borderWidth = 2.dp
                )
            }

            TicTacToeField(
                state = gameState,
                onTap = { pos->
                    if(gameState.winner == null) {
                        myTurn?.let {
                            if(it == gameState.playerAtTurn) onEvent(GameEvent.UpdateGame(pos))
                        } ?: onEvent(GameEvent.UpdateGame(pos))
                    }
                },
                modifier = Modifier
                    .padding(32.dp)
            )

            Button(
                onClick = { onEvent(GameEvent.ResetGame) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = stringResource(R.string.reset))
            }
        }
    }

    if(gameState.gameCode != null && showGameCodeDialog) {
        GameCodeDialog(
            gameCode = gameState.gameCode,
            onAction = {},
            onDismissAction = {
                showGameCodeDialog = false
                onNavigateBack()
            }
        )
    }

    if(gameState.winner != null && gameState.showDialog) {
        ResultDialog(
            icon = {
                PlayerAvatar(
                    image = if(gameState.winner == gameState.player1?.turn)
                                gameState.player1.avatar
                            else gameState.player2!!.avatar,
                    contentDescription = null,
                    size = 64.dp
                )
            },
            message = stringResource(R.string.winner),
            supportingText = if(gameState.winner == gameState.player1?.turn)
                                gameState.player1.name
                            else gameState.player2?.name,
            onDismiss = { onEvent(GameEvent.DismissDialog) },
            onConfirm = { onEvent(GameEvent.ResetGame) }
        )
    }

    if(gameState.isBoardFull && gameState.winner == null) {
        ResultDialog(
            icon = {
                Image(
                    painter = painterResource(R.drawable.ic_handshake),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
            },
            message = stringResource(R.string.draw),
            supportingText = "It's a",
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
            text = stringResource(R.string.score, player.score),
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
    turn: String,
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
        if(turn == "X") {
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

                    if (state.boardValues[pos] == "X") {
                        AnimatedCross(
                            color = playerXColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            strokeWidth = 8.dp
                        )
                    } else if(state.boardValues[pos] == "O") {
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
    val lineLength = remember { Animatable(0f) }
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
                    id = "1",
                    name = "Player 1",
                    turn = "X",
                    avatar = R.drawable.boy_avatar_1
                ),
                player2 = Player(
                    id = "2",
                    name = "Player 2",
                    turn = "O",
                    avatar = R.drawable.boy_avatar_2
                )
            ),
            onEvent = {}
        )
    }
}