package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.gymguia.app.ui.theme.espaciado

/** Mensaje centrado con ícono para cuando una lista no tiene contenido. */
@Composable
fun EstadoVacio(
    icono: ImageVector,
    titulo: String,
    mensaje: String,
    modifier: Modifier = Modifier,
) {
    val esp = MaterialTheme.espaciado
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = esp.xl, vertical = esp.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(esp.s),
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            modifier = Modifier.size(esp.iconoXl),
            tint = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewClaroOscuro
@Composable
private fun EstadoVacioPreview() {
    VistaPrevia {
        EstadoVacio(
            icono = Icons.Outlined.FavoriteBorder,
            titulo = stringResource(R.string.vacio_favoritos_titulo),
            mensaje = stringResource(R.string.vacio_favoritos_mensaje),
        )
    }
}
