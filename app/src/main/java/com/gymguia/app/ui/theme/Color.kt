package com.gymguia.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/*
 * Paleta de Gym Guía.
 *
 * Identidad "Placa": grafito (fondo), amarillo de seguridad (acción y números clave) y tiza
 * (texto). En modo claro el papel es tiza y la acción principal es grafito con detalles en
 * amarillo, porque el amarillo no tiene contraste suficiente como texto sobre claro.
 * Todos los pares "fondo / texto" cumplen contraste AA (4.5:1).
 *
 * Este es el ÚNICO archivo donde se escriben valores de color. Las pantallas siempre
 * leen MaterialTheme.colorScheme o MaterialTheme.extra.
 */

// ---------- Modo claro: tiza y grafito ----------
private val ClaroPrimario = Color(0xFF17191C)
private val ClaroSobrePrimario = Color(0xFFF2C230)
private val ClaroPrimarioContenedor = Color(0xFFF2C230)
private val ClaroSobrePrimarioContenedor = Color(0xFF17191C)
private val ClaroSecundario = Color(0xFF4A4D52)
private val ClaroSobreSecundario = Color(0xFFFFFFFF)
private val ClaroSecundarioContenedor = Color(0xFFF2C230)
private val ClaroSobreSecundarioContenedor = Color(0xFF17191C)
private val ClaroTerciario = Color(0xFF7A5C00)
private val ClaroSobreTerciario = Color(0xFFFFFFFF)
private val ClaroTerciarioContenedor = Color(0xFFFBE7A6)
private val ClaroSobreTerciarioContenedor = Color(0xFF2B2000)
private val ClaroError = Color(0xFFB3261E)
private val ClaroSobreError = Color(0xFFFFFFFF)
private val ClaroErrorContenedor = Color(0xFFF9DEDC)
private val ClaroSobreErrorContenedor = Color(0xFF410E0B)
private val ClaroFondo = Color(0xFFF1EEE6)
private val ClaroSobreFondo = Color(0xFF17191C)
private val ClaroSuperficie = Color(0xFFF1EEE6)
private val ClaroSobreSuperficie = Color(0xFF17191C)
private val ClaroSuperficieVariante = Color(0xFFDDD9CE)
private val ClaroSobreSuperficieVariante = Color(0xFF45474B)
private val ClaroContenedorMasBajo = Color(0xFFFAF8F3)
private val ClaroContenedorBajo = Color(0xFFE9E5DA)
private val ClaroContenedor = Color(0xFFE3DFD3)
private val ClaroContenedorAlto = Color(0xFFDCD8CB)
private val ClaroContenedorMasAlto = Color(0xFFD4D0C2)
private val ClaroContorno = Color(0xFF6C6F73)
private val ClaroContornoVariante = Color(0xFFC4C0B3)
private val ClaroInversoSuperficie = Color(0xFF17191C)
private val ClaroInversoSobreSuperficie = Color(0xFFECE8DF)
private val ClaroInversoPrimario = Color(0xFFF2C230)

// ---------- Modo oscuro: grafito y amarillo de seguridad ----------
private val OscuroPrimario = Color(0xFFF2C230)
private val OscuroSobrePrimario = Color(0xFF17191C)
private val OscuroPrimarioContenedor = Color(0xFF4A3F10)
private val OscuroSobrePrimarioContenedor = Color(0xFFFBE7A6)
private val OscuroSecundario = Color(0xFFECE8DF)
private val OscuroSobreSecundario = Color(0xFF17191C)
private val OscuroSecundarioContenedor = Color(0xFFF2C230)
private val OscuroSobreSecundarioContenedor = Color(0xFF17191C)
private val OscuroTerciario = Color(0xFFF2C230)
private val OscuroSobreTerciario = Color(0xFF17191C)
private val OscuroTerciarioContenedor = Color(0xFF4A3F10)
private val OscuroSobreTerciarioContenedor = Color(0xFFFBE7A6)
private val OscuroError = Color(0xFFF2B8B5)
private val OscuroSobreError = Color(0xFF601410)
private val OscuroErrorContenedor = Color(0xFF8C1D18)
private val OscuroSobreErrorContenedor = Color(0xFFF9DEDC)
private val OscuroFondo = Color(0xFF17191C)
private val OscuroSobreFondo = Color(0xFFECE8DF)
private val OscuroSuperficie = Color(0xFF17191C)
private val OscuroSobreSuperficie = Color(0xFFECE8DF)
private val OscuroSuperficieVariante = Color(0xFF2A2D31)
private val OscuroSobreSuperficieVariante = Color(0xFFB5B2AA)
private val OscuroContenedorMasBajo = Color(0xFF101214)
private val OscuroContenedorBajo = Color(0xFF1D2024)
private val OscuroContenedor = Color(0xFF212428)
private val OscuroContenedorAlto = Color(0xFF2A2D31)
private val OscuroContenedorMasAlto = Color(0xFF34383D)
private val OscuroContorno = Color(0xFF8B8F94)
private val OscuroContornoVariante = Color(0xFF3A3E43)
private val OscuroInversoSuperficie = Color(0xFFECE8DF)
private val OscuroInversoSobreSuperficie = Color(0xFF17191C)
private val OscuroInversoPrimario = Color(0xFF17191C)

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
    completada = ColorEtiqueta(fondo = Color(0xFF17191C), texto = Color(0xFFF2C230)),
    lienzoMapa = Color(0xFFFFFFFF),
)

internal val ExtraOscuro = ColoresExtra(
    principiante = ColorEtiqueta(fondo = Color(0xFF1F4A2A), texto = Color(0xFFB9EBC0)),
    intermedio = ColorEtiqueta(fondo = Color(0xFF5A4300), texto = Color(0xFFFFDF9E)),
    avanzado = ColorEtiqueta(fondo = Color(0xFF6B2018), texto = Color(0xFFFFD5CF)),
    completada = ColorEtiqueta(fondo = Color(0xFFF2C230), texto = Color(0xFF17191C)),
    lienzoMapa = Color(0xFFFFFFFF),
)

/** Permite leer los colores extra desde cualquier composable. */
val LocalColoresExtra = staticCompositionLocalOf { ExtraClaro }
