package com.devx.tictacpro.presentation.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class TriangleShape: Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            lineTo(0f, size.height/3f)
            lineTo(size.width/2f, size.height/2.3f)
            lineTo(size.width, size.height/3f)
            lineTo(size.width, 0f)
        }

        return Outline.Generic(path)
    }
}