package com.gymguia.app.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.gymguia.app.domain.model.TemaApp

/**
 * Tema raíz de la app. Recibe la preferencia de tema (sistema, claro u oscuro)
 * y entrega colores, tipografía, formas y espaciado a todo lo que esté dentro.
 */
@Composable
fun GymGuiaTheme(
    tema: TemaApp = TemaApp.SISTEMA,
    content: @Composable () -> Unit,
) {
    // Decide si se usa el modo oscuro según la preferencia del usuario.
    val oscuro = when (tema) {
        TemaApp.SISTEMA -> isSystemInDarkTheme()
        TemaApp.CLARO -> false
        TemaApp.OSCURO -> true
    }
    val esquema = if (oscuro) EsquemaOscuro else EsquemaClaro
    val extra = if (oscuro) ExtraOscuro else ExtraClaro

    // Ajusta el color de los íconos de la barra de estado y de navegación
    // (oscuros sobre fondo claro, claros sobre fondo oscuro).
    val vista = LocalView.current
    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = vista.context.buscarActividad()?.window ?: return@SideEffect
            val controlador = WindowCompat.getInsetsController(ventana, vista)
            controlador.isAppearanceLightStatusBars = !oscuro
            controlador.isAppearanceLightNavigationBars = !oscuro
        }
    }

    CompositionLocalProvider(
        LocalEspaciado provides Espaciado(),
        LocalColoresExtra provides extra,
    ) {
        MaterialTheme(
            colorScheme = esquema,
            typography = Tipografia,
            shapes = Formas,
            content = content,
        )
    }
}

/** Acceso corto al espaciado: MaterialTheme.espaciado.l */
val MaterialTheme.espaciado: Espaciado
    @Composable @ReadOnlyComposable get() = LocalEspaciado.current

/** Acceso corto a los colores semánticos: MaterialTheme.extra.completada */
val MaterialTheme.extra: ColoresExtra
    @Composable @ReadOnlyComposable get() = LocalColoresExtra.current

/** Recorre los ContextWrapper hasta encontrar la Activity que contiene la vista. */
private tailrec fun Context.buscarActividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.buscarActividad()
    else -> null
}
