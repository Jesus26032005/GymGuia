package com.gymguia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.Ejercicio
import com.gymguia.app.ui.components.mapa.MapaMuscular
import com.gymguia.app.ui.components.mapa.vistaMasRelevante
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.domain.util.capitalizado

/**
 * Tarjeta de un ejercicio en listas: miniatura, nombre, músculo y equipo,
 * nivel y botón de favorito.
 */
@Composable
fun TarjetaEjercicio(
    ejercicio: Ejercicio,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val esp = MaterialTheme.espaciado
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            modifier = Modifier.padding(esp.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(esp.m),
        ) {
            if (ejercicio.imagenes.isEmpty()) {
                // Sin fotos: una miniatura del mapa muscular (la vista donde más se nota el trabajo).
                Box(
                    modifier = Modifier
                        .size(esp.miniaturaEjercicio)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(esp.xs),
                    contentAlignment = Alignment.Center,
                ) {
                    MapaMuscular(
                        musculosPrimarios = ejercicio.musculosPrimarios,
                        musculosSecundarios = ejercicio.musculosSecundarios,
                        vista = vistaMasRelevante(ejercicio.musculosPrimarios, ejercicio.musculosSecundarios),
                        mostrarEtiquetas = false,
                    )
                }
            } else {
                ImagenEjercicio(
                    modelo = ejercicio.imagenes.first(),
                    descripcion = null,
                    modifier = Modifier
                        .size(esp.miniaturaEjercicio)
                        .clip(MaterialTheme.shapes.small),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(esp.xs),
            ) {
                Text(
                    text = ejercicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(
                        R.string.formato_musculo_equipo,
                        ejercicio.musculosPrimarios.joinToString().capitalizado(),
                        ejercicio.equipo?.capitalizado() ?: stringResource(R.string.equipo_ninguno),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                EtiquetaDificultad(ejercicio.nivel)
            }
            IconButton(onClick = onFavorito) {
                Icon(
                    imageVector = if (esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(
                        if (esFavorito) R.string.accion_quitar_favorito else R.string.accion_agregar_favorito,
                    ),
                    tint = if (esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun TarjetaEjercicioPreview() {
    VistaPrevia {
        TarjetaEjercicio(
            ejercicio = DatosEjemplo.ejercicios.first(),
            esFavorito = true,
            onClick = {},
            onFavorito = {},
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
        )
    }
}
