package com.gymguia.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.gymguia.app.R
import com.gymguia.app.domain.model.EstadoTemporizador
import com.gymguia.app.domain.model.FaseTemporizador
import com.gymguia.app.domain.util.formatearTiempo
import com.gymguia.app.ui.temporizador.AJUSTE_RAPIDO_SEGUNDOS
import com.gymguia.app.ui.theme.espaciado

/**
 * Tarjeta flotante del temporizador de descanso.
 * Estados: en curso (cuenta), en pausa (botón reanudar) y terminado (aviso y cerrar).
 */
@Composable
fun TarjetaTemporizador(
    estado: EstadoTemporizador,
    onPausarReanudar: () -> Unit,
    onSumar: () -> Unit,
    onRestar: () -> Unit,
    onOmitir: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val esp = MaterialTheme.espaciado
    val terminado = estado.fase == FaseTemporizador.TERMINADO
    // Al terminar la tarjeta cambia al color de acento para llamar la atención.
    val fondo = if (terminado) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val contenido = if (terminado) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
    // Barra de progreso animada para que no "salte" cada segundo.
    val progreso by animateFloatAsState(targetValue = estado.fraccionRestante, label = "progreso_descanso")

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(containerColor = fondo, contentColor = contenido),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = esp.elevacionFlotante),
    ) {
        Column(modifier = Modifier.padding(esp.l), verticalArrangement = Arrangement.spacedBy(esp.s)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Timer, contentDescription = null)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = esp.m)
                        // Lectores de pantalla anuncian los cambios del tiempo con cortesía.
                        .semantics { liveRegion = LiveRegionMode.Polite },
                ) {
                    Text(
                        text = stringResource(
                            when (estado.fase) {
                                FaseTemporizador.EN_CURSO -> R.string.temporizador_en_curso
                                FaseTemporizador.EN_PAUSA -> R.string.temporizador_en_pausa
                                FaseTemporizador.TERMINADO -> R.string.temporizador_terminado
                            },
                        ),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = formatearTiempo(estado.segundosRestantes),
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
                if (terminado) {
                    IconButton(onClick = onOmitir) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.temporizador_cerrar))
                    }
                } else {
                    FilledIconButton(onClick = onPausarReanudar) {
                        val pausado = estado.fase == FaseTemporizador.EN_PAUSA
                        Icon(
                            imageVector = if (pausado) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                            contentDescription = stringResource(
                                if (pausado) R.string.temporizador_reanudar else R.string.temporizador_pausar,
                            ),
                        )
                    }
                }
            }
            if (!terminado) {
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(esp.grosorProgreso),
                    color = contenido,
                    trackColor = fondo,
                    drawStopIndicator = {},
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onRestar) {
                        Text(stringResource(R.string.temporizador_restar, AJUSTE_RAPIDO_SEGUNDOS))
                    }
                    TextButton(onClick = onSumar) {
                        Text(stringResource(R.string.temporizador_sumar, AJUSTE_RAPIDO_SEGUNDOS))
                    }
                    TextButton(onClick = onOmitir) {
                        Icon(Icons.Filled.SkipNext, contentDescription = null)
                        Text(stringResource(R.string.temporizador_omitir))
                    }
                }
            }
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun TarjetaTemporizadorEnCursoPreview() {
    VistaPrevia {
        TarjetaTemporizador(
            estado = EstadoTemporizador(FaseTemporizador.EN_CURSO, 63, 90),
            onPausarReanudar = {}, onSumar = {}, onRestar = {}, onOmitir = {},
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
        )
    }
}

@PreviewClaroOscuro
@Composable
private fun TarjetaTemporizadorPausaPreview() {
    VistaPrevia {
        TarjetaTemporizador(
            estado = EstadoTemporizador(FaseTemporizador.EN_PAUSA, 41, 90),
            onPausarReanudar = {}, onSumar = {}, onRestar = {}, onOmitir = {},
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
        )
    }
}

@PreviewClaroOscuro
@Composable
private fun TarjetaTemporizadorTerminadoPreview() {
    VistaPrevia {
        TarjetaTemporizador(
            estado = EstadoTemporizador(FaseTemporizador.TERMINADO, 0, 90),
            onPausarReanudar = {}, onSumar = {}, onRestar = {}, onOmitir = {},
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
        )
    }
}
