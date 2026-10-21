package com.gymguia.app.ui.screens.progreso

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.EjercicioDeSesion
import com.gymguia.app.domain.model.Serie
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.domain.util.LocaleApp
import com.gymguia.app.domain.util.formatoDiaSemana
import com.gymguia.app.ui.components.CeldaDiaCalendario
import com.gymguia.app.ui.components.EncabezadoSeccion
import com.gymguia.app.ui.components.DialogoEliminarSerie
import com.gymguia.app.ui.components.EstadoVacio
import com.gymguia.app.ui.components.FilaSerie
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle

private const val DIAS_POR_SEMANA = 7

/**
 * Pestaña Calendario: cuadrícula mensual hecha con filas de Compose, resumen de racha y la
 * sesión del día seleccionado, cuyas series se pueden editar y eliminar.
 * No guarda estado: todo llega ya calculado en [calendario].
 */
@Composable
fun CalendarioContenido(
    calendario: CalendarioUi,
    unidad: UnidadPeso,
    onCambiarMes: (Int) -> Unit,
    onDia: (Int) -> Unit,
    onActualizarSerie: (Serie) -> Unit,
    onEliminarSerie: (Long) -> Unit,
) {
    val esp = MaterialTheme.espaciado
    val mes = calendario.mes
    val fechaSeleccionada = calendario.diaSeleccionado?.let { mes.atDay(it) }
    // Serie que el usuario pidió eliminar y está pendiente de confirmar.
    var serieAEliminar by remember { mutableStateOf<Pair<Int, Serie>?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = esp.margenPantalla, vertical = esp.l),
        verticalArrangement = Arrangement.spacedBy(esp.m),
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(esp.m)) {
                TarjetaResumen(
                    icono = Icons.Outlined.LocalFireDepartment,
                    titulo = stringResource(R.string.calendario_racha),
                    valor = pluralStringResource(R.plurals.cantidad_dias, calendario.racha, calendario.racha),
                    modifier = Modifier.weight(1f),
                )
                TarjetaResumen(
                    icono = Icons.Outlined.CalendarMonth,
                    titulo = stringResource(R.string.calendario_entrenamientos_mes),
                    valor = calendario.entrenamientosDelMes.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
        }
        item {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Column(modifier = Modifier.padding(esp.m)) {
                    EncabezadoMes(
                        mes = mes,
                        onAnterior = { onCambiarMes(-1) },
                        onSiguiente = { onCambiarMes(1) },
                    )
                    CuadriculaMes(
                        mes = mes,
                        hoy = calendario.hoy,
                        entrenados = calendario.diasEntrenados,
                        seleccionado = calendario.diaSeleccionado,
                        onDia = onDia,
                    )
                }
            }
        }
        if (fechaSeleccionada != null) {
            item {
                EncabezadoSeccion(
                    titulo = fechaSeleccionada.formatoDiaSemana(),
                    modifier = Modifier.padding(top = esp.s),
                )
            }
            if (calendario.sesion.isNotEmpty()) {
                items(calendario.sesion, key = { it.ejercicioId }) { ejercicio ->
                    TarjetaEjercicioDelDia(
                        ejercicio = ejercicio,
                        unidad = unidad,
                        onActualizarSerie = onActualizarSerie,
                        onEliminar = { numero, serie -> serieAEliminar = numero to serie },
                    )
                }
            } else {
                item {
                    EstadoVacio(
                        icono = Icons.Outlined.EventBusy,
                        titulo = stringResource(R.string.vacio_dia_titulo),
                        mensaje = stringResource(R.string.vacio_dia_mensaje),
                    )
                }
            }
        }
    }

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

/** Mes y año con flechas para cambiar de mes. */
@Composable
private fun EncabezadoMes(mes: YearMonth, onAnterior: () -> Unit, onSiguiente: () -> Unit) {
    val nombreMes = mes.month.getDisplayName(TextStyle.FULL_STANDALONE, LocaleApp)
        .replaceFirstChar { it.titlecase(LocaleApp) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onAnterior) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = stringResource(R.string.calendario_mes_anterior))
        }
        Text(
            text = stringResource(R.string.formato_mes_anio, nombreMes, mes.year),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onSiguiente) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = stringResource(R.string.calendario_mes_siguiente))
        }
    }
}

