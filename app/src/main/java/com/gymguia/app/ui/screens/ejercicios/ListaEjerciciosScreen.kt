package com.gymguia.app.ui.screens.ejercicios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.ConteoValor
import com.gymguia.app.domain.model.Ejercicio
import com.gymguia.app.domain.model.NivelDificultad
import com.gymguia.app.domain.util.capitalizado
import com.gymguia.app.ui.components.BarraBusqueda
import com.gymguia.app.ui.components.ChipFiltro
import com.gymguia.app.ui.components.EstadoVacio
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.TarjetaEjercicio
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.util.etiqueta

/**
 * Lista de ejercicios con búsqueda y filtros por músculo, equipo y nivel.
 * Cada filtro admite una opción a la vez; tocar la opción activa la quita.
 * No guarda estado propio: lo recibe del ViewModel y le avisa de cada cambio.
 *
 * [resultados] es null mientras la base responde por primera vez.
 */
@Composable
fun ListaEjerciciosScreen(
    filtros: Filtros,
    resultados: List<Ejercicio>?,
    favoritos: Set<String>,
    musculos: List<ConteoValor>,
    equipos: List<ConteoValor>,
    onConsultaChange: (String) -> Unit,
    onMusculo: (String) -> Unit,
    onEquipo: (String) -> Unit,
    onNivel: (NivelDificultad) -> Unit,
    onLimpiarFiltros: () -> Unit,
    onEjercicio: (String) -> Unit,
    onAlternarFavorito: (String) -> Unit,
) {
    val esp = MaterialTheme.espaciado
    // El texto del campo vive aquí, no en el ViewModel: si el campo leyera cada tecla de un flujo
    // asíncrono, escribir rápido podría desordenar las letras. El ViewModel solo recibe el texto.
    var consulta by remember { mutableStateOf(filtros.consulta) }

    PantallaBase(titulo = stringResource(R.string.titulo_ejercicios)) { relleno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno),
            contentPadding = PaddingValues(vertical = esp.s),
            verticalArrangement = Arrangement.spacedBy(esp.m),
        ) {
            item {
                BarraBusqueda(
                    consulta = consulta,
                    onConsultaChange = {
                        consulta = it
                        onConsultaChange(it)
                    },
                    modifier = Modifier.padding(horizontal = esp.margenPantalla),
                )
            }
            item {
                FilaFiltro(
                    titulo = stringResource(R.string.filtro_musculo),
                    opciones = musculos.map { it.valor },
                    seleccion = filtros.musculo,
                    texto = { it.capitalizado() },
                    onSeleccion = onMusculo,
                )
            }
            item {
                FilaFiltro(
                    titulo = stringResource(R.string.filtro_equipo),
                    opciones = equipos.map { it.valor },
                    seleccion = filtros.equipo,
                    texto = { it.capitalizado() },
                    onSeleccion = onEquipo,
                )
            }
            item {
                FilaFiltro(
                    titulo = stringResource(R.string.filtro_nivel),
                    opciones = NivelDificultad.entries,
                    seleccion = filtros.nivel,
                    texto = { stringResource(it.etiqueta) },
                    onSeleccion = onNivel,
                )
            }
            if (resultados == null) {
                item {
                    Box(Modifier.fillMaxWidth().padding(esp.xl), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                item {
                    Text(
                        text = pluralStringResource(R.plurals.cantidad_resultados, resultados.size, resultados.size),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = esp.margenPantalla),
                    )
                }
                if (resultados.isEmpty()) {
                    item {
                        Column {
                            EstadoVacio(
                                icono = Icons.Outlined.SearchOff,
                                titulo = stringResource(R.string.vacio_resultados_titulo),
                                mensaje = stringResource(R.string.vacio_resultados_mensaje),
                            )
                            if (filtros.hayActivos) {
                                TextButton(
                                    onClick = {
                                        consulta = ""
                                        onLimpiarFiltros()
                                    },
                                    modifier = Modifier.padding(horizontal = esp.margenPantalla),
                                ) { Text(stringResource(R.string.accion_limpiar_filtros)) }
                            }
                        }
                    }
                }
                items(resultados, key = { it.id }) { ejercicio ->
                    TarjetaEjercicio(
                        ejercicio = ejercicio,
                        esFavorito = ejercicio.id in favoritos,
                        onClick = { onEjercicio(ejercicio.id) },
                        onFavorito = { onAlternarFavorito(ejercicio.id) },
                        modifier = Modifier.padding(horizontal = esp.margenPantalla),
                    )
                }
            }
        }
    }
}

/** Fila con título y chips desplazables horizontalmente. */
@Composable
private fun <T> FilaFiltro(
    titulo: String,
    opciones: List<T>,
    seleccion: T?,
    texto: @Composable (T) -> String,
    onSeleccion: (T) -> Unit,
) {
    val esp = MaterialTheme.espaciado
    Column(verticalArrangement = Arrangement.spacedBy(esp.xs)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = esp.margenPantalla),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = esp.margenPantalla),
            horizontalArrangement = Arrangement.spacedBy(esp.s),
        ) {
            items(opciones) { opcion ->
                ChipFiltro(
                    texto = texto(opcion),
                    seleccionado = opcion == seleccion,
                    onClick = { onSeleccion(opcion) },
                )
            }
        }
    }
}

@PreviewPantalla
@Composable
private fun ListaEjerciciosScreenPreview() {
    VistaPrevia {
        ListaEjerciciosScreen(
            filtros = Filtros(),
            resultados = DatosEjemplo.ejercicios,
            favoritos = DatosEjemplo.favoritosIniciales,
            musculos = DatosEjemplo.musculos,
            equipos = DatosEjemplo.equipos,
            onConsultaChange = {}, onMusculo = {}, onEquipo = {}, onNivel = {},
            onLimpiarFiltros = {}, onEjercicio = {}, onAlternarFavorito = {},
        )
    }
}

@PreviewPantalla
@Composable
private fun ListaEjerciciosSinResultadosPreview() {
    VistaPrevia {
        ListaEjerciciosScreen(
            filtros = Filtros(consulta = "xyz"),
            resultados = emptyList(),
            favoritos = emptySet(),
            musculos = DatosEjemplo.musculos,
            equipos = DatosEjemplo.equipos,
            onConsultaChange = {}, onMusculo = {}, onEquipo = {}, onNivel = {},
            onLimpiarFiltros = {}, onEjercicio = {}, onAlternarFavorito = {},
        )
    }
}
