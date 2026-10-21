package com.gymguia.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado
import androidx.compose.material3.MaterialTheme

/**
 * Muestra una imagen de ejercicio.
 * - Si el modelo es un id de drawable (datos de ejemplo), usa painterResource,
 *   así también se ve en las vistas previas.
 * - Cualquier otro modelo (ruta de asset desde la Fase 2) lo carga Coil.
 */
@Composable
fun ImagenEjercicio(
    modelo: Any?,
    descripcion: String?,
    modifier: Modifier = Modifier,
    escala: ContentScale = ContentScale.Crop,
) {
    if (modelo is Int) {
        Image(
            painter = painterResource(modelo),
            contentDescription = descripcion,
            modifier = modifier,
            contentScale = escala,
        )
    } else {
        AsyncImage(
            model = modelo,
            contentDescription = descripcion,
            modifier = modifier,
            contentScale = escala,
        )
    }
}

@PreviewClaroOscuro
@Composable
private fun ImagenEjercicioPreview() {
    VistaPrevia {
        ImagenEjercicio(
            modelo = R.drawable.demo_ejemplo_1,
            descripcion = null,
            modifier = Modifier.size(MaterialTheme.espaciado.miniaturaEjercicio),
        )
    }
}
