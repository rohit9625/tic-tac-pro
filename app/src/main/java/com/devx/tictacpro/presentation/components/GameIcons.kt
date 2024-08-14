package com.devx.tictacpro.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devx.tictacpro.ui.theme.TicTacProTheme
import kotlinx.coroutines.launch

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
            containerColor = if(selected) MaterialTheme.colorScheme.secondaryContainer
            else Color.Transparent,
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondaryContainer)
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
private fun SelectableIconPreview() {
    var isSelected by remember { mutableStateOf(false) }

    TicTacProTheme {
        Surface {
            SelectableIcon(
                icon = {
                    CircleIcon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(10.dp),
                    )
                },
                selected = isSelected,
                onClick = { isSelected = !isSelected },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}