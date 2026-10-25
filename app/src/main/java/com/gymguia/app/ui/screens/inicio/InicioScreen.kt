package com.gymguia.app.ui.screens.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import java.time.format.TextStyle
import java.util.Locale
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.ConteoValor
import com.gymguia.app.domain.model.Ejercicio
import com.gymguia.app.domain.util.capitalizado
import com.gymguia.app.ui.components.BarraBusqueda
import com.gymguia.app.ui.components.EncabezadoSeccion
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.TarjetaEjercicio
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado

/** La app está solo en español: los nombres de los días no dependen del idioma del teléfono. */
private val IdiomaApp: Locale = Locale.forLanguageTag("es")

/** Columnas de la cuadrícula de grupos musculares. */
private const val COLUMNAS_GRUPOS = 2

/**
 * Pantalla de Inicio: búsqueda, accesos por músculo, acceso a Favoritos
 * y algunos ejercicios sugeridos.
 */
@Composable
fun InicioScreen(
    semana: List<DiaSemanaUi>,
    musculos: List<ConteoValor>,
    sugeridos: List<Ejercicio>,
    favoritos: Set<String>,
    onBuscar: (String) -> Unit,
    onMusculo: (String) -> Unit,
    onFavoritos: () -> Unit,
    onEjercicio: (String) -> Unit,
    onAlternarFavorito: (String) -> Unit,
) {
    val esp = MaterialTheme.espaciado
    var consulta by rememberSaveable { mutableStateOf("") }

    PantallaBase(
        titulo = stringResource(R.string.app_nombre),
        acciones = {
            IconButton(onClick = onFavoritos) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = stringResource(R.string.titulo_favoritos))
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
            item { Encabezado() }
            item { SemanaDiscos(semana) }
            item {
                BarraBusqueda(
                    consulta = consulta,
                    onConsultaChange = { consulta = it },
                    onBuscar = { if (it.isNotBlank()) onBuscar(it.trim()) },
                )
            }
            item { EncabezadoSeccion(titulo = stringResource(R.string.inicio_grupos_musculares)) }
            // Cuadrícula de grupos: filas de dos tarjetas.
            items(musculos.chunked(COLUMNAS_GRUPOS)) { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(esp.m)) {
                    fila.forEach { musculo ->
                        TarjetaGrupo(
                            nombre = musculo.valor.capitalizado(),
                            cantidad = musculo.cantidad,
                            onClick = { onMusculo(musculo.valor) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    // Si la última fila queda incompleta, se rellena para mantener el ancho.
                    repeat(COLUMNAS_GRUPOS - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            item {
                TarjetaAccesoFavoritos(
                    cantidad = favoritos.size,
                    onClick = onFavoritos,
                    modifier = Modifier.padding(top = esp.s),
                )
            }
            item { EncabezadoSeccion(titulo = stringResource(R.string.inicio_sugeridos)) }
            items(sugeridos, key = { it.id }) { ejercicio ->
                TarjetaEjercicio(
                    ejercicio = ejercicio,
                    esFavorito = ejercicio.id in favoritos,
                    onClick = { onEjercicio(ejercicio.id) },
                    onFavorito = { onAlternarFavorito(ejercicio.id) },
                )
            }
        }
    }
}

/**
 * Encabezado de Inicio: pregunta en tipografía grande. La segunda línea va en el color de énfasis
 * (amarillo en oscuro, ámbar profundo en claro, donde el amarillo no tendría contraste).
 */
@Composable
private fun Encabezado() {
    val esp = MaterialTheme.espaciado
    Column(modifier = Modifier.padding(top = esp.s)) {
        Text(
            text = stringResource(R.string.inicio_pregunta),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.inicio_pregunta_destacada),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

/**
 * La semana en curso como discos de una barra: un disco lleno por cada día entrenado.
 * Hoy se marca con la inicial en negrita. No hay una meta semanal: solo se cuentan los días.
 */
@Composable
private fun SemanaDiscos(semana: List<DiaSemanaUi>) {
    val esp = MaterialTheme.espaciado
    val entrenados = semana.count { it.entrenado }
    Column(verticalArrangement = Arrangement.spacedBy(esp.s)) {
        Text(
            text = stringResource(R.string.inicio_semana),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            semana.forEach { dia -> DiscoDia(dia) }
        }
        Text(
            text = pluralStringResource(R.plurals.dias_entrenados_semana, entrenados, entrenados),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Un disco: lleno con un anillo interior si se entrenó, de contorno punteado si no. */
@Composable
private fun DiscoDia(dia: DiaSemanaUi) {
    val esp = MaterialTheme.espaciado
    val colores = MaterialTheme.colorScheme
    val inicial = dia.fecha.dayOfWeek.getDisplayName(TextStyle.NARROW, IdiomaApp).uppercase()
    val descripcion = stringResource(
        if (dia.entrenado) R.string.semana_dia_entrenado else R.string.semana_dia_sin_entrenar,
        dia.fecha.dayOfWeek.getDisplayName(TextStyle.FULL, IdiomaApp),
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(esp.xs)) {
        Box(
            modifier = Modifier
                .size(esp.discoSemana)
                .drawBehind {
                    if (dia.entrenado) {
                        drawCircle(colores.primary)
                        // Anillo interior: da aspecto de disco de pesas.
                        drawCircle(
                            color = colores.background,
                            radius = size.minDimension / 2 - esp.anilloDisco.toPx(),
                            style = Stroke(width = esp.bordeFino.toPx()),
                        )
                    } else {
                        drawCircle(
                            color = colores.outline,
                            style = Stroke(
                                width = esp.bordeResaltado.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(esp.s.toPx(), esp.xs.toPx())),
                            ),
                        )
                    }
                }
                .semantics { contentDescription = descripcion },
        )
        Text(
            text = inicial,
            style = MaterialTheme.typography.labelMedium,
            color = if (dia.esHoy) colores.tertiary else colores.onSurfaceVariant,
            fontWeight = if (dia.esHoy) FontWeight.ExtraBold else null,
        )
    }
}

/**
 * Tarjeta de acceso a un grupo muscular: la cantidad de ejercicios es el dato principal,
 * en números grandes, y el nombre va debajo como una placa.
 */
@Composable
private fun TarjetaGrupo(
    nombre: String,
    cantidad: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val esp = MaterialTheme.espaciado
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(modifier = Modifier.padding(esp.l), verticalArrangement = Arrangement.spacedBy(esp.xs)) {
            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Tarjeta destacada que abre la lista de Favoritos. */
@Composable
private fun TarjetaAccesoFavoritos(cantidad: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val esp = MaterialTheme.espaciado
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Row(modifier = Modifier.padding(esp.l), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Favorite, contentDescription = null)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = esp.m),
            ) {
                Text(stringResource(R.string.titulo_favoritos), style = MaterialTheme.typography.titleMedium)
                Text(
                    text = pluralStringResource(R.plurals.cantidad_ejercicios_guardados, cantidad, cantidad),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@PreviewPantalla
@Composable
private fun InicioScreenPreview() {
    VistaPrevia {
        InicioScreen(
            semana = DatosEjemplo.semana,
            musculos = DatosEjemplo.musculos,
            sugeridos = DatosEjemplo.ejercicios.take(3),
            favoritos = DatosEjemplo.favoritosIniciales,
            onBuscar = {}, onMusculo = {}, onFavoritos = {}, onEjercicio = {}, onAlternarFavorito = {},
        )
    }
}
