package com.gymguia.app.domain.model

import java.time.LocalDate

/*
 * Modelos de dominio usados por la interfaz.
 * En la Fase 1 se llenan con datos de ejemplo; en fases siguientes los entregan
 * los repositorios (Room y DataStore).
 */

/** Nivel de dificultad de un ejercicio. */
enum class NivelDificultad {
    PRINCIPIANTE, INTERMEDIO, AVANZADO;

    companion object {
        /**
         * Convierte el texto del dataset ("beginner", "intermediate", "expert") o su
         * traducción al español en un nivel. Si no se reconoce, se asume intermedio.
         */
        fun desde(texto: String?): NivelDificultad = when (texto?.trim()?.lowercase()) {
            "beginner", "principiante" -> PRINCIPIANTE
            "expert", "avanzado", "experto" -> AVANZADO
            else -> INTERMEDIO
        }
    }
}

/**
 * Ejercicio del catálogo. Los músculos, el equipo y la categoría se guardan como texto
 * porque el dataset trae muchos valores y los filtros se arman con lo que haya en la base.
 */
data class Ejercicio(
    val id: String,
    val nombre: String,
    // Nombre en inglés del dataset original; sirve de respaldo para la búsqueda.
    val nombreOriginal: String,
    val nivel: NivelDificultad,
    val equipo: String?,
    val categoria: String,
    val fuerza: String?,
    val mecanica: String?,
    val musculosPrimarios: List<String>,
    val musculosSecundarios: List<String>,
    val instrucciones: List<String>,
    // Cada elemento es algo que el cargador de imágenes entiende:
    // un id de drawable (vistas previas) o la ruta de un asset (datos reales).
    val imagenes: List<Any>,
    // GIF animado de la demostración (ExerciseDB): ruta de asset, o null si el ejercicio no tiene.
    val gif: String? = null,
) {
    /** Músculo principal, usado para agrupar y filtrar. */
    val musculoPrincipal: String? get() = musculosPrimarios.firstOrNull()
}

/** Un valor de filtro con la cantidad de ejercicios que lo tienen. */
data class ConteoValor(val valor: String, val cantidad: Int)

/** Una serie registrada: el peso SIEMPRE se guarda en kilogramos. */
data class Serie(
    val id: Long,
    val pesoKg: Double,
    val repeticiones: Int,
    val completada: Boolean,
)

/** Series de la sesión anterior de un ejercicio, con la fecha en que se hicieron. */
data class UltimaVez(val fecha: LocalDate, val series: List<Serie>)

/** Un ejercicio dentro de una sesión del calendario, con sus series. */
data class EjercicioDeSesion(
    val ejercicioId: String,
    val nombreEjercicio: String,
    val series: List<Serie>,
)

/** Un ejercicio con registros, para el selector de Progreso. */
data class OpcionEjercicio(val id: String, val nombre: String)

/** Resumen de un ejercicio en una sesión (pesos en kg; el volumen es peso x repeticiones en kg). */
data class PuntoSesion(val fecha: LocalDate, val pesoMaximoKg: Double, val volumenKg: Double)

/** Punto de una gráfica: etiqueta del eje X y valor del eje Y. */
data class PuntoGrafica(val etiqueta: String, val valor: Float)

/** Tipos de récord personal que se muestran en Progreso. */
enum class TipoRecord { PESO_MAXIMO, MAYOR_VOLUMEN, UNO_RM_ESTIMADO }

/** Récord personal: el valor está en kg (o kg x repeticiones para el volumen). */
data class RecordPersonal(val tipo: TipoRecord, val valorKg: Double, val fecha: LocalDate)

/** Rangos de fechas disponibles en las gráficas de progreso. */
enum class RangoFechas { DIAS_30, DIAS_90, TODO }

/** Preferencia de tema. */
enum class TemaApp { SISTEMA, CLARO, OSCURO }

/**
 * Unidad de peso. El factor convierte desde kilogramos; la conversión solo se aplica
 * al mostrar o capturar valores, nunca a lo guardado.
 */
enum class UnidadPeso(private val factorDesdeKg: Double, val paso: Double) {
    KG(factorDesdeKg = 1.0, paso = 2.5),
    LB(factorDesdeKg = 2.20462, paso = 5.0);

    /** Convierte un valor en kg a esta unidad. */
    fun desdeKg(kg: Double): Double = kg * factorDesdeKg

    /** Convierte un valor capturado en esta unidad a kg. */
    fun aKg(valor: Double): Double = valor / factorDesdeKg
}

/** Límites del descanso por defecto, en segundos. */
object LimitesDescanso {
    const val MINIMO_SEGUNDOS = 15
    const val MAXIMO_SEGUNDOS = 600
    const val PASO_SEGUNDOS = 15
}

/** Ajustes de la app. */
data class Ajustes(
    val tema: TemaApp = TemaApp.SISTEMA,
    val unidad: UnidadPeso = UnidadPeso.KG,
    val descansoSegundos: Int = 90,
    val sonido: Boolean = true,
    val vibracion: Boolean = true,
)

/** Fases posibles del temporizador de descanso. */
enum class FaseTemporizador { EN_CURSO, EN_PAUSA, TERMINADO }

/** Estado visible del temporizador. */
data class EstadoTemporizador(
    val fase: FaseTemporizador,
    val segundosRestantes: Int,
    val segundosTotales: Int,
) {
    /** Fracción restante entre 0 y 1, para la barra de progreso. */
    val fraccionRestante: Float
        get() = if (segundosTotales == 0) 0f else segundosRestantes.toFloat() / segundosTotales
}
