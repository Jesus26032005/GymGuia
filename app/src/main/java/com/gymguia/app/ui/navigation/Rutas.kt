package com.gymguia.app.ui.navigation

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.gymguia.app.R

/** Rutas de navegación de la app. */
object Rutas {
    const val INICIO = "inicio"
    const val EJERCICIOS = "ejercicios"
    const val FAVORITOS = "favoritos"
    const val PROGRESO = "progreso"
    const val AJUSTES = "ajustes"
    const val ACERCA_DE = "acerca_de"

    // Lista de ejercicios con filtros opcionales al venir desde Inicio.
    const val ARG_MUSCULO = "musculo"
    const val ARG_CONSULTA = "consulta"
    const val EJERCICIOS_PATRON = "$EJERCICIOS?$ARG_MUSCULO={$ARG_MUSCULO}&$ARG_CONSULTA={$ARG_CONSULTA}"

    // Detalle de un ejercicio.
    const val ARG_ID = "id"
    const val DETALLE_PATRON = "ejercicio/{$ARG_ID}"

    /** Construye la ruta de la lista con los filtros indicados. */
    fun ejercicios(musculo: String? = null, consulta: String? = null): String {
        val partes = buildList {
            if (musculo != null) add("$ARG_MUSCULO=${Uri.encode(musculo)}")
            if (consulta != null) add("$ARG_CONSULTA=${Uri.encode(consulta)}")
        }
        return if (partes.isEmpty()) EJERCICIOS else "$EJERCICIOS?${partes.joinToString("&")}"
    }

    fun detalle(id: String): String = "ejercicio/${Uri.encode(id)}"
}

/** Pestañas de la barra inferior. */
enum class DestinoInferior(
    val ruta: String,
    @StringRes val etiqueta: Int,
    val iconoActivo: ImageVector,
    val iconoInactivo: ImageVector,
) {
    INICIO(Rutas.INICIO, R.string.nav_inicio, Icons.Filled.Home, Icons.Outlined.Home),
    EJERCICIOS(Rutas.EJERCICIOS, R.string.nav_ejercicios, Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    PROGRESO(Rutas.PROGRESO, R.string.nav_progreso, Icons.Filled.Insights, Icons.Outlined.Insights),
    AJUSTES(Rutas.AJUSTES, R.string.nav_ajustes, Icons.Filled.Settings, Icons.Outlined.Settings),
}
