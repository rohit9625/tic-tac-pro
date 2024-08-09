package com.devx.tictacpro.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PlayerAvatar(
    @DrawableRes image: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: Shape = CircleShape
) {
    OutlinedCard(
        modifier = modifier.size(size),
        shape = shape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = contentDescription,
            modifier = Modifier.aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PlayerAvatar(
    onClick: ()-> Unit,
    @DrawableRes image: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: Shape = CircleShape
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = shape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary)
    ) {
        Image(
            painter = painterResource(image),
            modifier = Modifier.aspectRatio(1f),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )
    }
}