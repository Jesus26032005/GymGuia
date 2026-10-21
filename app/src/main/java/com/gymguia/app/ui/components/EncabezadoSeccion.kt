package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado

/** Título de sección con una acción opcional a la derecha (por ejemplo "Ver todos"). */
@Composable
fun EncabezadoSeccion(
    titulo: String,
    modifier: Modifier = Modifier,
    textoAccion: String? = null,
    onAccion: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.espaciado.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (textoAccion != null) {
            TextButton(onClick = onAccion) { Text(textoAccion) }
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun EncabezadoSeccionPreview() {
    VistaPrevia {
        EncabezadoSeccion(
            titulo = stringResource(R.string.inicio_grupos_musculares),
            textoAccion = stringResource(R.string.accion_mostrar),
            modifier = Modifier.padding(horizontal = MaterialTheme.espaciado.l),
        )
    }
}
