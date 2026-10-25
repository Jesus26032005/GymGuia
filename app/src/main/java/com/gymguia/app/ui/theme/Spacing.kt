package com.gymguia.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Todos los tamaños de la app: márgenes, íconos, alturas de componentes y medidas
 * de las gráficas. Las pantallas nunca escriben "16.dp" directamente; leen
 * MaterialTheme.espaciado.
 */
@Immutable
data class Espaciado(
    // Escala base de márgenes (múltiplos de 4).
    val xs: Dp = 4.dp,
    val s: Dp = 8.dp,
    val m: Dp = 12.dp,
    val l: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,

    // Márgenes de pantalla y bordes.
    val margenPantalla: Dp = 16.dp,
    val bordeFino: Dp = 1.dp,
    val bordeResaltado: Dp = 2.dp,

    // Íconos.
    val iconoS: Dp = 18.dp,
    val iconoM: Dp = 24.dp,
    val iconoL: Dp = 32.dp,
    val iconoXl: Dp = 56.dp,

    // Controles.
    val alturaMinimaTactil: Dp = 48.dp,
    val botonCampoNumerico: Dp = 48.dp,
    val alturaCampoNumerico: Dp = 60.dp,
    val alturaBotonPrincipal: Dp = 56.dp,
    val alturaBarraBusqueda: Dp = 56.dp,

    // Imágenes.
    val miniaturaEjercicio: Dp = 72.dp,
    val alturaDemo: Dp = 240.dp,
    val alturaMapaMuscular: Dp = 280.dp,

    // Semana de Inicio (discos).
    val discoSemana: Dp = 36.dp,
    val anilloDisco: Dp = 7.dp,

    // Calendario.
    val marcaDiaEntrenado: Dp = 5.dp,

    // Gráficas (Canvas).
    val alturaGrafica: Dp = 180.dp,
    val margenEjeY: Dp = 44.dp,
    val margenEjeX: Dp = 22.dp,
    val grosorLineaGrafica: Dp = 2.5.dp,
    val grosorGrilla: Dp = 1.dp,
    val radioPuntoGrafica: Dp = 3.5.dp,

    // Elevaciones.
    val elevacionTarjeta: Dp = 1.dp,
    val elevacionFlotante: Dp = 6.dp,

    // Indicadores.
    val grosorProgreso: Dp = 6.dp,
)

/** Permite leer el espaciado desde cualquier composable. */
val LocalEspaciado = staticCompositionLocalOf { Espaciado() }
