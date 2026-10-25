package com.gymguia.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/*
 * Formas de la identidad "Placa": esquinas casi rectas, como una placa de metal.
 * Tarjetas usan "medium", etiquetas "small", hojas y diálogos "extraLarge".
 */
internal val Formas = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(12.dp),
)
