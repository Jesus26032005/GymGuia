package com.gymguia.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import com.gymguia.app.data.ejemplo.DatosEjemplo
import com.gymguia.app.domain.model.PuntoGrafica
import com.gymguia.app.domain.util.formatearNumero
import com.gymguia.app.ui.theme.espaciado

/** Número de líneas horizontales de referencia (mínimo, medio y máximo). */
private const val LINEAS_GRILLA = 3

/** Transparencia del área bajo la línea. */
private const val ALFA_RELLENO = 0.12f

/**
 * Gráfica de línea dibujada a mano con Canvas (sin librerías externas).
 * - Eje Y: tres valores de referencia a la izquierda.
 * - Eje X: primera y última etiqueta.
 * - Línea con puntos y un relleno suave debajo.
 */
@Composable
fun GraficaLinea(
    puntos: List<PuntoGrafica>,
    descripcion: String,
    modifier: Modifier = Modifier,
    colorLinea: Color = MaterialTheme.colorScheme.primary,
    formatoValor: (Float) -> String = { formatearNumero(it.toDouble()) },
) {
    val esp = MaterialTheme.espaciado
    // Los valores del tema se leen fuera del Canvas (el Canvas no es composable).
    val colorGrilla = MaterialTheme.colorScheme.outlineVariant
    val estiloTexto = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    val colorFondoPunto = MaterialTheme.colorScheme.surface
    val medidor = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(esp.alturaGrafica)
            .semantics { contentDescription = descripcion },
    ) {
        if (puntos.isEmpty()) return@Canvas

        // Área útil de dibujo, dejando espacio para las etiquetas de los ejes.
        val izquierda = esp.margenEjeY.toPx()
        val arriba = esp.s.toPx()
        val abajo = size.height - esp.margenEjeX.toPx()
        val ancho = size.width - izquierda
        val alto = abajo - arriba

        // Rango del eje Y. Si todos los valores son iguales se abre un poco para no dividir entre cero.
        val minimo = puntos.minOf { it.valor }
        val maximoReal = puntos.maxOf { it.valor }
        val maximo = if (maximoReal == minimo) minimo + 1f else maximoReal

        // Convierte un índice y un valor a coordenadas de pantalla.
        fun x(indice: Int): Float =
            if (puntos.size == 1) izquierda + ancho / 2 else izquierda + ancho * indice / (puntos.size - 1)
        fun y(valor: Float): Float = abajo - (valor - minimo) / (maximo - minimo) * alto

        // 1. Líneas de referencia y sus valores.
        repeat(LINEAS_GRILLA) { i ->
            val fraccion = i / (LINEAS_GRILLA - 1f)
            val valor = minimo + (maximo - minimo) * fraccion
            val posY = y(valor)
            drawLine(
                color = colorGrilla,
                start = Offset(izquierda, posY),
                end = Offset(size.width, posY),
                strokeWidth = esp.grosorGrilla.toPx(),
            )
            val texto = medidor.measure(formatoValor(valor), estiloTexto)
            drawText(texto, topLeft = Offset(0f, posY - texto.size.height / 2))
        }

        // 2. Relleno bajo la línea y la línea misma.
        val linea = Path()
        puntos.forEachIndexed { i, punto ->
            if (i == 0) linea.moveTo(x(i), y(punto.valor)) else linea.lineTo(x(i), y(punto.valor))
        }
        val relleno = Path().apply {
            addPath(linea)
            lineTo(x(puntos.lastIndex), abajo)
            lineTo(x(0), abajo)
            close()
        }
        drawPath(relleno, color = colorLinea.copy(alpha = ALFA_RELLENO))
        drawPath(
            linea,
            color = colorLinea,
            style = Stroke(width = esp.grosorLineaGrafica.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )

        // 3. Puntos: círculo de color con centro del color de la superficie.
        val radio = esp.radioPuntoGrafica.toPx()
        puntos.forEachIndexed { i, punto ->
            val centro = Offset(x(i), y(punto.valor))
            drawCircle(colorLinea, radius = radio, center = centro)
            drawCircle(colorFondoPunto, radius = radio / 2, center = centro)
        }

        // 4. Etiquetas del eje X: primera alineada a la izquierda y última a la derecha.
        val primera = medidor.measure(puntos.first().etiqueta, estiloTexto)
        drawText(primera, topLeft = Offset(izquierda, abajo + esp.xs.toPx()))
        if (puntos.size > 1) {
            val ultima = medidor.measure(puntos.last().etiqueta, estiloTexto)
            drawText(ultima, topLeft = Offset(size.width - ultima.size.width, abajo + esp.xs.toPx()))
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun GraficaLineaPreview() {
    VistaPrevia {
        GraficaLinea(
            puntos = DatosEjemplo.graficas().pesoMaximo,
            descripcion = "",
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
        )
    }
}
