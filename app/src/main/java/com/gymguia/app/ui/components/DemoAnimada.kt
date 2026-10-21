package com.gymguia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.theme.extra

/**
 * Demostración animada de un ejercicio: alterna las dos imágenes con un fundido suave, en bucle
 * (una sola imagen se muestra fija). Al tocarla se abre a pantalla completa con zoom.
 *
 * Si el ejercicio no trae imágenes se muestra [respaldo] (por ejemplo, el mapa muscular) o, si no
 * hay, un ícono.
 */
@Composable
fun DemoAnimada(
    imagenes: List<Any>,
    descripcion: String,
    modifier: Modifier = Modifier,
    gif: String? = null,
    respaldo: (@Composable () -> Unit)? = null,
) {
    // Se guarda al rotar la pantalla para que el visor no se cierre solo.
    var visorAbierto by rememberSaveable { mutableStateOf(false) }
    // Con GIF disponible se muestra primero la animación; el usuario puede cambiar a las fotos.
    var verGif by rememberSaveable(gif) { mutableStateOf(true) }
    val mostrarGif = gif != null && (verGif || imagenes.isEmpty())
    val contenido: List<Any> = if (mostrarGif) listOf(gif!!) else imagenes
    val hayImagenes = contenido.isNotEmpty()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.espaciado.s)) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.espaciado.alturaDemo)
            .clip(MaterialTheme.shapes.large)
            // Los GIF están dibujados sobre blanco; las fotos usan el gris de las superficies.
            .background(if (mostrarGif) MaterialTheme.extra.lienzoMapa else MaterialTheme.colorScheme.surfaceContainerHigh)
            .then(
                if (hayImagenes) {
                    Modifier.clickable(
                        onClickLabel = stringResource(R.string.visor_ampliar),
                        role = Role.Button,
                        onClick = { visorAbierto = true },
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        when {
            hayImagenes -> {
                ImagenAnimada(imagenes = contenido, descripcion = descripcion, modifier = Modifier.fillMaxSize())
                // Pista visual de que se puede ampliar.
                Icon(
                    imageVector = Icons.Outlined.Fullscreen,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(MaterialTheme.espaciado.s)
                        .size(MaterialTheme.espaciado.iconoM),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            respaldo != null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.espaciado.m),
                contentAlignment = Alignment.Center,
            ) { respaldo() }
            else -> Icon(
                imageVector = Icons.Outlined.ImageNotSupported,
                contentDescription = descripcion,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(MaterialTheme.espaciado.iconoXl),
                tint = MaterialTheme.colorScheme.outline,
            )
        }
    }

    // Selector Animación / Fotos: solo cuando el ejercicio tiene las dos cosas.
    if (gif != null && imagenes.isNotEmpty()) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = verGif,
                onClick = { verGif = true },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                label = { Text(stringResource(R.string.demo_animacion)) },
            )
            SegmentedButton(
                selected = !verGif,
                onClick = { verGif = false },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                label = { Text(stringResource(R.string.demo_fotos)) },
            )
        }
    }
    }

    if (visorAbierto && hayImagenes) {
        VisorImagen(imagenes = contenido, descripcion = descripcion, onCerrar = { visorAbierto = false })
    }
}

@PreviewClaroOscuro
@Composable
private fun DemoAnimadaPreview() {
    VistaPrevia {
        DemoAnimada(
            imagenes = listOf(R.drawable.demo_ejemplo_1, R.drawable.demo_ejemplo_2),
            descripcion = "",
        )
    }
}

@PreviewClaroOscuro
@Composable
private fun DemoAnimadaEstaticaPreview() {
    VistaPrevia {
        DemoAnimada(imagenes = listOf(R.drawable.demo_ejemplo_2), descripcion = "")
    }
}

@PreviewClaroOscuro
@Composable
private fun DemoAnimadaSinImagenesPreview() {
    VistaPrevia {
        DemoAnimada(imagenes = emptyList(), descripcion = "")
    }
}
