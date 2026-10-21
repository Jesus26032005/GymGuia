package com.gymguia.app.ui.screens.progreso

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.RangoFechas
import com.gymguia.app.domain.model.Serie
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.components.PreviewPantalla
import com.gymguia.app.ui.components.VistaPrevia

/** Pestañas de la sección. */
private enum class PestanaProgreso(val titulo: Int) {
    CALENDARIO(R.string.pestana_calendario),
    PROGRESO(R.string.pestana_progreso),
}

/** Contenedor con las pestañas "Calendario" y "Progreso". No calcula nada: muestra lo que le da el ViewModel. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgresoScreen(
    calendario: CalendarioUi,
    graficas: GraficasUi,
    unidad: UnidadPeso,
    onCambiarMes: (Int) -> Unit,
    onDia: (Int) -> Unit,
    onActualizarSerie: (Serie) -> Unit,
    onEliminarSerie: (Long) -> Unit,
    onEjercicio: (String) -> Unit,
    onRango: (RangoFechas) -> Unit,
) {
    var pestana by rememberSaveable { mutableIntStateOf(PestanaProgreso.CALENDARIO.ordinal) }

    PantallaBase(titulo = stringResource(R.string.titulo_progreso)) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno),
        ) {
            PrimaryTabRow(
                selectedTabIndex = pestana,
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                PestanaProgreso.entries.forEach { p ->
                    Tab(
                        selected = pestana == p.ordinal,
                        onClick = { pestana = p.ordinal },
                        text = { Text(stringResource(p.titulo)) },
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            when (PestanaProgreso.entries[pestana]) {
                PestanaProgreso.CALENDARIO -> CalendarioContenido(
                    calendario = calendario,
                    unidad = unidad,
                    onCambiarMes = onCambiarMes,
                    onDia = onDia,
                    onActualizarSerie = onActualizarSerie,
                    onEliminarSerie = onEliminarSerie,
                )
                PestanaProgreso.PROGRESO -> GraficasContenido(
                    graficas = graficas,
                    unidad = unidad,
                    onEjercicio = onEjercicio,
                    onRango = onRango,
                )
            }
        }
    }
}

@PreviewPantalla
@Composable
private fun ProgresoScreenPreview() {
    VistaPrevia {
        ProgresoScreen(
            calendario = DatosEjemplo.calendario(),
            graficas = DatosEjemplo.graficas(),
            unidad = UnidadPeso.KG,
            onCambiarMes = {}, onDia = {}, onActualizarSerie = {}, onEliminarSerie = {},
            onEjercicio = {}, onRango = {},
        )
    }
}
