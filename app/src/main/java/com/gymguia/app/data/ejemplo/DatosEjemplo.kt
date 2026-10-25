package com.gymguia.app.data.ejemplo

import com.gymguia.app.R
import com.gymguia.app.domain.util.diasDeLaSemana
import com.gymguia.app.ui.screens.inicio.DiaSemanaUi
import com.gymguia.app.domain.model.Ejercicio
import com.gymguia.app.domain.model.EjercicioDeSesion
import com.gymguia.app.domain.model.ConteoValor
import com.gymguia.app.domain.model.NivelDificultad
import com.gymguia.app.domain.model.OpcionEjercicio
import com.gymguia.app.domain.model.PuntoGrafica
import com.gymguia.app.domain.model.RangoFechas
import com.gymguia.app.domain.model.RecordPersonal
import com.gymguia.app.domain.model.Serie
import com.gymguia.app.domain.model.TipoRecord
import com.gymguia.app.domain.util.formatoCorto
import java.time.LocalDate
import java.time.YearMonth
import com.gymguia.app.ui.screens.progreso.CalendarioUi
import com.gymguia.app.ui.screens.progreso.GraficasUi

/**
 * DATOS DE EJEMPLO (solo Fase 1).
 * Sirven para diseñar y probar las pantallas sin base de datos.
 * Los ejercicios solo sirven para las vistas previas (el catálogo real viene de Room).
 * Las series y el estado del calendario y de Progreso son solo para vistas previas.
 */
object DatosEjemplo {

    /** Semana de ejemplo para las vistas previas: lunes, miércoles y jueves entrenados, hoy viernes. */
    val semana: List<DiaSemanaUi> = diasDeLaSemana(LocalDate.of(2026, 10, 23)).let { dias ->
        dias.map { DiaSemanaUi(it, entrenado = it.dayOfWeek.value in setOf(1, 3, 4), esHoy = it.dayOfWeek.value == 5) }
    }

    private val imagenesDemo = listOf(R.drawable.demo_ejemplo_1, R.drawable.demo_ejemplo_2)

    private fun ejemplo(
        id: String,
        nombre: String,
        musculo: String,
        secundarios: List<String>,
        equipo: String?,
        nivel: NivelDificultad,
        imagenes: List<Any> = imagenesDemo,
    ) = Ejercicio(
        id = id,
        nombre = nombre,
        nombreOriginal = nombre,
        nivel = nivel,
        equipo = equipo,
        categoria = "strength",
        fuerza = "push",
        mecanica = "compound",
        musculosPrimarios = listOf(musculo),
        musculosSecundarios = secundarios,
        instrucciones = listOf(
            "Colócate en la posición inicial con la espalda recta.",
            "Realiza el movimiento de forma controlada.",
            "Regresa lentamente a la posición inicial.",
        ),
        imagenes = imagenes,
    )

    /** Ejercicios ficticios, usados solo en las vistas previas. */
    val ejercicios: List<Ejercicio> = listOf(
        ejemplo("press_banca", "Barbell Bench Press", "chest", listOf("triceps", "shoulders"), "barbell", NivelDificultad.INTERMEDIO),
        ejemplo("sentadilla", "Barbell Squat", "quadriceps", listOf("glutes", "hamstrings"), "barbell", NivelDificultad.INTERMEDIO),
        ejemplo("peso_muerto", "Deadlift", "lower back", listOf("glutes", "hamstrings"), "barbell", NivelDificultad.AVANZADO),
        ejemplo("curl", "Dumbbell Bicep Curl", "biceps", listOf("forearms"), "dumbbell", NivelDificultad.PRINCIPIANTE),
        ejemplo("plancha", "Plank", "abdominals", listOf("shoulders"), null, NivelDificultad.PRINCIPIANTE, listOf(R.drawable.demo_ejemplo_1)),
    )

    val musculos: List<ConteoValor> = listOf(
        ConteoValor("abdominals", 93), ConteoValor("biceps", 53), ConteoValor("chest", 84),
        ConteoValor("quadriceps", 148), ConteoValor("shoulders", 129),
    )

    val equipos: List<ConteoValor> = listOf(
        ConteoValor("barbell", 170), ConteoValor("body only", 111), ConteoValor("dumbbell", 123),
    )

    /** Ids marcados como favoritos al iniciar. */
    val favoritosIniciales: Set<String> = setOf("press_banca", "sentadilla")

    /** Series de hoy para la pantalla de detalle. */
    val seriesDeHoy: List<Serie> = listOf(
        Serie(id = 1, pesoKg = 60.0, repeticiones = 10, completada = true),
        Serie(id = 2, pesoKg = 62.5, repeticiones = 8, completada = false),
    )

    /** Series de la sesión anterior ("Última vez"). */
    val seriesUltimaVez: List<Serie> = listOf(
        Serie(id = 1, pesoKg = 57.5, repeticiones = 10, completada = true),
        Serie(id = 2, pesoKg = 60.0, repeticiones = 8, completada = true),
        Serie(id = 3, pesoKg = 60.0, repeticiones = 7, completada = true),
    )
    val fechaUltimaVez: LocalDate = LocalDate.now().minusDays(3)

    /** Estado de ejemplo del calendario, solo para las vistas previas. */
    fun calendario(): CalendarioUi {
        val hoy = LocalDate.now()
        return CalendarioUi(
            mes = YearMonth.from(hoy),
            hoy = hoy,
            diasEntrenados = setOf(2, 4, 6, 9, 11, 13, 16, 18, 20, 23, 25, 27).filter { it <= YearMonth.from(hoy).lengthOfMonth() }.toSet(),
            diaSeleccionado = hoy.dayOfMonth,
            racha = 3,
            sesion = listOf(
                EjercicioDeSesion("press_banca", "Press de banca con barra", seriesUltimaVez),
                EjercicioDeSesion("dominadas", "Dominadas", seriesDeHoy),
            ),
        )
    }

    private val pesosMaximos = listOf(50f, 52.5f, 52.5f, 55f, 55f, 57.5f, 57.5f, 60f, 60f, 62.5f, 62.5f, 65f)
    private val volumenes = listOf(1400f, 1520f, 1480f, 1650f, 1600f, 1720f, 1800f, 1760f, 1900f, 1950f, 2010f, 2100f)

    private fun puntos(valores: List<Float>): List<PuntoGrafica> = valores.mapIndexed { i, valor ->
        PuntoGrafica(LocalDate.now().minusWeeks((valores.size - 1 - i).toLong()).formatoCorto(), valor)
    }

    /** Estado de ejemplo de Progreso, solo para las vistas previas. */
    fun graficas(): GraficasUi = GraficasUi(
        ejercicios = listOf(
            OpcionEjercicio("press_banca", "Press de banca con barra"),
            OpcionEjercicio("sentadilla", "Sentadilla con barra"),
        ),
        ejercicioId = "press_banca",
        rango = RangoFechas.DIAS_90,
        pesoMaximo = puntos(pesosMaximos),
        volumen = puntos(volumenes),
        records = listOf(
            RecordPersonal(TipoRecord.PESO_MAXIMO, 65.0, LocalDate.now().minusDays(2)),
            RecordPersonal(TipoRecord.MAYOR_VOLUMEN, 2100.0, LocalDate.now().minusDays(2)),
            RecordPersonal(TipoRecord.UNO_RM_ESTIMADO, 80.2, LocalDate.now().minusDays(9)),
        ),
    )
}
