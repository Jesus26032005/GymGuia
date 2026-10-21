package com.gymguia.app.domain.util

import java.text.Normalizer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

/** Idioma de la app: todas las fechas se formatean en español. */
val LocaleApp: Locale = Locale.forLanguageTag("es")

private const val SEGUNDOS_POR_MINUTO = 60

/** Convierte segundos a "mm:ss". Ejemplo: 95 -> "01:35". */
fun formatearTiempo(segundos: Int): String {
    val seguro = segundos.coerceAtLeast(0)
    return String.format(LocaleApp, "%02d:%02d", seguro / SEGUNDOS_POR_MINUTO, seguro % SEGUNDOS_POR_MINUTO)
}

/**
 * Muestra un número sin decimales si es entero y con un decimal si no lo es.
 * Ejemplo: 40.0 -> "40", 42.5 -> "42.5".
 */
fun formatearNumero(valor: Double): String {
    val redondeado = (valor * 10).roundToInt() / 10.0
    return if (redondeado % 1.0 == 0.0) redondeado.toLong().toString()
    else String.format(Locale.ROOT, "%.1f", redondeado)
}

/**
 * Texto en minúsculas y sin acentos, para comparar al buscar:
 * "Extensión" y "extension" se vuelven iguales.
 */
fun String.paraBusqueda(): String =
    Normalizer.normalize(lowercase(LocaleApp), Normalizer.Form.NFD).replace(MarcasDiacriticas, "")

private val MarcasDiacriticas = Regex("\\p{M}+")

/** Primera letra en mayúscula: "lower back" -> "Lower back". */
fun String.capitalizado(): String = replaceFirstChar { it.titlecase(LocaleApp) }

private val FormatoFechaCorta = DateTimeFormatter.ofPattern("d MMM", LocaleApp)
private val FormatoFechaLarga = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", LocaleApp)
private val FormatoDiaSemana = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", LocaleApp)

/** "12 oct" */
fun LocalDate.formatoCorto(): String = format(FormatoFechaCorta)

/** "12 de octubre de 2026" */
fun LocalDate.formatoLargo(): String = format(FormatoFechaLarga)

/** "Lunes 12 de octubre" */
fun LocalDate.formatoDiaSemana(): String =
    format(FormatoDiaSemana).replaceFirstChar { it.titlecase(LocaleApp) }