/** Cuadrícula de semanas que empieza en lunes. */
@Composable
private fun CuadriculaMes(
    mes: YearMonth,
    hoy: LocalDate,
    entrenados: Set<Int>,
    seleccionado: Int?,
    onDia: (Int) -> Unit,
) {
    // Huecos antes del día 1: lunes = 0 ... domingo = 6.
    val huecos = mes.atDay(1).dayOfWeek.value - DayOfWeek.MONDAY.value
    val celdas: List<Int?> = List(huecos) { null } + (1..mes.lengthOfMonth()).toList()
    val semanas = celdas.chunked(DIAS_POR_SEMANA)

    Column {
        // Iniciales de los días de la semana.
        Row {
            DayOfWeek.entries.forEach { dia ->
                Text(
                    text = dia.getDisplayName(TextStyle.NARROW_STANDALONE, LocaleApp).uppercase(LocaleApp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = MaterialTheme.espaciado.s),
                )
            }
        }
        semanas.forEach { semana ->
            Row {
                semana.forEach { dia ->
                    CeldaDiaCalendario(
                        dia = dia,
                        entrenado = dia in entrenados,
                        esHoy = dia != null && mes.atDay(dia) == hoy,
                        seleccionado = dia != null && dia == seleccionado,
                        onClick = { if (dia != null) onDia(dia) },
                        modifier = Modifier.weight(1f),
                    )
                }
                // Completa la última semana para que las celdas mantengan su ancho.
                repeat(DIAS_POR_SEMANA - semana.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

/** Tarjeta pequeña con un dato del resumen (racha, entrenamientos del mes). */
@Composable
private fun TarjetaResumen(icono: ImageVector, titulo: String, valor: String, modifier: Modifier = Modifier) {
    val esp = MaterialTheme.espaciado
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    ) {
        Column(modifier = Modifier.padding(esp.l), verticalArrangement = Arrangement.spacedBy(esp.xs)) {
            Icon(icono, contentDescription = null)
            Text(titulo, style = MaterialTheme.typography.labelMedium)
            Text(valor, style = MaterialTheme.typography.titleLarge)
        }
    }
}

/** Ejercicio realizado en el día seleccionado, con sus series editables. */
@Composable
private fun TarjetaEjercicioDelDia(
    ejercicio: EjercicioDeSesion,
    unidad: UnidadPeso,
    onActualizarSerie: (Serie) -> Unit,
    onEliminar: (Int, Serie) -> Unit,
) {
    val esp = MaterialTheme.espaciado
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(esp.m),
            verticalArrangement = Arrangement.spacedBy(esp.s),
        ) {
            Text(
                text = ejercicio.nombreEjercicio,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = esp.xs),
            )
            ejercicio.series.forEachIndexed { indice, serie ->
                FilaSerie(
                    numero = indice + 1,
                    peso = unidad.desdeKg(serie.pesoKg),
                    repeticiones = serie.repeticiones,
                    completada = serie.completada,
                    unidad = unidad,
                    // El valor capturado se convierte de vuelta a kg antes de guardarlo.
                    onPesoChange = { onActualizarSerie(serie.copy(pesoKg = unidad.aKg(it))) },
                    onRepeticionesChange = { onActualizarSerie(serie.copy(repeticiones = it)) },
                    onCompletadaChange = { onActualizarSerie(serie.copy(completada = it)) },
                    onEliminar = { onEliminar(indice + 1, serie) },
                )
            }
        }
    }
}

@PreviewPantalla
@Composable
private fun CalendarioContenidoPreview() {
    VistaPrevia {
        CalendarioContenido(
            calendario = DatosEjemplo.calendario(),
            unidad = UnidadPeso.KG,
            onCambiarMes = {}, onDia = {}, onActualizarSerie = {}, onEliminarSerie = {},
        )
    }
}

@PreviewPantalla
@Composable
private fun CalendarioSinRegistrosPreview() {
    VistaPrevia {
        CalendarioContenido(
            calendario = DatosEjemplo.calendario().copy(diasEntrenados = emptySet(), racha = 0, sesion = emptyList()),
            unidad = UnidadPeso.KG,
            onCambiarMes = {}, onDia = {}, onActualizarSerie = {}, onEliminarSerie = {},
        )
    }
}
