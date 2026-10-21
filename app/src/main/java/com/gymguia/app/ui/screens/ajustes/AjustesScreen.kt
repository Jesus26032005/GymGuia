package com.gymguia.app.ui.screens.ajustes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.selection.toggleable
import com.gymguia.app.R
import com.gymguia.app.domain.model.Ajustes
import com.gymguia.app.domain.model.LimitesDescanso
import com.gymguia.app.domain.model.TemaApp
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.domain.util.formatearTiempo
import com.gymguia.app.ui.components.CampoNumerico
import com.gymguia.app.ui.components.EncabezadoSeccion
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.VistaPrevia
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.util.etiqueta
import kotlinx.coroutines.launch

/**
 * Ajustes: tema, unidades, descanso por defecto, sonido y vibración, borrar historial y acceso
 * a "Acerca de". Los cambios se aplican al instante y los guarda el repositorio (DataStore).
 */
@Composable
fun AjustesScreen(
    ajustes: Ajustes,
    onAjustesChange: (Ajustes) -> Unit,
    onBorrarHistorial: (alTerminar: () -> Unit) -> Unit,
    onAcercaDe: () -> Unit,
) {
    val esp = MaterialTheme.espaciado
    val avisos = remember { SnackbarHostState() }
    val ambito = rememberCoroutineScope()
    val textoHistorialBorrado = stringResource(R.string.ajustes_historial_borrado)
    // Paso de la doble confirmación para borrar el historial: 0 = ninguna, 1 = primera, 2 = segunda.
    var pasoConfirmacion by rememberSaveable { mutableIntStateOf(PASO_NINGUNO) }

    PantallaBase(
        titulo = stringResource(R.string.titulo_ajustes),
        snackbarHost = { SnackbarHost(avisos) },
    ) { relleno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno),
            contentPadding = PaddingValues(horizontal = esp.margenPantalla, vertical = esp.s),
            verticalArrangement = Arrangement.spacedBy(esp.m),
        ) {
            item { EncabezadoSeccion(titulo = stringResource(R.string.ajustes_apariencia)) }
            item {
                GrupoAjustes {
                    TituloAjuste(stringResource(R.string.ajustes_tema))
                    SelectorSegmentado(
                        opciones = TemaApp.entries,
                        seleccion = ajustes.tema,
                        texto = { stringResource(it.etiqueta) },
                        onSeleccion = { onAjustesChange(ajustes.copy(tema = it)) },
                    )
                }
            }

            item { EncabezadoSeccion(titulo = stringResource(R.string.ajustes_entrenamiento)) }
            item {
                GrupoAjustes {
                    TituloAjuste(stringResource(R.string.ajustes_unidades))
                    SelectorSegmentado(
                        opciones = UnidadPeso.entries,
                        seleccion = ajustes.unidad,
                        texto = { stringResource(it.etiqueta) },
                        onSeleccion = { onAjustesChange(ajustes.copy(unidad = it)) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    TituloAjuste(
                        texto = stringResource(R.string.ajustes_descanso),
                        detalle = stringResource(R.string.ajustes_descanso_detalle, formatearTiempo(ajustes.descansoSegundos)),
                    )
                    CampoNumerico(
                        valor = ajustes.descansoSegundos.toDouble(),
                        onValorChange = { onAjustesChange(ajustes.copy(descansoSegundos = it.toInt())) },
                        etiqueta = stringResource(R.string.ajustes_segundos),
                        paso = LimitesDescanso.PASO_SEGUNDOS.toDouble(),
                        minimo = LimitesDescanso.MINIMO_SEGUNDOS.toDouble(),
                        maximo = LimitesDescanso.MAXIMO_SEGUNDOS.toDouble(),
                        mensajeError = stringResource(
                            R.string.error_descanso,
                            LimitesDescanso.MINIMO_SEGUNDOS,
                            LimitesDescanso.MAXIMO_SEGUNDOS,
                        ),
                    )
                }
            }

            item { EncabezadoSeccion(titulo = stringResource(R.string.ajustes_aviso_descanso)) }
            item {
                GrupoAjustes {
                    FilaInterruptor(
                        titulo = stringResource(R.string.ajustes_sonido),
                        detalle = stringResource(R.string.ajustes_sonido_detalle),
                        activo = ajustes.sonido,
                        onCambio = { onAjustesChange(ajustes.copy(sonido = it)) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FilaInterruptor(
                        titulo = stringResource(R.string.ajustes_vibracion),
                        detalle = stringResource(R.string.ajustes_vibracion_detalle),
                        activo = ajustes.vibracion,
                        onCambio = { onAjustesChange(ajustes.copy(vibracion = it)) },
                    )
                }
            }

            item { EncabezadoSeccion(titulo = stringResource(R.string.ajustes_datos)) }
            item {
                FilaAccion(
                    icono = Icons.Outlined.DeleteForever,
                    titulo = stringResource(R.string.ajustes_borrar_historial),
                    detalle = stringResource(R.string.ajustes_borrar_historial_detalle),
                    peligro = true,
                    onClick = { pasoConfirmacion = PASO_PRIMERO },
                )
            }

            item { EncabezadoSeccion(titulo = stringResource(R.string.ajustes_acerca_de)) }
            item {
                FilaAccion(
                    icono = Icons.Outlined.Info,
                    titulo = stringResource(R.string.ajustes_acerca_titulo),
                    detalle = stringResource(R.string.ajustes_acerca_detalle),
                    peligro = false,
                    onClick = onAcercaDe,
                )
            }
        }
    }

    // ---------- Doble confirmación para borrar el historial ----------
    if (pasoConfirmacion == PASO_PRIMERO) {
        AlertDialog(
            onDismissRequest = { pasoConfirmacion = PASO_NINGUNO },
            title = { Text(stringResource(R.string.borrar_primera_titulo)) },
            text = { Text(stringResource(R.string.borrar_primera_mensaje)) },
            confirmButton = {
                TextButton(onClick = { pasoConfirmacion = PASO_SEGUNDO }) { Text(stringResource(R.string.accion_continuar)) }
            },
            dismissButton = {
                TextButton(onClick = { pasoConfirmacion = PASO_NINGUNO }) { Text(stringResource(R.string.accion_cancelar)) }
            },
        )
    }
    if (pasoConfirmacion == PASO_SEGUNDO) {
        AlertDialog(
            onDismissRequest = { pasoConfirmacion = PASO_NINGUNO },
            icon = { Icon(Icons.Outlined.DeleteForever, contentDescription = null) },
            title = { Text(stringResource(R.string.borrar_segunda_titulo)) },
            text = { Text(stringResource(R.string.borrar_segunda_mensaje)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        pasoConfirmacion = PASO_NINGUNO
                        onBorrarHistorial { ambito.launch { avisos.showSnackbar(textoHistorialBorrado) } }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) { Text(stringResource(R.string.ajustes_borrar_historial)) }
            },
            dismissButton = {
                TextButton(onClick = { pasoConfirmacion = PASO_NINGUNO }) { Text(stringResource(R.string.accion_cancelar)) }
            },
        )
    }
}

private const val PASO_NINGUNO = 0
private const val PASO_PRIMERO = 1
private const val PASO_SEGUNDO = 2

/** Tarjeta que agrupa varios ajustes relacionados. */
@Composable
private fun GrupoAjustes(contenido: @Composable () -> Unit) {
    val esp = MaterialTheme.espaciado
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(esp.l),
            verticalArrangement = Arrangement.spacedBy(esp.m),
        ) { contenido() }
    }
}

