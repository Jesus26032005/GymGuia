package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.domain.model.NivelDificultad
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.theme.extra
import com.gymguia.app.ui.util.etiqueta

/** Etiqueta pequeña con color según el nivel: verde, ámbar o rojo apagado. */
@Composable
fun EtiquetaDificultad(nivel: NivelDificultad, modifier: Modifier = Modifier) {
    val colores = when (nivel) {
        NivelDificultad.PRINCIPIANTE -> MaterialTheme.extra.principiante
        NivelDificultad.INTERMEDIO -> MaterialTheme.extra.intermedio
        NivelDificultad.AVANZADO -> MaterialTheme.extra.avanzado
    }
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = colores.fondo,
        contentColor = colores.texto,
    ) {
        Text(
            text = stringResource(nivel.etiqueta),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.espaciado.s,
                vertical = MaterialTheme.espaciado.xs / 2,
            ),
        )
    }
}

@PreviewClaroOscuro
@Composable
private fun EtiquetaDificultadPreview() {
    VistaPrevia {
        Row(modifier = Modifier.padding(MaterialTheme.espaciado.l)) {
            NivelDificultad.entries.forEach {
                EtiquetaDificultad(it, Modifier.padding(end = MaterialTheme.espaciado.s))
            }
        }
    }
}
