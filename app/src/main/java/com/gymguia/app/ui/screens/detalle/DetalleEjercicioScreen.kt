package com.gymguia.app.ui.screens.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.Ejercicio
import com.gymguia.app.domain.model.Serie
import com.gymguia.app.domain.model.UltimaVez
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.domain.util.capitalizado
import com.gymguia.app.domain.util.formatearTiempo
import com.gymguia.app.domain.util.formatoLargo
import com.gymguia.app.ui.components.DemoAnimada
import com.gymguia.app.ui.components.DialogoEliminarSerie
import com.gymguia.app.ui.components.EncabezadoSeccion
import com.gymguia.app.ui.components.EstadoVacio
import com.gymguia.app.ui.components.EtiquetaDificultad
import com.gymguia.app.ui.components.FilaSerie
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.components.mapa.LeyendaMapaMuscular
import com.gymguia.app.ui.components.mapa.MapaMuscularAuto
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.util.etiqueta
import com.gymguia.app.ui.util.textoSerie
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Detalle de un ejercicio: demostración animada, datos, instrucciones y
 * la sección "Mis series de hoy".
 * No guarda las series: las recibe ya leídas de la base (en kg) y avisa de cada cambio.
 */
@Composable
fun DetalleEjercicioScreen(
    ejercicio: Ejercicio,
    esFavorito: Boolean,
    unidad: UnidadPeso,
    descansoSegundos: Int,
    series: List<Serie>,
    ultimaVez: UltimaVez?,
    onAgregarSerie: () -> Unit,
    onActualizarSerie: (Serie) -> Unit,
    onEliminarSerie: (Long) -> Unit,
    onAtras: () -> Unit,
    onAlternarFavorito: () -> Unit,
    onIniciarDescanso: () -> Unit,
) {
    val esp = MaterialTheme.espaciado
    // Aviso inferior que ofrece iniciar el descanso al completar una serie.
    val avisos = remember { SnackbarHostState() }
    val ambito = rememberCoroutineScope()
    val textoOferta = stringResource(R.string.serie_completada_oferta, formatearTiempo(descansoSegundos))
    val textoAccionOferta = stringResource(R.string.accion_iniciar)
    // Serie que el usuario pidió eliminar y está pendiente de confirmar.
    var serieAEliminar by remember { mutableStateOf<Pair<Int, Serie>?>(null) }

    PantallaBase(
        titulo = ejercicio.nombre,
        onAtras = onAtras,
        snackbarHost = { SnackbarHost(avisos) },
        acciones = {
            IconButton(onClick = onAlternarFavorito) {
                Icon(
                    imageVector = if (esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(
                        if (esFavorito) R.string.accion_quitar_favorito else R.string.accion_agregar_favorito,
                    ),
                    tint = if (esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    ) { relleno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno),
            contentPadding = PaddingValues(horizontal = esp.margenPantalla, vertical = esp.s),
            verticalArrangement = Arrangement.spacedBy(esp.m),
        ) {
            item {
                DemoAnimada(
                    imagenes = ejercicio.imagenes,
                    gif = ejercicio.gif,
                    descripcion = stringResource(R.string.detalle_demo_descripcion, ejercicio.nombre),
                    // Los ejercicios sin fotos muestran el mapa muscular en su lugar.
                    respaldo = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(esp.s),
                        ) {
                            MapaMuscularAuto(
                                musculosPrimarios = ejercicio.musculosPrimarios,
                                musculosSecundarios = ejercicio.musculosSecundarios,
                                modifier = Modifier.weight(1f),
                            )
                            LeyendaMapaMuscular(Modifier.fillMaxWidth())
                        }
                    },
                )
            }
            item { CabeceraEjercicio(ejercicio) }
            // Si no hay fotos, el mapa ya ocupa el lugar de la demostración y no se repite.
            if (ejercicio.imagenes.isNotEmpty() || ejercicio.gif != null) item { TarjetaMapaMuscular(ejercicio) }
            item { EncabezadoSeccion(titulo = stringResource(R.string.detalle_instrucciones)) }
            itemsIndexed(ejercicio.instrucciones) { indice, paso ->
                PasoInstruccion(numero = indice + 1, texto = paso)
            }

            // ---------- Mis series de hoy ----------
            item {
                EncabezadoSeccion(
                    titulo = stringResource(R.string.detalle_mis_series_hoy),
                    modifier = Modifier.padding(top = esp.s),
                )
            }
            item {
                TarjetaUltimaVez(ultimaVez = ultimaVez, unidad = unidad)
            }
            if (series.isEmpty()) {
                item {
                    EstadoVacio(
                        icono = Icons.Outlined.History,
                        titulo = stringResource(R.string.vacio_series_titulo),
                        mensaje = stringResource(R.string.vacio_series_mensaje),
                    )
                }
            }
            itemsIndexed(series, key = { _, serie -> serie.id }) { indice, serie ->
                FilaSerie(
                    numero = indice + 1,
                    peso = unidad.desdeKg(serie.pesoKg),
                    repeticiones = serie.repeticiones,
                    completada = serie.completada,
                    unidad = unidad,
                    // El valor capturado se convierte de vuelta a kg antes de guardarlo.
                    onPesoChange = { onActualizarSerie(serie.copy(pesoKg = unidad.aKg(it))) },
                    onRepeticionesChange = { onActualizarSerie(serie.copy(repeticiones = it)) },
                    onCompletadaChange = { completada ->
                        onActualizarSerie(serie.copy(completada = completada))
                        if (completada) {
                            // Se ofrece el descanso; si el usuario lo ignora, el aviso desaparece solo.
                            ambito.launch {
                                avisos.currentSnackbarData?.dismiss()
                                val respuesta = avisos.showSnackbar(
                                    message = textoOferta,
                                    actionLabel = textoAccionOferta,
                                    duration = SnackbarDuration.Short,
                                )
                                if (respuesta == SnackbarResult.ActionPerformed) onIniciarDescanso()
                            }
                        }
                    },
                    onEliminar = { serieAEliminar = indice + 1 to serie },
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(esp.m)) {
                    // La nueva serie copia peso y repeticiones de la anterior (lo decide el repositorio).
                    OutlinedButton(onClick = onAgregarSerie, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text(stringResource(R.string.accion_agregar_serie), modifier = Modifier.padding(start = esp.s))
                    }
                    Button(onClick = onIniciarDescanso, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Outlined.Timer, contentDescription = null)
                        Text(stringResource(R.string.accion_iniciar_descanso), modifier = Modifier.padding(start = esp.s))
                    }
                }
            }
        }
    }

    // Confirmación antes de borrar una serie.
    serieAEliminar?.let { (numero, serie) ->
        DialogoEliminarSerie(
            numero = numero,
            onConfirmar = {
                onEliminarSerie(serie.id)
                serieAEliminar = null
            },
            onCancelar = { serieAEliminar = null },
        )
    }
}

