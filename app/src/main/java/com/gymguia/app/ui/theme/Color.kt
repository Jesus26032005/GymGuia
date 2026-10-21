package com.gymguia.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/*
 * Paleta de Gym Guía.
 *
 * Idea general: un verde azulado (teal) sobrio como color principal, un azul pizarra como
 * secundario y un ámbar apagado como acento (récords, temporizador terminado).
 * Las superficies son grises ligeramente fríos para que se distingan entre sí sin usar
 * blanco ni negro puros. Todos los pares "fondo / texto" cumplen contraste AA (4.5:1).
 *
 * Este es el ÚNICO archivo donde se escriben valores de color. Las pantallas siempre
 * leen MaterialTheme.colorScheme o MaterialTheme.extra.
 */

// ---------- Modo claro ----------
private val ClaroPrimario = Color(0xFF1F6F6B)
private val ClaroSobrePrimario = Color(0xFFFFFFFF)
private val ClaroPrimarioContenedor = Color(0xFFCDE8E5)
private val ClaroSobrePrimarioContenedor = Color(0xFF0B2F2D)
private val ClaroSecundario = Color(0xFF4A5F7A)
private val ClaroSobreSecundario = Color(0xFFFFFFFF)
private val ClaroSecundarioContenedor = Color(0xFFDCE4EF)
private val ClaroSobreSecundarioContenedor = Color(0xFF1B2838)
private val ClaroTerciario = Color(0xFF8A5310)
private val ClaroSobreTerciario = Color(0xFFFFFFFF)
private val ClaroTerciarioContenedor = Color(0xFFF6E1C5)
private val ClaroSobreTerciarioContenedor = Color(0xFF2E1A00)
private val ClaroError = Color(0xFFB3261E)
private val ClaroSobreError = Color(0xFFFFFFFF)
private val ClaroErrorContenedor = Color(0xFFF9DEDC)
private val ClaroSobreErrorContenedor = Color(0xFF410E0B)
private val ClaroFondo = Color(0xFFF6F7F8)
private val ClaroSobreFondo = Color(0xFF191C1D)
private val ClaroSuperficie = Color(0xFFF6F7F8)
private val ClaroSobreSuperficie = Color(0xFF191C1D)
private val ClaroSuperficieVariante = Color(0xFFDDE3E4)
private val ClaroSobreSuperficieVariante = Color(0xFF40484A)
private val ClaroContenedorMasBajo = Color(0xFFFFFFFF)
private val ClaroContenedorBajo = Color(0xFFF0F2F3)
private val ClaroContenedor = Color(0xFFEAEEEF)
private val ClaroContenedorAlto = Color(0xFFE4E9EA)
private val ClaroContenedorMasAlto = Color(0xFFDEE3E4)
private val ClaroContorno = Color(0xFF6F797A)
private val ClaroContornoVariante = Color(0xFFC6CDCE)
private val ClaroInversoSuperficie = Color(0xFF2D3132)
private val ClaroInversoSobreSuperficie = Color(0xFFEFF1F1)
private val ClaroInversoPrimario = Color(0xFF7FD1CB)

// ---------- Modo oscuro (sin negro puro) ----------
private val OscuroPrimario = Color(0xFF7FD1CB)
private val OscuroSobrePrimario = Color(0xFF003735)
private val OscuroPrimarioContenedor = Color(0xFF00504D)
private val OscuroSobrePrimarioContenedor = Color(0xFFBCECE7)
private val OscuroSecundario = Color(0xFFAFC6E4)
private val OscuroSobreSecundario = Color(0xFF19304A)
private val OscuroSecundarioContenedor = Color(0xFF33475F)
private val OscuroSobreSecundarioContenedor = Color(0xFFD5E3F7)
private val OscuroTerciario = Color(0xFFF1BE7E)
private val OscuroSobreTerciario = Color(0xFF462A00)
private val OscuroTerciarioContenedor = Color(0xFF5E3F0E)
private val OscuroSobreTerciarioContenedor = Color(0xFFFFDDB5)
private val OscuroError = Color(0xFFF2B8B5)
private val OscuroSobreError = Color(0xFF601410)
private val OscuroErrorContenedor = Color(0xFF8C1D18)
private val OscuroSobreErrorContenedor = Color(0xFFF9DEDC)
private val OscuroFondo = Color(0xFF111416)
private val OscuroSobreFondo = Color(0xFFE1E3E4)
private val OscuroSuperficie = Color(0xFF111416)
private val OscuroSobreSuperficie = Color(0xFFE1E3E4)
private val OscuroSuperficieVariante = Color(0xFF3F484A)
private val OscuroSobreSuperficieVariante = Color(0xFFBFC8C9)
private val OscuroContenedorMasBajo = Color(0xFF0C0F10)
private val OscuroContenedorBajo = Color(0xFF191C1E)
private val OscuroContenedor = Color(0xFF1D2123)
private val OscuroContenedorAlto = Color(0xFF272B2D)
private val OscuroContenedorMasAlto = Color(0xFF323638)
private val OscuroContorno = Color(0xFF899293)
private val OscuroContornoVariante = Color(0xFF3F484A)
private val OscuroInversoSuperficie = Color(0xFFE1E3E4)
private val OscuroInversoSobreSuperficie = Color(0xFF2D3132)
private val OscuroInversoPrimario = Color(0xFF1F6F6B)

