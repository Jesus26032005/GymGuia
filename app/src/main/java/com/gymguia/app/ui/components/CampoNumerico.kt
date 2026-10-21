package com.gymguia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.gymguia.app.R
import com.gymguia.app.domain.util.formatearNumero
import com.gymguia.app.ui.theme.espaciado
import kotlin.math.abs

/** Acepta números con hasta un punto o coma decimal (para la captura de peso). */
private val PatronDecimal = Regex("^\\d*([.,]\\d*)?$")
private val PatronEntero = Regex("^\\d*$")

/** Diferencia máxima para considerar iguales dos valores (evita errores de redondeo kg/lb). */
private const val TOLERANCIA = 0.05

/** Como máximo se pueden escribir tantos caracteres (evita números absurdos). */
private const val LARGO_MAXIMO_TEXTO = 7

/**
 * Campo numérico con botones de menos y más.
 * - Los botones suman o restan [paso] sin salirse de [minimo] y [maximo].
 * - También se puede escribir el valor a mano. Si lo escrito no es válido (vacío o fuera de
 *   rango) el campo se marca en rojo con [mensajeError], el valor guardado no cambia, y al salir
 *   del campo vuelve al último valor válido.
 * - Mientras el usuario escribe, el texto no se reemplaza por valores que llegan de fuera.
 */
@Composable
fun CampoNumerico(
    valor: Double,
    onValorChange: (Double) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    paso: Double = 1.0,
    minimo: Double = 0.0,
    maximo: Double = Double.MAX_VALUE,
    permiteDecimales: Boolean = false,
    mensajeError: String = "",
) {
    val esp = MaterialTheme.espaciado
    // Texto que se está editando (puede estar a medias, por ejemplo "62.").
    var texto by remember { mutableStateOf(formatearNumero(valor)) }
    var enfocado by remember { mutableStateOf(false) }
    val gestorFoco = LocalFocusManager.current

    // Si el valor cambia desde fuera (cambio de unidad, otra pantalla) y el usuario no está
    // escribiendo, el texto se actualiza. Mientras escribe, se respeta lo que teclea.
    LaunchedEffect(valor, enfocado) {
        val escrito = texto.replace(',', '.').toDoubleOrNull()
        if (!enfocado && (escrito == null || abs(escrito - valor) > TOLERANCIA)) texto = formatearNumero(valor)
    }

    val escrito = texto.replace(',', '.').toDoubleOrNull()
    val invalido = escrito == null || escrito < minimo || escrito > maximo
    val colores = MaterialTheme.colorScheme

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(esp.xs)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = colores.onSurfaceVariant,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(esp.xs),
        ) {
            FilledTonalIconButton(
                onClick = {
                    val nuevo = (valor - paso).coerceAtLeast(minimo)
                    texto = formatearNumero(nuevo)
                    onValorChange(nuevo)
                },
                enabled = valor > minimo,
                modifier = Modifier.size(esp.botonCampoNumerico),
            ) {
                Icon(Icons.Filled.Remove, contentDescription = stringResource(R.string.accion_restar, etiqueta))
            }
            BasicTextField(
                value = texto,
                onValueChange = { nuevo ->
                    val patron = if (permiteDecimales) PatronDecimal else PatronEntero
                    if (nuevo.length <= LARGO_MAXIMO_TEXTO && nuevo.matches(patron)) {
                        texto = nuevo
                        // Solo se avisa del cambio si el texto es un número válido.
                        nuevo.replace(',', '.').toDoubleOrNull()
                            ?.takeIf { it in minimo..maximo }
                            ?.let(onValorChange)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = esp.anchoMinimoCampo)
                    .onFocusChanged { estado ->
                        enfocado = estado.isFocused
                        // Al salir con un texto inválido se vuelve al último valor guardado.
                        if (!estado.isFocused && invalido) texto = formatearNumero(valor)
                    }
                    .semantics { if (invalido) error(mensajeError) },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = if (invalido) colores.error else colores.onSurface,
                    textAlign = TextAlign.Center,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (permiteDecimales) KeyboardType.Decimal else KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                // La tecla "listo" cierra la edición: si el texto era inválido, vuelve al último valor.
                keyboardActions = KeyboardActions(onDone = { gestorFoco.clearFocus() }),
                cursorBrush = SolidColor(colores.primary),
                decorationBox = { campo ->
                    Box(
                        modifier = Modifier
                            .height(esp.alturaCampoNumerico)
                            .background(
                                if (invalido) colores.errorContainer else colores.surfaceContainerHighest,
                                MaterialTheme.shapes.small,
                            )
                            .then(
                                if (invalido) Modifier.border(esp.bordeFino, colores.error, MaterialTheme.shapes.small)
                                else Modifier,
                            )
                            .padding(horizontal = esp.s),
                        contentAlignment = Alignment.Center,
                    ) { campo() }
                },
            )
            FilledTonalIconButton(
                onClick = {
                    val nuevo = (valor + paso).coerceAtMost(maximo)
                    texto = formatearNumero(nuevo)
                    onValorChange(nuevo)
                },
                enabled = valor < maximo,
                modifier = Modifier.size(esp.botonCampoNumerico),
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.accion_sumar, etiqueta))
            }
        }
        // El aviso ocupa su lugar solo cuando hay un error, para que la fila no "salte" al escribir bien.
        if (invalido && mensajeError.isNotEmpty()) {
            Text(
                text = mensajeError,
                style = MaterialTheme.typography.labelSmall,
                color = colores.error,
            )
        }
    }
}

@PreviewClaroOscuro
@Composable
private fun CampoNumericoPreview() {
    VistaPrevia {
        CampoNumerico(
            valor = 62.5,
            onValorChange = {},
            etiqueta = stringResource(R.string.etiqueta_peso, stringResource(R.string.unidad_kg)),
            paso = 2.5,
            permiteDecimales = true,
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
        )
    }
}