/** Mapa del cuerpo con los músculos que trabaja el ejercicio. */
@Composable
private fun TarjetaMapaMuscular(ejercicio: Ejercicio) {
    val esp = MaterialTheme.espaciado
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(esp.l),
            verticalArrangement = Arrangement.spacedBy(esp.m),
        ) {
            Text(stringResource(R.string.mapa_titulo), style = MaterialTheme.typography.titleSmall)
            MapaMuscularAuto(
                musculosPrimarios = ejercicio.musculosPrimarios,
                musculosSecundarios = ejercicio.musculosSecundarios,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(esp.alturaMapaMuscular),
            )
            LeyendaMapaMuscular(Modifier.fillMaxWidth())
        }
    }
}

/** Nombre, músculos, equipo y nivel. */
@Composable
private fun CabeceraEjercicio(ejercicio: Ejercicio) {
    val esp = MaterialTheme.espaciado
    Column(verticalArrangement = Arrangement.spacedBy(esp.s)) {
        Text(ejercicio.nombre, style = MaterialTheme.typography.headlineSmall)
        EtiquetaDificultad(ejercicio.nivel)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(esp.l),
                verticalArrangement = Arrangement.spacedBy(esp.m),
            ) {
                DatoEjercicio(stringResource(R.string.detalle_musculos_primarios), ejercicio.musculosPrimarios.joinToString().capitalizado())
                DatoEjercicio(
                    stringResource(R.string.detalle_musculos_secundarios),
                    ejercicio.musculosSecundarios.joinToString().ifEmpty { stringResource(R.string.detalle_ninguno) }.capitalizado(),
                )
                DatoEjercicio(stringResource(R.string.detalle_equipo), ejercicio.equipo?.capitalizado() ?: stringResource(R.string.equipo_ninguno))
                DatoEjercicio(stringResource(R.string.detalle_categoria), ejercicio.categoria.capitalizado())
                DatoEjercicio(stringResource(R.string.detalle_nivel), stringResource(ejercicio.nivel.etiqueta))
            }
        }
    }
}

@Composable
private fun DatoEjercicio(titulo: String, valor: String) {
    Column {
        Text(titulo, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}

/** Paso numerado de las instrucciones. */
@Composable
private fun PasoInstruccion(numero: Int, texto: String) {
    val esp = MaterialTheme.espaciado
    Row(horizontalArrangement = Arrangement.spacedBy(esp.m)) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(esp.iconoL),
        ) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(numero.toString(), style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
            }
        }
        Text(texto, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}

/** Tarjeta desplegable con las series de la sesión anterior de este ejercicio. */
@Composable
private fun TarjetaUltimaVez(ultimaVez: UltimaVez?, unidad: UnidadPeso) {
    val esp = MaterialTheme.espaciado
    val series = ultimaVez?.series.orEmpty()
    var abierta by rememberSaveable { mutableStateOf(false) }
    Card(
        onClick = { abierta = !abierta },
        enabled = series.isNotEmpty(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(modifier = Modifier.padding(esp.l), verticalArrangement = Arrangement.spacedBy(esp.s)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = esp.m),
                ) {
                    Text(stringResource(R.string.detalle_ultima_vez), style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = if (ultimaVez != null) ultimaVez.fecha.formatoLargo()
                        else stringResource(R.string.detalle_sin_registros_previos),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (series.isNotEmpty()) {
                    Icon(
                        imageVector = if (abierta) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = stringResource(if (abierta) R.string.accion_ocultar else R.string.accion_mostrar),
                    )
                }
            }
            AnimatedVisibility(visible = abierta) {
                Column(verticalArrangement = Arrangement.spacedBy(esp.xs)) {
                    series.forEachIndexed { i, serie ->
                        Text(
                            text = stringResource(
                                R.string.formato_linea_serie,
                                i + 1,
                                textoSerie(serie.pesoKg, serie.repeticiones, unidad),
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@PreviewPantalla
@Composable
private fun DetalleEjercicioScreenPreview() {
    VistaPrevia {
        DetalleEjercicioScreen(
            ejercicio = DatosEjemplo.ejercicios.first(),
            esFavorito = true,
            unidad = UnidadPeso.KG,
            descansoSegundos = 90,
            series = DatosEjemplo.seriesDeHoy,
            ultimaVez = UltimaVez(DatosEjemplo.fechaUltimaVez, DatosEjemplo.seriesUltimaVez),
            onAgregarSerie = {}, onActualizarSerie = {}, onEliminarSerie = {},
            onAtras = {}, onAlternarFavorito = {}, onIniciarDescanso = {},
        )
    }
}