/** Esquema de color Material 3 para el modo claro. */
internal val EsquemaClaro = lightColorScheme(
    primary = ClaroPrimario,
    onPrimary = ClaroSobrePrimario,
    primaryContainer = ClaroPrimarioContenedor,
    onPrimaryContainer = ClaroSobrePrimarioContenedor,
    secondary = ClaroSecundario,
    onSecondary = ClaroSobreSecundario,
    secondaryContainer = ClaroSecundarioContenedor,
    onSecondaryContainer = ClaroSobreSecundarioContenedor,
    tertiary = ClaroTerciario,
    onTertiary = ClaroSobreTerciario,
    tertiaryContainer = ClaroTerciarioContenedor,
    onTertiaryContainer = ClaroSobreTerciarioContenedor,
    error = ClaroError,
    onError = ClaroSobreError,
    errorContainer = ClaroErrorContenedor,
    onErrorContainer = ClaroSobreErrorContenedor,
    background = ClaroFondo,
    onBackground = ClaroSobreFondo,
    surface = ClaroSuperficie,
    onSurface = ClaroSobreSuperficie,
    surfaceVariant = ClaroSuperficieVariante,
    onSurfaceVariant = ClaroSobreSuperficieVariante,
    surfaceContainerLowest = ClaroContenedorMasBajo,
    surfaceContainerLow = ClaroContenedorBajo,
    surfaceContainer = ClaroContenedor,
    surfaceContainerHigh = ClaroContenedorAlto,
    surfaceContainerHighest = ClaroContenedorMasAlto,
    outline = ClaroContorno,
    outlineVariant = ClaroContornoVariante,
    inverseSurface = ClaroInversoSuperficie,
    inverseOnSurface = ClaroInversoSobreSuperficie,
    inversePrimary = ClaroInversoPrimario,
)

/** Esquema de color Material 3 para el modo oscuro. */
internal val EsquemaOscuro = darkColorScheme(
    primary = OscuroPrimario,
    onPrimary = OscuroSobrePrimario,
    primaryContainer = OscuroPrimarioContenedor,
    onPrimaryContainer = OscuroSobrePrimarioContenedor,
    secondary = OscuroSecundario,
    onSecondary = OscuroSobreSecundario,
    secondaryContainer = OscuroSecundarioContenedor,
    onSecondaryContainer = OscuroSobreSecundarioContenedor,
    tertiary = OscuroTerciario,
    onTertiary = OscuroSobreTerciario,
    tertiaryContainer = OscuroTerciarioContenedor,
    onTertiaryContainer = OscuroSobreTerciarioContenedor,
    error = OscuroError,
    onError = OscuroSobreError,
    errorContainer = OscuroErrorContenedor,
    onErrorContainer = OscuroSobreErrorContenedor,
    background = OscuroFondo,
    onBackground = OscuroSobreFondo,
    surface = OscuroSuperficie,
    onSurface = OscuroSobreSuperficie,
    surfaceVariant = OscuroSuperficieVariante,
    onSurfaceVariant = OscuroSobreSuperficieVariante,
    surfaceContainerLowest = OscuroContenedorMasBajo,
    surfaceContainerLow = OscuroContenedorBajo,
    surfaceContainer = OscuroContenedor,
    surfaceContainerHigh = OscuroContenedorAlto,
    surfaceContainerHighest = OscuroContenedorMasAlto,
    outline = OscuroContorno,
    outlineVariant = OscuroContornoVariante,
    inverseSurface = OscuroInversoSuperficie,
    inverseOnSurface = OscuroInversoSobreSuperficie,
    inversePrimary = OscuroInversoPrimario,
)

/** Par de colores para etiquetas: fondo y el texto que va encima. */
@Immutable
data class ColorEtiqueta(val fondo: Color, val texto: Color)

/**
 * Colores semánticos que Material 3 no trae de serie: niveles de dificultad
 * y el color de "serie completada".
 */
@Immutable
data class ColoresExtra(
    val principiante: ColorEtiqueta,
    val intermedio: ColorEtiqueta,
    val avanzado: ColorEtiqueta,
    val completada: ColorEtiqueta,
    /** Fondo blanco de las demostraciones en GIF y del mapa muscular de wger: ambos están dibujados sobre blanco. */
    val lienzoMapa: Color,
)

internal val ExtraClaro = ColoresExtra(
    principiante = ColorEtiqueta(fondo = Color(0xFFD6EFD8), texto = Color(0xFF0F3D17)),
    intermedio = ColorEtiqueta(fondo = Color(0xFFFBE8B8), texto = Color(0xFF4A3300)),
    avanzado = ColorEtiqueta(fondo = Color(0xFFF8D7D3), texto = Color(0xFF5C130C)),
    completada = ColorEtiqueta(fondo = Color(0xFF2E7D4F), texto = Color(0xFFFFFFFF)),
    lienzoMapa = Color(0xFFFFFFFF),
)

internal val ExtraOscuro = ColoresExtra(
    principiante = ColorEtiqueta(fondo = Color(0xFF1F4A2A), texto = Color(0xFFB9EBC0)),
    intermedio = ColorEtiqueta(fondo = Color(0xFF5A4300), texto = Color(0xFFFFDF9E)),
    avanzado = ColorEtiqueta(fondo = Color(0xFF6B2018), texto = Color(0xFFFFD5CF)),
    completada = ColorEtiqueta(fondo = Color(0xFF7ED79B), texto = Color(0xFF00391B)),
    lienzoMapa = Color(0xFFFFFFFF),
)

/** Permite leer los colores extra desde cualquier composable. */
val LocalColoresExtra = staticCompositionLocalOf { ExtraClaro }
