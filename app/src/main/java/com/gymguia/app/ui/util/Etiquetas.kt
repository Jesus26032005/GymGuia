package com.gymguia.app.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.domain.model.NivelDificultad
import com.gymguia.app.domain.model.RangoFechas
import com.gymguia.app.domain.model.TemaApp
import com.gymguia.app.domain.model.TipoRecord
import com.gymguia.app.domain.model.UnidadPeso
import com.gymguia.app.domain.util.formatearNumero

/*
 * Traducción de los enums del dominio a textos de strings.xml.
 * Así el dominio no depende de recursos de Android y las pantallas
 * nunca escriben textos sueltos.
 */

@get:StringRes
val NivelDificultad.etiqueta: Int
    get() = when (this) {
        NivelDificultad.PRINCIPIANTE -> R.string.nivel_principiante
        NivelDificultad.INTERMEDIO -> R.string.nivel_intermedio
        NivelDificultad.AVANZADO -> R.string.nivel_avanzado
    }

@get:StringRes
val TemaApp.etiqueta: Int
    get() = when (this) {
        TemaApp.SISTEMA -> R.string.tema_sistema
        TemaApp.CLARO -> R.string.tema_claro
        TemaApp.OSCURO -> R.string.tema_oscuro
    }

@get:StringRes
val UnidadPeso.etiqueta: Int
    get() = when (this) {
        UnidadPeso.KG -> R.string.unidad_kg
        UnidadPeso.LB -> R.string.unidad_lb
    }

@get:StringRes
val RangoFechas.etiqueta: Int
    get() = when (this) {
        RangoFechas.DIAS_30 -> R.string.rango_30_dias
        RangoFechas.DIAS_90 -> R.string.rango_90_dias
        RangoFechas.TODO -> R.string.rango_todo
    }

@get:StringRes
val TipoRecord.etiqueta: Int
    get() = when (this) {
        TipoRecord.PESO_MAXIMO -> R.string.record_peso_maximo
        TipoRecord.MAYOR_VOLUMEN -> R.string.record_mayor_volumen
        TipoRecord.UNO_RM_ESTIMADO -> R.string.record_1rm
    }

/** Texto de un peso guardado en kg, convertido a la unidad elegida. Ej: "62.5 kg". */
@Composable
fun textoPeso(pesoKg: Double, unidad: UnidadPeso): String =
    stringResource(R.string.formato_peso, formatearNumero(unidad.desdeKg(pesoKg)), stringResource(unidad.etiqueta))

/** Texto de una serie: "62.5 kg x 8". Si no hay peso (peso corporal) solo muestra repeticiones. */
@Composable
fun textoSerie(pesoKg: Double, repeticiones: Int, unidad: UnidadPeso): String =
    if (pesoKg > 0.0) stringResource(R.string.formato_serie, textoPeso(pesoKg, unidad), repeticiones)
    else stringResource(R.string.formato_solo_repeticiones, repeticiones)
