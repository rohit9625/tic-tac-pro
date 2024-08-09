package com.devx.tictacpro.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun LabeledBox(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ()-> Unit,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 4.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}