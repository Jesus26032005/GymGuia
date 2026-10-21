package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.domain.model.LimitesSerie
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.ui.theme.espaciado
import com.gymguia.app.ui.theme.extra
import com.gymguia.app.ui.util.etiqueta

/**
 * Fila de una serie: número, peso, repeticiones, marca de completada y botón eliminar.
 * Recibe el peso ya convertido a la unidad visible; quien la usa se encarga de
 * convertir de vuelta a kg al guardar.
 */
@Composable
fun FilaSerie(
    numero: Int,
    peso: Double,
    repeticiones: Int,
    completada: Boolean,
    unidad: UnidadPeso,
    onPesoChange: (Double) -> Unit,
    onRepeticionesChange: (Int) -> Unit,
    onCompletadaChange: (Boolean) -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val esp = MaterialTheme.espaciado
    val colorCompletada = MaterialTheme.extra.completada
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (completada) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(modifier = Modifier.padding(esp.m), verticalArrangement = Arrangement.spacedBy(esp.s)) {
            // Encabezado: "Serie N", eliminar y marca de completada.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.serie_numero, numero),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onEliminar) {
                    Icon(
                        Icons.Outlined.DeleteOutline,
                        contentDescription = stringResource(R.string.accion_eliminar_serie, numero),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconToggleButton(checked = completada, onCheckedChange = onCompletadaChange) {
                    Icon(
                        imageVector = if (completada) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = stringResource(
                            if (completada) R.string.serie_marcar_pendiente else R.string.serie_marcar_completada,
                        ),
                        tint = if (completada) colorCompletada.fondo else MaterialTheme.colorScheme.outline,
                    )
                }
            }
            // Campos: peso y repeticiones.
            Row(horizontalArrangement = Arrangement.spacedBy(esp.m)) {
                CampoNumerico(
                    valor = peso,
                    onValorChange = onPesoChange,
                    etiqueta = stringResource(R.string.etiqueta_peso, stringResource(unidad.etiqueta)),
                    paso = unidad.paso,
                    minimo = LimitesSerie.PESO_MINIMO_KG,
                    // El máximo está en kg; se convierte a la unidad que se está mostrando.
                    maximo = unidad.desdeKg(LimitesSerie.PESO_MAXIMO_KG),
                    permiteDecimales = true,
                    mensajeError = stringResource(R.string.error_peso),
                    modifier = Modifier.weight(1f),
                )
                CampoNumerico(
                    valor = repeticiones.toDouble(),
                    onValorChange = { onRepeticionesChange(it.toInt()) },
                    etiqueta = stringResource(R.string.etiqueta_repeticiones),
                    paso = 1.0,
                    minimo = LimitesSerie.REPETICIONES_MINIMAS.toDouble(),
                    maximo = LimitesSerie.REPETICIONES_MAXIMAS.toDouble(),
                    mensajeError = stringResource(R.string.error_repeticiones),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun FilaSeriePreview() {
    VistaPrevia {
        Column(
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.espaciado.s),
        ) {
            FilaSerie(1, 60.0, 10, true, UnidadPeso.KG, {}, {}, {}, {})
            FilaSerie(2, 62.5, 8, false, UnidadPeso.KG, {}, {}, {}, {})
        }
    }
}
