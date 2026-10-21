package com.gymguia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado

/**
 * Celda de un día en el calendario mensual.
 * - Día de hoy: borde de color primario.
 * - Día seleccionado: círculo relleno.
 * - Día con entrenamiento: punto debajo del número.
 * Si "dia" es null la celda queda vacía (relleno antes del día 1).
 */
@Composable
fun CeldaDiaCalendario(
    dia: Int?,
    entrenado: Boolean,
    esHoy: Boolean,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val esp = MaterialTheme.espaciado
    val colores = MaterialTheme.colorScheme
    Box(
        modifier = modifier.aspectRatio(1f).padding(esp.xs / 2),
        contentAlignment = Alignment.Center,
    ) {
        if (dia == null) return@Box

        val fondo = if (seleccionado) colores.primary else Color.Transparent
        val texto = when {
            seleccionado -> colores.onPrimary
            esHoy -> colores.primary
            else -> colores.onSurface
        }
        val descripcion = if (entrenado) stringResource(R.string.calendario_dia_entrenado, dia)
        else stringResource(R.string.calendario_dia, dia)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(fondo)
                .then(
                    if (esHoy && !seleccionado) Modifier.border(esp.bordeResaltado, colores.primary, CircleShape)
                    else Modifier,
                )
                .clickable(onClick = onClick)
                .semantics {
                    contentDescription = descripcion
                    selected = seleccionado
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = dia.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (esHoy || entrenado) FontWeight.SemiBold else FontWeight.Normal,
                color = texto,
            )
            // El punto de "entrenado" siempre ocupa su espacio para que los números no se muevan.
            Spacer(
                modifier = Modifier
                    .padding(top = esp.xs / 2)
                    .size(esp.marcaDiaEntrenado)
                    .clip(CircleShape)
                    .background(
                        when {
                            !entrenado -> Color.Transparent
                            seleccionado -> colores.onPrimary
                            else -> colores.tertiary
                        },
                    ),
            )
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun CeldaDiaCalendarioPreview() {
    VistaPrevia {
        Row(modifier = Modifier.padding(MaterialTheme.espaciado.l)) {
            CeldaDiaCalendario(4, entrenado = false, esHoy = false, seleccionado = false, onClick = {}, modifier = Modifier.weight(1f))
            CeldaDiaCalendario(5, entrenado = true, esHoy = false, seleccionado = false, onClick = {}, modifier = Modifier.weight(1f))
            CeldaDiaCalendario(6, entrenado = false, esHoy = true, seleccionado = false, onClick = {}, modifier = Modifier.weight(1f))
            CeldaDiaCalendario(7, entrenado = true, esHoy = false, seleccionado = true, onClick = {}, modifier = Modifier.weight(1f))
            CeldaDiaCalendario(null, entrenado = false, esHoy = false, seleccionado = false, onClick = {}, modifier = Modifier.weight(1f))
        }
    }
}
