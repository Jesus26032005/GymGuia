package com.gymguia.app.ui.screens.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.Ejercicio
import com.gymguia.app.ui.components.EstadoVacio
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.TarjetaEjercicio
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado

/** Lista de ejercicios marcados como favoritos, con estado vacío si no hay ninguno. */
@Composable
fun FavoritosScreen(
    favoritos: List<Ejercicio>?,
    onAtras: () -> Unit,
    onEjercicio: (String) -> Unit,
    onAlternarFavorito: (String) -> Unit,
) {
    val esp = MaterialTheme.espaciado
    PantallaBase(titulo = stringResource(R.string.titulo_favoritos), onAtras = onAtras) { relleno ->
        if (favoritos == null) {
            // Aún no responde la base: no se muestra nada para evitar un parpadeo del estado vacío.
        } else if (favoritos.isEmpty()) {
            EstadoVacio(
                icono = Icons.Outlined.FavoriteBorder,
                titulo = stringResource(R.string.vacio_favoritos_titulo),
                mensaje = stringResource(R.string.vacio_favoritos_mensaje),
                modifier = Modifier.padding(relleno),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentPadding = PaddingValues(horizontal = esp.margenPantalla, vertical = esp.s),
                verticalArrangement = Arrangement.spacedBy(esp.m),
            ) {
                items(favoritos, key = { it.id }) { ejercicio ->
                    TarjetaEjercicio(
                        ejercicio = ejercicio,
                        esFavorito = true,
                        onClick = { onEjercicio(ejercicio.id) },
                        onFavorito = { onAlternarFavorito(ejercicio.id) },
                    )
                }
            }
        }
    }
}

@PreviewPantalla
@Composable
private fun FavoritosScreenPreview() {
    VistaPrevia {
        FavoritosScreen(
            favoritos = DatosEjemplo.ejercicios.filter { it.id in DatosEjemplo.favoritosIniciales },
            onAtras = {}, onEjercicio = {}, onAlternarFavorito = {},
        )
    }
}

@PreviewPantalla
@Composable
private fun FavoritosVacioPreview() {
    VistaPrevia {
        FavoritosScreen(favoritos = emptyList(), onAtras = {}, onEjercicio = {}, onAlternarFavorito = {})
    }
}
