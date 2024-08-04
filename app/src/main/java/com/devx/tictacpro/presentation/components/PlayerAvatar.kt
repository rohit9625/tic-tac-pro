package com.devx.tictacpro.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
                color = MaterialTheme.colorScheme.tertiary,
                shape = CircleShape
            ),
        contentScale = ContentScale.Crop
    )
}