@Composable
private fun TituloAjuste(texto: String, detalle: String? = null) {
    Column {
        Text(texto, style = MaterialTheme.typography.titleSmall)
        if (detalle != null) {
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Botones segmentados de opción única (tema, unidades). */
@Composable
private fun <T> SelectorSegmentado(
    opciones: List<T>,
    seleccion: T,
    texto: @Composable (T) -> String,
    onSeleccion: (T) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        opciones.forEachIndexed { indice, opcion ->
            SegmentedButton(
                selected = opcion == seleccion,
                onClick = { onSeleccion(opcion) },
                shape = SegmentedButtonDefaults.itemShape(index = indice, count = opciones.size),
                label = { Text(texto(opcion)) },
            )
        }
    }
}

/** Fila con texto e interruptor; toda la fila es tocable. */
@Composable
private fun FilaInterruptor(titulo: String, detalle: String, activo: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = activo, role = Role.Switch, onValueChange = onCambio),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        // onCheckedChange = null: el cambio lo maneja la fila completa.
        Switch(checked = activo, onCheckedChange = null)
    }
}

/** Fila tocable con ícono, título y descripción (borrar historial, acerca de). */
@Composable
private fun FilaAccion(icono: ImageVector, titulo: String, detalle: String, peligro: Boolean, onClick: () -> Unit) {
    val esp = MaterialTheme.espaciado
    val color = if (peligro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(esp.l),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icono, contentDescription = null, tint = color)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = esp.m),
            ) {
                Text(titulo, style = MaterialTheme.typography.titleSmall, color = if (peligro) color else MaterialTheme.colorScheme.onSurface)
                Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@PreviewPantalla
@Composable
private fun AjustesScreenPreview() {
    VistaPrevia {
        AjustesScreen(ajustes = Ajustes(), onAjustesChange = {}, onBorrarHistorial = {}, onAcercaDe = {})
    }
}
