package com.gymguia.app.ui.screens.progreso

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.OpcionEjercicio
import com.gymguia.app.domain.model.PuntoGrafica
import com.gymguia.app.domain.model.RangoFechas
import com.gymguia.app.domain.model.RecordPersonal
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.domain.util.formatoLargo
import com.gymguia.app.ui.components.ChipFiltro
import com.gymguia.app.ui.components.EncabezadoSeccion
import com.gymguia.app.ui.components.EstadoVacio
import com.gymguia.app.ui.components.GraficaLinea
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.util.etiqueta
import com.gymguia.app.ui.util.textoPeso

/**
 * Pestaña Progreso: selector de ejercicio, rango de fechas, gráficas de peso máximo y volumen,
 * y récords personales. Los puntos ya llegan en la unidad elegida; los récords, en kg.
 * Solo cuentan las series completadas y con peso.
 */
@Composable
fun GraficasContenido(
    graficas: GraficasUi,
    unidad: UnidadPeso,
    onEjercicio: (String) -> Unit,
    onRango: (RangoFechas) -> Unit,
) {
    val esp = MaterialTheme.espaciado

    if (graficas.ejercicios.isEmpty()) {
        EstadoVacio(
            icono = Icons.Outlined.Insights,
            titulo = stringResource(R.string.vacio_progreso_titulo),
            mensaje = stringResource(R.string.vacio_progreso_mensaje),
            modifier = Modifier.padding(top = esp.xl),
        )
        return
    }

    val etiquetaUnidad = stringResource(unidad.etiqueta)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = esp.margenPantalla, vertical = esp.l),
        verticalArrangement = Arrangement.spacedBy(esp.m),
    ) {
        item {
            SelectorEjercicio(
                opciones = graficas.ejercicios,
                seleccionadoId = graficas.ejercicioId,
                onSeleccion = onEjercicio,
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(esp.s)) {
                RangoFechas.entries.forEach { r ->
                    ChipFiltro(texto = stringResource(r.etiqueta), seleccionado = r == graficas.rango, onClick = { onRango(r) })
                }
            }
        }
        item {
            TarjetaGrafica(
                titulo = stringResource(R.string.grafica_peso_maximo, etiquetaUnidad),
                puntos = graficas.pesoMaximo,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        item {
            TarjetaGrafica(
                titulo = stringResource(R.string.grafica_volumen, etiquetaUnidad),
                puntos = graficas.volumen,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        item { EncabezadoSeccion(titulo = stringResource(R.string.titulo_records)) }
        items(graficas.records, key = { it.tipo }) { record -> TarjetaRecord(record, unidad) }
    }
}

/** Menú desplegable con los ejercicios que tienen registros. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorEjercicio(opciones: List<OpcionEjercicio>, seleccionadoId: String?, onSeleccion: (String) -> Unit) {
    var abierto by rememberSaveable { mutableStateOf(false) }
    val seleccionado = opciones.firstOrNull { it.id == seleccionadoId }
    ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { abierto = it }) {
        OutlinedTextField(
            value = seleccionado?.nombre.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.progreso_ejercicio)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            opciones.forEach { (id, nombre) ->
                DropdownMenuItem(
                    text = { Text(nombre) },
                    onClick = {
                        onSeleccion(id)
                        abierto = false
                    },
                )
            }
        }
    }
}

@Composable
private fun TarjetaGrafica(titulo: String, puntos: List<PuntoGrafica>, color: Color) {
    val esp = MaterialTheme.espaciado
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(modifier = Modifier.padding(esp.l), verticalArrangement = Arrangement.spacedBy(esp.m)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            if (puntos.isEmpty()) {
                // Hay registros del ejercicio, pero ninguno dentro del rango elegido.
                Box(Modifier.fillMaxWidth().height(esp.alturaGrafica), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.grafica_sin_sesiones),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                GraficaLinea(puntos = puntos, descripcion = titulo, colorLinea = color)
            }
        }
    }
}

/** Tarjeta de un récord personal con su fecha. */
@Composable
private fun TarjetaRecord(record: RecordPersonal, unidad: UnidadPeso) {
    val esp = MaterialTheme.espaciado
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(esp.l),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = esp.m),
            ) {
                Text(stringResource(record.tipo.etiqueta), style = MaterialTheme.typography.titleSmall)
                Text(
                    text = record.fecha.formatoLargo(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            // Peso máximo, volumen y 1RM se guardan en kg y se muestran en la unidad elegida.
            Text(textoPeso(record.valorKg, unidad), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@PreviewPantalla
@Composable
private fun GraficasContenidoPreview() {
    VistaPrevia {
        GraficasContenido(graficas = DatosEjemplo.graficas(), unidad = UnidadPeso.KG, onEjercicio = {}, onRango = {})
    }
}

@PreviewPantalla
@Composable
private fun GraficasSinRegistrosPreview() {
    VistaPrevia {
        GraficasContenido(
            graficas = DatosEjemplo.graficas().copy(ejercicios = emptyList(), ejercicioId = null, pesoMaximo = emptyList(), volumen = emptyList(), records = emptyList()),
            unidad = UnidadPeso.KG,
            onEjercicio = {},
            onRango = {},
        )
    }
}
